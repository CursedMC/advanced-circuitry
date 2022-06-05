package dev.cursedmc.ac.features.circuit.block.node

import com.kneelawk.graphlib.GraphLib
import dev.cursedmc.ac.features.circuit.PowerManager
import dev.cursedmc.ac.features.circuit.block.component.PoweredComponentBlock
import dev.cursedmc.ac.features.circuit.util.NetNode
import dev.cursedmc.ac.features.circuit.util.node
import dev.cursedmc.ac.features.circuit.util.pos
import dev.cursedmc.ac.util.warn
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import kotlin.streams.asSequence

sealed class NotGateNode(side: Direction) : GateNode(InputType.STRAIGHT, side) {
	override fun getTypeId(): Identifier = BlockNodes.NOT_GATE_NODE
	
	data class Input(private val side: Direction) : NotGateNode(side), GateInput {
		override fun setState(world: World, self: NetNode, power: Boolean) {
			val blockState = world.getBlockState(self.pos)
			world.setBlockState(self.pos, blockState.with(PoweredComponentBlock.POWERED, !power))
			
			if (blockState[PoweredComponentBlock.POWERED] == power) {
				// this means we've had a change in power because those two shouldn't be equal otherwise
				if (world is ServerWorld) {
					GraphLib.getController(world).getNodesAt(self.pos).asSequence().first { it.node is Output }?.let { node -> 
						PowerManager.updateGraph(world, node.data().graphId)
					}
				}
			}
		}
	}
	
	data class Output(private val side: Direction) : NotGateNode(side), GateOutput {
		override fun setState(world: World, self: NetNode, power: Boolean) {
			// do nothing here
		}
		
		override fun getSourceOutput(world: World, self: NetNode): Boolean {
			// the output node actually outputs whether this node is powered
			val blockState = world.getBlockState(self.pos)
			
			if (!blockState.contains(PoweredComponentBlock.POWERED)) {
				warn("Not-Gate Output node in a block that is not a Not-Gate! Pos: ${self.pos}")
				return false
			}
			
			return blockState[PoweredComponentBlock.POWERED]
		}
	}
	
	object Decoder : GateNode.Decoder()
}
