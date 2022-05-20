package dev.cursedmc.ac.features.circuit

import com.kneelawk.graphlib.GraphLib
import dev.cursedmc.ac.features.Initializable
import dev.cursedmc.ac.features.circuit.block.Blocks
import dev.cursedmc.ac.features.circuit.block.node.PowerCarrierNode
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
				node is PowerCarrierNode && node.getInput(world, it)
			}
		network.nodes
			.forEach {
				// ensure we're only updating wires
				if (!world.getBlockState(it.pos).isOf(Blocks.WIRE_BLOCK.first)) return@forEach
				
				(it.node as? PowerCarrierNode)?.setPower(world, it, power)
			}
	}
}
