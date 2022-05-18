package dev.cursedmc.ac.features.circuit.block.node.discoverer

import com.kneelawk.graphlib.GraphLib
import com.kneelawk.graphlib.graph.BlockNode
import com.kneelawk.graphlib.graph.BlockNodeDiscoverer
import dev.cursedmc.ac.features.Initializable
import dev.cursedmc.ac.goose.block.node.discoverer.ServerWorldWithNodes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

object ACBlockNodeDiscoverer : BlockNodeDiscoverer, Initializable {
	override fun getNodesInBlock(world: ServerWorld, pos: BlockPos): Collection<BlockNode> {
		val l = mutableListOf<BlockNode>()
		val node = getNode(world, pos)
		if (node != null) {
			l.add(node)
		}
		return l
	}
	
	private fun getNode(world: ServerWorld, pos: BlockPos): BlockNode? {
		val worldGoose = world as ServerWorldWithNodes
		return worldGoose.`adv_circ$getBlockNode`(pos)
	}
	
	/* no-op */
	override fun initialize() = Unit
	
	init {
		GraphLib.registerDiscoverer(this)
	}
}
