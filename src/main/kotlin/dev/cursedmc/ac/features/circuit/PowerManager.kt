package dev.cursedmc.ac.features.circuit

import com.kneelawk.graphlib.GraphLib
import dev.cursedmc.ac.features.Initializable
import dev.cursedmc.ac.features.circuit.block.node.PowerCarrierNode
import dev.cursedmc.ac.features.circuit.util.node
import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet
import it.unimi.dsi.fastutil.longs.LongSet
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.RegistryKey
import net.minecraft.world.World

object PowerManager : Initializable {
	private val graphsToUpdate = mutableMapOf<RegistryKey<World>, LongSet>()
	
	override fun initialize() {
		ServerTickEvents.END_WORLD_TICK.register(::doAllUpdates)
	}
	
	fun scheduleUpdate(world: ServerWorld, pos: BlockPos) {
		val setForWorld = graphsToUpdate.computeIfAbsent(world.registryKey) { LongLinkedOpenHashSet() }
		GraphLib.getController(world).getGraphsInPos(pos).forEach(setForWorld::add)
	}
	
	private fun doAllUpdates(world: ServerWorld) {
		graphsToUpdate[world.registryKey]?.let { setForWorld ->
			for (id in setForWorld.iterator()) {
				updateGraph(world, id)
			}
			setForWorld.clear()
		}
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
//            .limit(512)
//            .reduce { node, node2 ->
//                // get all power sources on the network and say we're powered if one of them is.
//                if ((node.node as PowerCarrierNode).getInput(world, node2)) {
//                    return@reduce node
//                } else return@reduce node2
//            }.orElseGet {
//                return@orElseGet network.nodes.findAny().get()
//            }.run {
//                return@run (this.node as PowerCarrierNode).getInput(world, this)
//            }
		network.nodes
			.forEach {
				(it.node as? PowerCarrierNode)?.setPower(world, it, power)
			}
	}
}