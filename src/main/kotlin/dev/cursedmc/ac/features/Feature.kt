package dev.cursedmc.ac.features

import net.minecraft.item.ItemGroup

/**
 * A class containing information about a mod feature.
 * A mod feature is a group of items and/or blocks that compose an ItemGroup.
 * <br><br><br>
 * Each item in this ItemGroup is intended to work with each other in some way.
 * However, not every item has to interact with every single other item; just one.
 * @param I Class with items
 * @param B Class with blocks
 */
interface Feature<I : Initializable, B : Initializable> : Initializable {
	fun name(): String
	fun group(): ItemGroup
	fun items(): I
	fun blocks(): B
	override fun initialize() {
		items().initialize()
		blocks().initialize()
	}
}
