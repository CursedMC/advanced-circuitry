package dev.cursedmc.ac.features.circuit.block.component

import com.kneelawk.graphlib.graph.BlockNode
import dev.cursedmc.ac.features.circuit.block.node.WireNode
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

@Suppress("OVERRIDE_DEPRECATION")
class WireBlock : PoweredComponentBlock(
	FabricBlockSettings
		.copyOf(net.minecraft.block.Blocks.IRON_BLOCK)
		.collidable(false)
		.luminance { if (it[POWERED]) 7 else 0 }
		.nonOpaque()
) {
	override fun createBlockNodes(world: ServerWorld, pos: BlockPos): Collection<BlockNode> {
		return listOf(WireNode)
	}
}
