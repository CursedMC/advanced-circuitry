package dev.cursedmc.ac.features.circuit.block.node

import com.kneelawk.graphlib.graph.BlockNodeWrapper
import com.kneelawk.graphlib.graph.NodeView
import com.kneelawk.graphlib.graph.struct.Node
import com.kneelawk.graphlib.wire.FullWireBlockNode
import dev.cursedmc.ac.features.circuit.util.NetNode
import dev.cursedmc.ac.features.circuit.util.node
import dev.cursedmc.ac.features.circuit.util.pos
import net.minecraft.nbt.NbtElement
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Direction.Axis
import net.minecraft.world.World

abstract class PowerCarrierNode : FullWireBlockNode {
	override fun toTag(): NbtElement? {
		return null
	}
	
	override fun findConnections(
		world: ServerWorld,
		nodeView: NodeView,
		pos: BlockPos
	): MutableCollection<NetNode> {
		val collector = ArrayList<NetNode>()
		
		for (k in -1..1) {
			for (dir in Direction.Type.HORIZONTAL.stream()) {
				val neighborPos = pos.offset(dir).offset(Axis.Y, k)
				nodeView.getNodesAt(neighborPos)
					.forEach {
						if (it.node !is PowerCarrierNode) return@forEach
						collector.add(it)
					}
			}
		}
		
		return collector
	}
	
	override fun canConnect(
		world: ServerWorld,
		nodeView: NodeView,
		pos: BlockPos,
		other: Node<BlockNodeWrapper<*>>
	): Boolean {
		return other.pos.isWithinDistance(pos, 1.4142135623730951)
	}
	
	abstract fun getPower(world: World, self: NetNode): Boolean
	
	abstract fun setPower(world: World, self: NetNode, power: Boolean)
	
	/**
	 * Gets the nearest power source.
	 */
	abstract fun getInput(world: World, self: NetNode): Boolean
}
