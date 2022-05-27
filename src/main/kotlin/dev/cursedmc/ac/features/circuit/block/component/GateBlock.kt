package dev.cursedmc.ac.features.circuit.block.component

import dev.cursedmc.ac.util.block.facing.HorizontalFacingBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.Direction

@Suppress("OVERRIDE_DEPRECATION")
abstract class GateBlock(settings: Settings) : PoweredComponentBlock(settings), PoweredComponent, HorizontalFacingBlock, Gate {
	init {
		this.defaultState = this.defaultState.with(net.minecraft.block.HorizontalFacingBlock.FACING, Direction.NORTH)
	}
	
	override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
		super.appendProperties(builder)
		
		builder.add(net.minecraft.block.HorizontalFacingBlock.FACING)
	}
	
	override fun getPlacementState(ctx: ItemPlacementContext?): BlockState? {
		return this.defaultState.with(net.minecraft.block.HorizontalFacingBlock.FACING, ctx?.playerFacing)
	}
	
	override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
		return super<HorizontalFacingBlock>.rotate(state, rotation)
	}
	
	override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
		return super<HorizontalFacingBlock>.mirror(state, mirror)
	}
}
