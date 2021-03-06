package dev.cursedmc.ac.features.circuit.block

import dev.cursedmc.ac.features.Initializable
import dev.cursedmc.ac.features.circuit.CircuitFeature
import dev.cursedmc.ac.features.circuit.block.component.NotGateBlock
import dev.cursedmc.ac.features.circuit.block.component.WireBlock
import dev.cursedmc.ac.features.circuit.block.node.BlockNodes
import dev.cursedmc.libreg.registry.block.blockItem
import net.fabricmc.fabric.api.item.v1.FabricItemSettings

object Blocks : Initializable {
	val IC = blockItem(
		"ic",
		ICBlock(),
		FabricItemSettings()
			.group(CircuitFeature.group())
	)
	
	val WIRE_BLOCK = blockItem(
		"wire",
		WireBlock(),
		FabricItemSettings()
			.group(CircuitFeature.group())
	)
	
	val NOT_GATE = blockItem(
		"gate_not",
		NotGateBlock(),
		FabricItemSettings()
			.group(CircuitFeature.group())
	)

	override fun initialize() {
		BlockNodes.initialize()
	}
}
