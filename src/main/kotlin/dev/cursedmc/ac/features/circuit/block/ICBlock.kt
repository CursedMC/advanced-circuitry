package dev.cursedmc.ac.features.circuit.block

import dev.cursedmc.ac.util.block.facing.HorizontalFacingBlock
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView

@Suppress("OVERRIDE_DEPRECATION")
class ICBlock : Block(
	FabricBlockSettings
		.copyOf(net.minecraft.block.Blocks.STONE)
), HorizontalFacingBlock {
	init {
		this.defaultState = this.stateManager.defaultState.with(Properties.HORIZONTAL_FACING, Direction.NORTH)
	}
	
	override fun getPlacementState(ctx: ItemPlacementContext?): BlockState? {
		return this.defaultState.with(Properties.HORIZONTAL_FACING, ctx?.playerFacing)
	}
	
	override fun appendProperties(builder: StateManager.Builder<Block, BlockState>?) {
		builder?.add(Properties.HORIZONTAL_FACING)
	}
	
	override fun getOutlineShape(
		state: BlockState?,
		world: BlockView?,
		pos: BlockPos?,
		context: ShapeContext?,
	): VoxelShape {
		return when (state?.get(Properties.HORIZONTAL_FACING)) {
			Direction.NORTH -> VoxelShapes.cuboid(0.1875, 0.0, 0.0, 0.8125, 0.25, 1.0)
			Direction.SOUTH -> VoxelShapes.cuboid(0.1875, 0.0, 0.0, 0.8125, 0.25, 1.0)
			Direction.WEST -> VoxelShapes.cuboid(0.0, 0.0, 0.1875, 1.0, 0.25, 0.8125)
			Direction.EAST -> VoxelShapes.cuboid(0.0, 0.0, 0.1875, 1.0, 0.25, 0.8125)
			else -> VoxelShapes.fullCube() // there is no whey this would be happened
		}
	}
	
	override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
		return super<HorizontalFacingBlock>.rotate(state, rotation)
	}
	
	override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
		return super<HorizontalFacingBlock>.mirror(state, mirror)
	}
}
