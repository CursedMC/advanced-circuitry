package dev.cursedmc.ac.features.circuit.block.entity

import dev.cursedmc.ac.features.Initializable
import dev.cursedmc.ac.features.circuit.block.Blocks
import dev.cursedmc.libreg.registry.block.entity.blockEntityType

object BlockEntityType : Initializable {
	val WIRE_BLOCK = blockEntityType(
		"wire_block",
		::WireBlockEntity,
		Blocks.WIRE_BLOCK.first
	)
}
