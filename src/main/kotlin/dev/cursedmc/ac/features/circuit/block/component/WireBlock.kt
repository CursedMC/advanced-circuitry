package dev.cursedmc.ac.features.circuit.block.component

import com.kneelawk.graphlib.graph.BlockNode
import dev.cursedmc.ac.features.circuit.block.node.WireBlockNode
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings

@Suppress("OVERRIDE_DEPRECATION")
class WireBlock : PoweredComponentBlock(
	FabricBlockSettings
		.copyOf(net.minecraft.block.Blocks.IRON_BLOCK)
		.collidable(false)
		.luminance { if (it[POWERED]) 7 else 0 }
		.nonOpaque()
) {
	override fun createBlockNodes(): Collection<BlockNode> {
		return listOf(WireBlockNode)
	}
}
