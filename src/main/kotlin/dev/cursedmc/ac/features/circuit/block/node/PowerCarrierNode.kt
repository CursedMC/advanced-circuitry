package dev.cursedmc.ac.features.circuit.block.node

import com.kneelawk.graphlib.GraphLib
import com.kneelawk.graphlib.graph.BlockGraph
import com.kneelawk.graphlib.graph.BlockNodeWrapper
import com.kneelawk.graphlib.graph.NodeView
import com.kneelawk.graphlib.graph.struct.Node
import com.kneelawk.graphlib.wire.FullWireBlockNode
import com.kneelawk.graphlib.wire.FullWireConnectionFilter
import com.kneelawk.graphlib.wire.WireConnectionDiscoverers
import dev.cursedmc.ac.features.circuit.util.NetNode
import dev.cursedmc.ac.util.info
import net.minecraft.nbt.NbtElement
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class PowerCarrierNode(private val filter: FullWireConnectionFilter) : FullWireBlockNode {
	override fun toTag(): NbtElement? {
		return null
	}
	
	override fun findConnections(
		world: ServerWorld,
		nodeView: NodeView,
		pos: BlockPos
	): MutableCollection<Node<BlockNodeWrapper<*>>> {
		return WireConnectionDiscoverers.fullBlockFindConnections(this, world, nodeView, pos, filter)
	}
	
	override fun canConnect(
		world: ServerWorld,
		nodeView: NodeView,
		pos: BlockPos,
		other: Node<BlockNodeWrapper<*>>
	): Boolean {
		return WireConnectionDiscoverers.fullBlockCanConnect(this, world, pos, filter, other)
	}
	
	override fun onChanged(world: ServerWorld, pos: BlockPos) {
		val controller = GraphLib.getController(world)
		controller.getGraphsInPos(pos)
			.forEach {
				info("test")
				updateState(world, controller.getGraph(it)!!)
			}
	}
	
	abstract fun updateState(world: ServerWorld, network: BlockGraph)
	
	abstract fun getPower(world: World, self: NetNode): Boolean
	
	abstract fun setPower(world: World, self: NetNode, power: Boolean)
	
	/**
	 * Gets the nearest power source.
	 */
	abstract fun getInput(world: World, self: NetNode): Boolean
}
