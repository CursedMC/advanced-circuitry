package dev.cursedmc.ac.features.circuit.block.node

import com.kneelawk.graphlib.graph.BlockGraph
import dev.cursedmc.ac.features.circuit.block.Blocks
import dev.cursedmc.ac.features.circuit.block.WireBlock.Companion.POWERED
import dev.cursedmc.ac.features.circuit.block.node.filter.PowerCarrierFilter
import dev.cursedmc.ac.features.circuit.util.NetNode
import dev.cursedmc.ac.util.warn
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Direction.Axis
import net.minecraft.world.World

class WireBlockNode : PowerCarrierNode(PowerCarrierFilter) {
	override fun getTypeId(): Identifier = Blocks.WIRE_BLOCK.first.lootTableId
	
	override fun updateState(world: ServerWorld, network: BlockGraph) {
		val power = network.nodes
			.limit(512)
			.reduce { node, node2 ->
				if ((node.data().node() as PowerCarrierNode).getInput(world, node2)) {
					return@reduce node
				} else return@reduce node2
			}.get().run {
				return@run (this.data().node() as PowerCarrierNode).getInput(world, this)
			}
	}
	
	override fun getPower(world: World, self: NetNode): Boolean {
		return try {
			world.getBlockState(self.data().pos()).get(POWERED)
		} catch (e: java.lang.IllegalArgumentException) {
			warn(e.message!!)
			false
		}
	}
	
	override fun setPower(world: World, self: NetNode, power: Boolean) {
		world.setBlockState(self.data().pos(), world.getBlockState(self.data().pos()).with(POWERED, power), 0)
	}
	
	override fun getInput(world: World, self: NetNode): Boolean {
		var powered = false
		k@for (k in -1..1) {
			for (dir in Direction.Type.HORIZONTAL.stream()) {
				val neighborPos = self.data().pos().offset(dir).offset(Axis.Y, k)
				val neighborState = world.getBlockState(neighborPos)
				if (!neighborState.contains(POWERED)) continue
				if (neighborState.get(POWERED)) {
					powered = neighborState.get(POWERED)
					break@k
				}
			}
		}
		
		return powered
	}
}
