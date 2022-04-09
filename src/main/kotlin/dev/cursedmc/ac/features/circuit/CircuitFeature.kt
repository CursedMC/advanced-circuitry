package dev.cursedmc.ac.features.circuit

import dev.cursedmc.ac.features.Feature
import dev.cursedmc.ac.features.circuit.block.Blocks
import dev.cursedmc.ac.features.circuit.block.entity.BlockEntityType
import dev.cursedmc.libreg.registry.ident
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack

object CircuitFeature : Feature<Items, Blocks, BlockEntityType> {
	private val GROUP = FabricItemGroupBuilder
		.create(ident("circuit_group"))
		.icon { return@icon ItemStack(blocks().IC.second) }
		.build()
	
	override fun name(): String {
		return "Circuit Feature"
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
	
	override fun blockEntityType(): BlockEntityType {
		return BlockEntityType
	}
}
