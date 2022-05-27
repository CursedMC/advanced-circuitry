package dev.cursedmc.ac.features.circuit.block.node

import com.kneelawk.graphlib.graph.BlockNode
import dev.cursedmc.ac.features.circuit.PowerManager
import dev.cursedmc.ac.features.circuit.block.component.PoweredComponent
import dev.cursedmc.ac.features.circuit.block.component.PoweredComponentBlock.Companion.POWERED
import dev.cursedmc.ac.features.circuit.block.node.filter.PowerCarrierFilter
import dev.cursedmc.ac.features.circuit.util.NetNode
import dev.cursedmc.ac.features.circuit.util.pos
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

abstract class GateNode(private val inputType: InputType, private val side: Direction) : PowerCarrierNode(PowerCarrierFilter) {
	override fun setState(world: World, self: NetNode, power: Boolean) {
		world.setBlockState(self.pos, world.getBlockState(self.pos).with(POWERED, power))
	}
	
	override fun getState(world: World, self: NetNode): Boolean {
		return world.getBlockState(self.pos).get(POWERED)
	}
	
	override fun getSourceOutput(world: World, self: NetNode): Boolean {
		var powered = false
		k@for (k in -1..1) {
			val neighborPos = self.pos.offset(side).offset(Direction.Axis.Y, k)
			val neighborState = world.getBlockState(neighborPos)
			if (!neighborState.contains(POWERED) || neighborState.block is PoweredComponent) continue
			
			val poweredProp = neighborState.get(POWERED)
			if (poweredProp) {
				powered = poweredProp
				break@k
			}
		}
		return powered
	}
	
	override fun toTag(): NbtElement? {
		val tag = NbtCompound()
		tag.putBoolean("isInput", this is NotGateNode.Input)
		tag.putInt("side", side.id)
		return tag
	}
	
	override fun onChanged(world: ServerWorld, pos: BlockPos) {
		PowerManager.update(world, pos)
	}
	
	sealed class Decoder : PowerCarrierNode.Decoder() {
		override fun createBlockNodeFromTag(tag: NbtCompound): BlockNode {
			val side = Direction.byId(tag.getInt("side"))
			return if (tag.getBoolean("isInput")) {
				NotGateNode.Input(side)
			} else {
				NotGateNode.Output(side)
			}
		}
	}
	
	interface GateInput
	
	interface GateOutput
}
