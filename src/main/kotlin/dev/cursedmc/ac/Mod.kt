package dev.cursedmc.ac

import dev.cursedmc.ac.features.pcb.PCBFeature
import dev.cursedmc.ac.util.info
import dev.cursedmc.libreg.registry.ns

internal const val MOD_ID = "adv_circ"

fun init() {
	info("Advanced Circuitry started!")
	
	ns(MOD_ID)
	
	// Feature Initialization
	PCBFeature.initialize()
}
