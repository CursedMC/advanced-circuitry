package dev.cursedmc.ac.features.circuit

import net.minecraft.util.StringIdentifiable

enum class GatePowerState : StringIdentifiable {
	LEFT,
	RIGHT,
	BOTH,
	NONE,
	;
	
	override fun asString(): String {
		return name
	}
}
