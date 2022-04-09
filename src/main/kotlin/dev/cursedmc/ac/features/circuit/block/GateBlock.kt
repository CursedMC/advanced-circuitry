package dev.cursedmc.ac.features.circuit.block

import net.minecraft.block.HorizontalFacingBlock

abstract class GateBlock(settings: Settings) : HorizontalFacingBlock(settings) {
	companion object {
		protected val SHAPE = createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0)!!
	}
}
