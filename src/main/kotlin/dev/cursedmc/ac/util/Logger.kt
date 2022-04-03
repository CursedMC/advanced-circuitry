@file:Suppress("unused")

package dev.cursedmc.ac.util

import dev.cursedmc.ac.MOD_ID
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(MOD_ID)!!

internal fun info(msg: String) = logger.info(msg)

internal fun warn(msg: String) = logger.warn(msg)

internal fun error(msg: String) = logger.error(msg)

internal fun fatal(msg: String) = logger.fatal(msg)

internal fun debug(msg: String) = logger.debug(msg)
