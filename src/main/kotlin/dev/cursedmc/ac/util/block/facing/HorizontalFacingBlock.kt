package dev.cursedmc.ac.util.block.facing

import net.minecraft.block.BlockState
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation

interface HorizontalFacingBlock {
	fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
		return state.with(
			Properties.HORIZONTAL_FACING,
			rotation.rotate(state.get(Properties.HORIZONTAL_FACING))
		) as BlockState
	}
	
	fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
		return state.rotate(mirror.getRotation(state.get(Properties.HORIZONTAL_FACING)))
	}
}