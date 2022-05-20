package dev.cursedmc.ac.node.discoverer

import com.kneelawk.graphlib.GraphLib
import com.kneelawk.graphlib.graph.BlockNode
import com.kneelawk.graphlib.graph.BlockNodeDiscoverer
import dev.cursedmc.ac.features.Initializable
import dev.cursedmc.ac.node.container.BlockNodeProvider
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

object ACBlockNodeDiscoverer : BlockNodeDiscoverer, Initializable {
	override fun getNodesInBlock(world: ServerWorld, pos: BlockPos): Collection<BlockNode> {
		return (world.getBlockState(pos).block as? BlockNodeProvider)?.createBlockNodes() ?: emptyList()
	}
	
	override fun initialize() {
		GraphLib.registerDiscoverer(this)
	}
}
