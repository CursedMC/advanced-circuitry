package dev.cursedmc.ac.features.circuit.util

import com.kneelawk.graphlib.graph.BlockNode
import com.kneelawk.graphlib.graph.BlockNodeWrapper
import com.kneelawk.graphlib.graph.struct.Node
import net.minecraft.util.math.BlockPos

typealias NetNode = Node<BlockNodeWrapper<out BlockNode>>

val NetNode.pos: BlockPos
    get() = data().pos()

val NetNode.node: BlockNode
    get() = data().node()
