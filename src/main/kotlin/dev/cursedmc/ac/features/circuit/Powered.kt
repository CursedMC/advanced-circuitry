package dev.cursedmc.ac.features.circuit

import net.minecraft.util.StringIdentifiable

enum class Powered(name: String) : StringIdentifiable {
	LEFT("left"),
	RIGHT("right"),
	BOTH("both"),
	NONE("none"),
	;
	
	override fun asString(): String {
		return name
	}
}
