package dev.cursedmc.ac.features.pcb

import dev.cursedmc.ac.features.Feature
import dev.cursedmc.libreg.registry.ident
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack

object PCBFeature : Feature<Items, Blocks> {
	private val GROUP = FabricItemGroupBuilder
		.create(ident("pcb_group"))
		.icon {
			val i = ItemStack(blocks().PCB.second)
			i.count = 2
			return@icon i
		}
		.build()
	
	override fun name(): String {
		return "PCB Feature"
	}
	
	override fun group(): ItemGroup {
		return GROUP
	}
	
	override fun items(): Items {
		return Items
	}
	
	override fun blocks(): Blocks {
		return Blocks
	}
}
