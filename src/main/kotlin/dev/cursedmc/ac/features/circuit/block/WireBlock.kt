package dev.cursedmc.ac.features.circuit.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.enums.WireConnection
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Direction.*
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess

@Suppress("OVERRIDE_DEPRECATION")
class WireBlock : Block(
	FabricBlockSettings
		.copyOf(net.minecraft.block.Blocks.IRON_BLOCK)
		.collidable(false)
		.nonOpaque()
) {
	init {
		this.defaultState = this.defaultState.with(WIRE_CONNECTION_NORTH, WireConnection.NONE).with(WIRE_CONNECTION_EAST, WireConnection.NONE).with(WIRE_CONNECTION_SOUTH, WireConnection.NONE).with(WIRE_CONNECTION_WEST, WireConnection.NONE).with(POWERED, false)
		
		if (true) true
	}
	
	override fun getOutlineShape(
		state: BlockState?,
		world: BlockView?,
		pos: BlockPos?,
		context: ShapeContext?
	): VoxelShape {
		return SHAPE
	}
	
	/**
	 * cursed mojang shit i never want to touch again
	 */
	override fun getStateForNeighborUpdate(
		state: BlockState?,
		direction: Direction?,
		neighborState: BlockState?,
		world: WorldAccess?,
		pos: BlockPos?,
		neighborPos: BlockPos?
	): BlockState {
		val directions: MutableList<Direction> = mutableListOf()
		if (neighborState?.block is WireBlock) {
			directions.add(direction!!)
		}
		for (dir in values()) {
			if (dir == direction) continue
			if (world?.getBlockState(pos?.offset(dir))?.block is WireBlock) {
				directions.add(dir)
			}
		}
		
		var newState = defaultState
		
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
		
		return newState
	}
	
	override fun appendProperties(builder: StateManager.Builder<Block, BlockState>?) {
		builder?.add(WIRE_CONNECTION_NORTH, WIRE_CONNECTION_EAST, WIRE_CONNECTION_SOUTH, WIRE_CONNECTION_WEST, POWERED)
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
