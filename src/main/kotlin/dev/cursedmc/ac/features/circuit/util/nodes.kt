package dev.cursedmc.ac.features.circuit.util

import com.kneelawk.graphlib.graph.BlockNode
import com.kneelawk.graphlib.graph.BlockNodeWrapper
import com.kneelawk.graphlib.graph.struct.Node

typealias NetNode = Node<BlockNodeWrapper<out BlockNode>>
