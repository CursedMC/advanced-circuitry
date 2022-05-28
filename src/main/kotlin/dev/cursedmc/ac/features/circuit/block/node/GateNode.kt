package dev.cursedmc.ac.features.circuit.block.node

import com.kneelawk.graphlib.graph.BlockNode
import com.kneelawk.graphlib.graph.NodeView
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
	
	override fun findConnections(world: ServerWorld, nodeView: NodeView, pos: BlockPos): MutableCollection<NetNode> {
		val list = super.findConnections(world, nodeView, pos)
		list.retainAll { isCorrectDirection(pos, it) } // only keep valid outputs
		return list
	}
	
	private fun isCorrectDirection(myPos: BlockPos, other: NetNode): Boolean {
		val diff = other.pos.subtract(myPos)
		val dir = Direction.fromVector(BlockPos(diff.x, 0, diff.z))
		return dir == inputType.ofCardinal(inputType.ofCardinal(side).opposite)
	}
	
	override fun canConnect(
		world: ServerWorld, nodeView: NodeView, pos: BlockPos, other: NetNode
	): Boolean {
		return super.canConnect(world, nodeView, pos, other) && isCorrectDirection(pos, other)
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
	
	enum class InputType {
		LEFT,
		RIGHT,
		STRAIGHT,
		;
		
		/**
		 * Gets the cardinal direction to the right, left, or "none" of the specified cardinal direction.
		 */
		fun ofCardinal(cardinal: Direction): Direction {
			return when (this) {
				RIGHT -> cardinal.rotateYClockwise()
				LEFT -> cardinal.rotateYCounterclockwise()
				STRAIGHT -> cardinal
			}
		}
	}
	
	interface GateInput
	
	interface GateOutput
}
