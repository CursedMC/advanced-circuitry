package dev.cursedmc.ac.features

/**
 * Adds a no-op initialize function that automatically calls <code>&lt;clinit&gt;</code>.
 */
interface Initializable {
	/* no-op */
	fun initialize() = Unit
}
