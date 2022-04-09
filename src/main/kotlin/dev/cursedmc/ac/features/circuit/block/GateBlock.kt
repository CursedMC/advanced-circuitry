package dev.cursedmc.ac.features.circuit.block

import dev.cursedmc.ac.features.circuit.property.PoweredProperty
import net.minecraft.block.HorizontalFacingBlock
import net.minecraft.util.shape.VoxelShapes

abstract class GateBlock(settings: Settings) : HorizontalFacingBlock(settings) {
	val powered = PoweredProperty.NONE
	
	companion object {
		protected val SHAPE = VoxelShapes.cuboid(0.0, 0.0, 0.0, 16.0, 2.0, 16.0)!!
	}
}
