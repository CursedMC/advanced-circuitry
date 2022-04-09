package dev.cursedmc.ac.features.circuit.property

import dev.cursedmc.ac.features.circuit.Powered
import net.minecraft.state.property.EnumProperty

class PoweredProperty(name: String) : EnumProperty<Powered>(name, Powered::class.java, Powered.values().asList()) {
	companion object {
		val LEFT = of("left", Powered::class.java)!!
		val RIGHT = of("right", Powered::class.java)!!
		val BOTH = of("both", Powered::class.java)!!
		val NONE = of("none", Powered::class.java)!!
	}
}
