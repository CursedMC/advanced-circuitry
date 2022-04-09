package dev.cursedmc.ac.features.pcb.block.entity

import dev.cursedmc.ac.features.Initializable
import dev.cursedmc.ac.features.pcb.PCBFeature
import dev.cursedmc.libreg.registry.block.entity.blockEntityType
import net.minecraft.block.BlockWithEntity

object BlockEntityType : Initializable {
	val PCB = blockEntityType(
		"pcb",
		::PCBBlockEntity,
		PCBFeature.blocks().PCB.first as BlockWithEntity,
	)
}
