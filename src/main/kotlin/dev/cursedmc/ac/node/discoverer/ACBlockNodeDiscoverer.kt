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
		val list = try {
			val block = world.getBlockState(pos).block
			val container = block as BlockNodeProvider
			container.createBlockNodes()
		} catch (e: java.lang.ClassCastException) {
			dev.cursedmc.ac.util.error(e.message ?: "An unexpected error occurred.")
			null
		}
		return list ?: emptyList()
	}
	
	/* no-op */
	override fun initialize() = Unit
	
	init {
		GraphLib.registerDiscoverer(this)
	}
}
