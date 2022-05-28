package dev.cursedmc.ac.features.circuit.block.component

import com.kneelawk.graphlib.GraphLib
import dev.cursedmc.ac.features.circuit.PowerManager
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
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView

@Suppress("OVERRIDE_DEPRECATION")
abstract class PoweredComponentBlock(settings: Settings) : Block(settings), PoweredComponent {
	init {
		this.defaultState = this.defaultState.with(WIRE_CONNECTION_NORTH, WireConnection.NONE).with(WIRE_CONNECTION_EAST, WireConnection.NONE).with(
			WIRE_CONNECTION_SOUTH, WireConnection.NONE).with(WIRE_CONNECTION_WEST, WireConnection.NONE).with(POWERED, false)
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
		
		val fromState = world.getBlockState(fromPos)
		if (fromState.block is PoweredComponent) return // don't accept block updates from other components
		
		if (!state.canPlaceAt(world, pos)) { // make sure we're on a full block face
			dropStacks(state, world, pos)
			world.removeBlock(pos, false)
			GraphLib.getController(world).updateNodes(pos)
		} else {
			GraphLib.getController(world).updateConnections(pos)
			
			// makes it so that a lever or redstone torch changing will update this wire's powered state
			PowerManager.update(world, pos)
		}
	}
	
	override fun getOutlineShape(
		state: BlockState,
		world: BlockView,
		pos: BlockPos,
		context: ShapeContext,
	): VoxelShape {
		return SHAPE
	}
	
	override fun onPlaced(
		world: World,
		pos: BlockPos,
		state: BlockState,
		placer: LivingEntity?,
		itemStack: ItemStack
	) {
		if (world.isClient || world !is ServerWorld) return // why does mojang get away with this
		
		GraphLib.getController(world).updateConnections(pos)
		
		GraphLib.getController(world).updateNodes(pos)
	}
	
	override fun onBroken(world: WorldAccess, pos: BlockPos, state: BlockState) {
		disconnectFromNeighbors(world, pos)
		
		if (world.isClient || world !is ServerWorld) return // i'm fucking tired
		
		// shut up and keep typing. nobody cares
		GraphLib.getController(world).updateNodes(pos)
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
		return world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos, Direction.UP)
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
		if (neighborState.isOf(this)) {
			directions.add(direction)
		}
		for (dir in Direction.values()) {
			if (dir == direction) continue
			if (world.getBlockState(pos.offset(dir)).block is PoweredComponent) {
				directions.add(dir)
			}
		}
		
		var newState = state
		
		for (dir in directions) {
			newState = when (dir) {
				Direction.NORTH -> {
					newState.with(WIRE_CONNECTION_NORTH, WireConnection.SIDE)
				}
				Direction.SOUTH -> {
					newState.with(WIRE_CONNECTION_SOUTH, WireConnection.SIDE)
				}
				Direction.WEST -> {
					newState.with(WIRE_CONNECTION_WEST, WireConnection.SIDE)
				}
				Direction.EAST -> {
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
		val directions = listOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)
		val upPos = pos.offset(Direction.UP)
		var newState = state
		for (dir in directions) {
			if (world.getBlockState(upPos?.offset(dir))?.block !is PoweredComponent) continue
			newState = when (dir) {
				Direction.NORTH -> {
					newState.with(WIRE_CONNECTION_NORTH, WireConnection.UP)
				}
				Direction.EAST -> {
					newState.with(WIRE_CONNECTION_EAST, WireConnection.UP)
				}
				Direction.SOUTH -> {
					newState.with(WIRE_CONNECTION_SOUTH, WireConnection.UP)
				}
				Direction.WEST -> {
					newState.with(WIRE_CONNECTION_WEST, WireConnection.UP)
				}
				else -> newState
			}
		}
		
		return newState
	}
	
	override fun prepare(state: BlockState, world: WorldAccess, pos: BlockPos, flags: Int, maxUpdateDepth: Int) {
		// update neighbors above
		val directions = listOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)
		val upPos = pos.offset(Direction.UP)
		for (dir in directions) { // go through every neighbor of the block above us
			val neighborPos = upPos?.offset(dir)
			val neighborState = world.getBlockState(neighborPos) // the block state we're fucking with
			if (neighborState?.block !is PoweredComponent) continue
			replace(neighborState, neighborState.with(EnumProperty.of(dir.opposite.name.lowercase(), WireConnection::class.java), WireConnection.SIDE), world, neighborPos, flags, maxUpdateDepth)
		}
		
		// update neighbors below
		val downPos = pos.offset(Direction.DOWN)
		for (dir in directions) { // go through every neighbor of the block below us
			val neighborPos = downPos?.offset(dir)
			val neighborState = world.getBlockState(neighborPos) // the block state we're fucking with
			if (neighborState?.block !is PoweredComponent) continue
			replace(neighborState, neighborState.with(EnumProperty.of(dir.opposite.name.lowercase(), WireConnection::class.java), WireConnection.UP), world, neighborPos, flags, maxUpdateDepth)
		}
	}
	
	private fun disconnectFromNeighbors(world: WorldAccess, pos: BlockPos) {
		// update neighbors above
		val upPos = pos.offset(Direction.UP)
		disconnectFrom(world, upPos)
		
		// update neighbors below
		val downPos = pos.offset(Direction.DOWN)
		disconnectFrom(world, downPos)
		
		// update horizontal neighbors
		disconnectFrom(world, pos)
	}
	
	private fun disconnectFrom(world: WorldAccess, pos: BlockPos) {
		val directions = listOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)
		for (dir in directions) {
			val neighborPos = pos.offset(dir)
			val neighborState = world.getBlockState(neighborPos) // the block state we're fucking with
			if (neighborState.block !is PoweredComponent) continue // make sure our neighbor is one of us
			replace(neighborState, neighborState.with(EnumProperty.of(dir.opposite.name.lowercase(), WireConnection::class.java), WireConnection.NONE), world, neighborPos, 0)
		}
	}
	
	override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
		builder.add(
			WIRE_CONNECTION_NORTH,
			WIRE_CONNECTION_EAST,
			WIRE_CONNECTION_SOUTH,
			WIRE_CONNECTION_WEST,
			POWERED
		)
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
