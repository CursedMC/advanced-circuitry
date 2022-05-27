package dev.cursedmc.ac.features.circuit

import com.kneelawk.graphlib.GraphLib
import dev.cursedmc.ac.features.Initializable
import dev.cursedmc.ac.features.circuit.block.component.PoweredComponent
import dev.cursedmc.ac.features.circuit.block.node.GateNode
import dev.cursedmc.ac.features.circuit.block.node.PowerCarrierNode
import dev.cursedmc.ac.features.circuit.block.node.WireNode
import dev.cursedmc.ac.features.circuit.util.node
import dev.cursedmc.ac.features.circuit.util.pos
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

object PowerManager : Initializable {
    fun update(world: ServerWorld, pos: BlockPos) {
        GraphLib.getController(world).getGraphsInPos(pos).forEach { updateGraph(world, it) }
    }
	
	private fun updateGraph(world: ServerWorld, id: Long) {
		val controller = GraphLib.getController(world)
		
		// Sometimes graphs that were registered for an update will have been removed by the time this has been called,
		// like if the graph was merged with another graph, but that would mean the other graph would also be scheduled
		// for an update as well, so it would be ok.
		val network = controller.getGraph(id) ?: return
		
		val power = network.nodes
			.anyMatch {
				// loop through all power sources and return as soon as we find one that's powered
				val node = it.node
				
				if (world.getBlockState(it.pos).block !is PoweredComponent) return@anyMatch false
				if (it.node is GateNode.GateOutput) return@anyMatch false // don't update gate outputs
				
				(node is WireNode && node.getSourceOutput(world, it)) || (node is GateNode.GateOutput && node is GateNode && node.getState(world, it))
			}
		network.nodes
			.forEach {
				// ensure we're only updating components
				if (world.getBlockState(it.pos).block !is PoweredComponent) return@forEach
				if (it.node is GateNode.GateOutput) return@forEach // don't update gate outputs
				
				(it.node as? PowerCarrierNode)?.setState(world, it, power)
			}
	}
}
