package dev.cursedmc.ac.features.circuit.block.node.filter

import com.kneelawk.graphlib.graph.BlockNode
import com.kneelawk.graphlib.wire.WireConnectionFilter
import dev.cursedmc.ac.features.circuit.block.node.GateNode
import dev.cursedmc.ac.features.circuit.block.node.PowerCarrierNode

object GateFilter : WireConnectionFilter {
	override fun accepts(self: BlockNode, other: BlockNode): Boolean {
		return self is GateNode && other is PowerCarrierNode
	}
}
