package dev.cursedmc.ac.features.circuit.block.component

interface Gate {
	fun condition(input: Array<Boolean>): Array<Boolean>
}
