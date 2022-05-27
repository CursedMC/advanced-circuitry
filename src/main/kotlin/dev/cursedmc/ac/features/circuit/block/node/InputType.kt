package dev.cursedmc.ac.features.circuit.block.node

import net.minecraft.util.math.Direction

enum class InputType {
	LEFT,
	RIGHT,
	STRAIGHT,
	;
	
	/**
	 * Gets the cardinal direction to the right, left, or "none" of the specified cardinal direction.
	 */
	fun ofCardinal(cardinal: Direction): Direction {
		return when (this) {
			RIGHT -> cardinal.rotateYClockwise()
			LEFT -> cardinal.rotateYCounterclockwise()
			STRAIGHT -> cardinal
		}
	}
}
