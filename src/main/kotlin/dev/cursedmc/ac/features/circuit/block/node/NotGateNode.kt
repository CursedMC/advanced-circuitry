package dev.cursedmc.ac.features.circuit.block.node

import dev.cursedmc.ac.features.circuit.PowerManager
import dev.cursedmc.ac.features.circuit.util.NetNode
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

sealed class NotGateNode(side: Direction) : GateNode(InputType.STRAIGHT, side) {
	override fun getTypeId(): Identifier = BlockNodes.NOT_GATE_NODE
	
	override fun onChanged(world: ServerWorld, pos: BlockPos) {
		PowerManager.update(world, pos)
	}
	
	data class Input(private val side: Direction) : NotGateNode(side), GateInput {
		override fun setState(world: World, self: NetNode, power: Boolean) {
			super.setState(world, self, !power)
		}
		
		override fun getState(world: World, self: NetNode): Boolean {
			return !super.getState(world, self)
		}
	}
	data class Output(private val side: Direction) : NotGateNode(side), GateOutput
	
	object Decoder : GateNode.Decoder()
}
