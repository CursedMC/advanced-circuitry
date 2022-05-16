package dev.cursedmc.ac.exception

import dev.cursedmc.ac.MOD_ID

sealed class ModException(override val message: String) : RuntimeException("Advanced Circuitry ($MOD_ID) has encountered an error: $message")
