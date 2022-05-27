package dev.cursedmc.ac.features.circuit.block.component

/**
 * A logic gate with two inputs and one output.
 */
interface DualMonoGate : Gate {
	fun condition(first: Boolean, second: Boolean): Boolean
	
	override fun condition(input: Array<Boolean>): Array<Boolean> {
		return arrayOf(this.condition(input[0], input[1]))
	}
}
