package dev.cursedmc.ac.features.circuit.block.component

import net.minecraft.block.HorizontalFacingBlock

abstract class GateBlock(settings: Settings) : HorizontalFacingBlock(settings), PoweredComponent {
	companion object {
		private val SHAPE = createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0)!!
	}
}
