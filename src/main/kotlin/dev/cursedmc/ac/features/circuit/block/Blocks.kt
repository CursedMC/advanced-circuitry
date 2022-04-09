package dev.cursedmc.ac.features.circuit.block

import dev.cursedmc.ac.features.Initializable
import dev.cursedmc.ac.features.pcb.PCBFeature
import dev.cursedmc.libreg.registry.block.blockItem
import net.fabricmc.fabric.api.item.v1.FabricItemSettings

object Blocks : Initializable {
	val IC = blockItem(
		"ic",
		ICBlock(),
		FabricItemSettings()
			.group(PCBFeature.group())
	)
}
