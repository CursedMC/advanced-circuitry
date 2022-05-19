package dev.cursedmc.ac.features.circuit.block

import com.kneelawk.graphlib.GraphLib
import com.kneelawk.graphlib.graph.BlockNode
import dev.cursedmc.ac.features.circuit.PowerManager
import dev.cursedmc.ac.features.circuit.block.node.WireBlockNode
import dev.cursedmc.ac.node.container.BlockNodeProvider
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.enums.WireConnection
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Direction.*
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView

@Suppress("OVERRIDE_DEPRECATION")
class WireBlock : Block(
	FabricBlockSettings
		.copyOf(net.minecraft.block.Blocks.IRON_BLOCK)
		.collidable(false)
		.luminance { if (it[POWERED]) 7 else 0 }
		.nonOpaque()
), BlockNodeProvider {
	init {
		this.defaultState = this.defaultState.with(WIRE_CONNECTION_NORTH, WireConnection.NONE).with(WIRE_CONNECTION_EAST, WireConnection.NONE).with(WIRE_CONNECTION_SOUTH, WireConnection.NONE).with(WIRE_CONNECTION_WEST, WireConnection.NONE).with(POWERED, false)
		
//		if (true) true
	}
	
	override fun getOutlineShape(
		state: BlockState,
		world: BlockView,
		pos: BlockPos,
		context: ShapeContext,
	): VoxelShape {
		return SHAPE
	}
	
	override fun neighborUpdate(
		state: BlockState,
		world: World,
		pos: BlockPos,
		block: Block,
		fromPos: BlockPos,
		notify: Boolean,
	) {
		if (world.isClient || world !is ServerWorld) return // just in case (like mojang does it!)
		
		if (!state.canPlaceAt(world, pos)) { // make sure we're on a full block face
			dropStacks(state, world, pos)
			world.removeBlock(pos, false)
		} else {
			GraphLib.getController(world).updateConnections(pos)
			
			// makes it so that a lever or redstone torch changing will update this wires powered state
			PowerManager.scheduleUpdate(world, pos)
		}
	}
	
	override fun onPlaced(
		world: World,
		pos: BlockPos,
		state: BlockState,
		placer: LivingEntity?,
		itemStack: ItemStack
	) {
		if (world.isClient || world !is ServerWorld) return // why does mojang get away with this
		
		GraphLib.getController(world).onChanged(pos)
	}
	
	override fun onBroken(world: WorldAccess, pos: BlockPos, state: BlockState) {
		disconnectFromNeighbors(world, pos)
		
		if (world.isClient || world !is ServerWorld) return // i'm fucking tired
		
		// shut up and keep typing. nobody cares
		GraphLib.getController(world).onChanged(pos)
	}
	
	override fun getWeakRedstonePower(
		state: BlockState,
		world: BlockView,
		pos: BlockPos,
		direction: Direction,
	): Int {
		return if (state.get(POWERED)) {
			15
		} else 0
	}
	
	override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
		return world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos, UP)
	}
	
	/**
	 * cursed mojang shit i never want to touch again
	 */
	override fun getStateForNeighborUpdate(
		state: BlockState,
		direction: Direction,
		neighborState: BlockState,
		world: WorldAccess,
		pos: BlockPos,
		neighborPos: BlockPos,
	): BlockState {
		val directions: MutableList<Direction> = mutableListOf()
		if (neighborState.block is WireBlock) {
			directions.add(direction)
		}
		for (dir in values()) {
			if (dir == direction) continue
			if (world.getBlockState(pos.offset(dir))?.block is WireBlock) {
				directions.add(dir)
			}
		}
		
		var newState = state
		
		for (dir in directions) {
			newState = when (dir) {
				NORTH -> {
					newState.with(WIRE_CONNECTION_NORTH, WireConnection.SIDE)
				}
				SOUTH -> {
					newState.with(WIRE_CONNECTION_SOUTH, WireConnection.SIDE)
				}
				WEST -> {
					newState.with(WIRE_CONNECTION_WEST, WireConnection.SIDE)
				}
				EAST -> {
					newState.with(WIRE_CONNECTION_EAST, WireConnection.SIDE)
				}
				else -> newState
			}
		}
		
		return getStateForVerticalNeighbors(newState, world, pos)
	}
	
	private fun getStateForVerticalNeighbors(
		state: BlockState,
		world: WorldAccess,
		pos: BlockPos,
	): BlockState {
		// update self
		val directions = listOf(NORTH, EAST, SOUTH, WEST)
		val upPos = pos.offset(UP)
		var newState = state
		for (dir in directions) {
			if (world.getBlockState(upPos?.offset(dir))?.block !is WireBlock) continue
			newState = when (dir) {
				NORTH -> {
					newState.with(WIRE_CONNECTION_NORTH, WireConnection.UP)
				}
				EAST -> {
					newState.with(WIRE_CONNECTION_EAST, WireConnection.UP)
				}
				SOUTH -> {
					newState.with(WIRE_CONNECTION_SOUTH, WireConnection.UP)
				}
				WEST -> {
					newState.with(WIRE_CONNECTION_WEST, WireConnection.UP)
				}
				else -> newState
			}
		}
		
		return newState
	}
	
	override fun prepare(state: BlockState, world: WorldAccess, pos: BlockPos, flags: Int, maxUpdateDepth: Int) {
		// update neighbors above
		val directions = listOf(NORTH, EAST, SOUTH, WEST)
		val upPos = pos.offset(UP)
		for (dir in directions) { // go through every neighbor of the block above us
			val neighborPos = upPos?.offset(dir)
			val neighborState = world.getBlockState(neighborPos) // the block state we're fucking with
			if (neighborState?.block !is WireBlock) continue
			replace(neighborState, neighborState.with(EnumProperty.of(dir.opposite.name.lowercase(), WireConnection::class.java), WireConnection.SIDE), world, neighborPos, flags, maxUpdateDepth)
		}
		
		// update neighbors below
		val downPos = pos.offset(DOWN)
		for (dir in directions) { // go through every neighbor of the block below us
			val neighborPos = downPos?.offset(dir)
			val neighborState = world.getBlockState(neighborPos) // the block state we're fucking with
			if (neighborState?.block !is WireBlock) continue
			replace(neighborState, neighborState.with(EnumProperty.of(dir.opposite.name.lowercase(), WireConnection::class.java), WireConnection.UP), world, neighborPos, flags, maxUpdateDepth)
		}
	}
	
	private fun disconnectFromNeighbors(world: WorldAccess, pos: BlockPos) {
		// update neighbors above
		val upPos = pos.offset(UP)
		disconnectFrom(world, upPos)
		
		// update neighbors below
		val downPos = pos.offset(DOWN)
		disconnectFrom(world, downPos)
		
		// update horizontal neighbors
		disconnectFrom(world, pos)
	}
	
	private fun disconnectFrom(world: WorldAccess, pos: BlockPos) {
		val directions = listOf(NORTH, EAST, SOUTH, WEST)
		for (dir in directions) {
			val neighborPos = pos.offset(dir)
			val neighborState = world.getBlockState(neighborPos) // the block state we're fucking with
			if (neighborState?.block !is WireBlock) continue // make sure our neighbor is one of us
			replace(neighborState, neighborState.with(EnumProperty.of(dir.opposite.name.lowercase(), WireConnection::class.java), WireConnection.NONE), world, neighborPos, 0)
		}
	}
	
	override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
		builder.add(WIRE_CONNECTION_NORTH, WIRE_CONNECTION_EAST, WIRE_CONNECTION_SOUTH, WIRE_CONNECTION_WEST, POWERED)
	}
	
	override fun createBlockNodes(): Collection<BlockNode> {
		return listOf(WireBlockNode)
	}
	
	companion object {
		private val SHAPE = createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0)!!
		val POWERED: BooleanProperty = Properties.POWERED
		val WIRE_CONNECTION_NORTH: EnumProperty<WireConnection> = Properties.NORTH_WIRE_CONNECTION
		val WIRE_CONNECTION_EAST: EnumProperty<WireConnection> = Properties.EAST_WIRE_CONNECTION
		val WIRE_CONNECTION_SOUTH: EnumProperty<WireConnection> = Properties.SOUTH_WIRE_CONNECTION
		val WIRE_CONNECTION_WEST: EnumProperty<WireConnection> = Properties.WEST_WIRE_CONNECTION
	}
}
