package dev.cursedmc.ac.features.circuit.property

import dev.cursedmc.ac.features.circuit.GatePowerState
import net.minecraft.state.property.EnumProperty

class GatePowerProperty(name: String) : EnumProperty<GatePowerState>(name, GatePowerState::class.java, GatePowerState.values().asList()) {
	companion object {
		val LEFT = of("left", GatePowerState::class.java)!!
		val RIGHT = of("right", GatePowerState::class.java)!!
		val BOTH = of("both", GatePowerState::class.java)!!
		val NONE = of("none", GatePowerState::class.java)!!
	}
}
