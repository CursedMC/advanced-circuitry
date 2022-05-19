package dev.cursedmc.ac.features.circuit.block.node

import com.kneelawk.graphlib.GraphLib
import dev.cursedmc.ac.features.Initializable
import dev.cursedmc.ac.features.circuit.block.Blocks
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object BlockNodes : Initializable {
	val WIRE_BLOCK_NODE: Identifier = Blocks.WIRE_BLOCK.first.lootTableId
	
	override fun initialize() {
		Registry.register(GraphLib.BLOCK_NODE_DECODER, WIRE_BLOCK_NODE, WireBlockNode.Decoder)
	}
}