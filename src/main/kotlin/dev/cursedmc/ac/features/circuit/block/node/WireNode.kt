package dev.cursedmc.ac.features.circuit.block.node

import com.kneelawk.graphlib.graph.BlockNode
import com.kneelawk.graphlib.graph.BlockNodeDecoder
import dev.cursedmc.ac.features.circuit.PowerManager
import dev.cursedmc.ac.features.circuit.block.component.PoweredComponent
import dev.cursedmc.ac.features.circuit.block.component.PoweredComponentBlock.Companion.POWERED
import dev.cursedmc.ac.features.circuit.block.node.filter.PowerCarrierFilter
import dev.cursedmc.ac.features.circuit.util.NetNode
import dev.cursedmc.ac.features.circuit.util.pos
import net.minecraft.nbt.NbtElement
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Direction.Axis
import net.minecraft.world.World

// immutable with no data means there only needs to be one instance, hence an object
object WireNode : PowerCarrierNode(PowerCarrierFilter) {
	override fun getTypeId(): Identifier = BlockNodes.WIRE_BLOCK_NODE
	
	override fun onChanged(world: ServerWorld, pos: BlockPos) {
		PowerManager.update(world, pos)
	}
	
	override fun setState(world: World, self: NetNode, power: Boolean) {
		world.setBlockState(self.pos, world.getBlockState(self.pos).with(POWERED, power))
	}
	
	override fun getState(world: World, self: NetNode): Boolean {
		return world.getBlockState(self.pos).get(POWERED)
	}
	
	override fun getSourceOutput(world: World, self: NetNode): Boolean {
		var powered = false
		k@for (k in -1..1) {
			for (dir in Direction.Type.HORIZONTAL.stream()) {
				val neighborPos = self.pos.offset(dir).offset(Axis.Y, k)
				val neighborState = world.getBlockState(neighborPos)
				if (!neighborState.contains(POWERED) || neighborState.block is PoweredComponent) continue
				
				val poweredProp = neighborState.get(POWERED)
				if (poweredProp) {
					powered = poweredProp
					break@k
				}
			}
		}
		
		return powered
	}
	
	object Decoder : BlockNodeDecoder {
		override fun createBlockNodeFromTag(tag: NbtElement?): BlockNode {
			return WireNode
		}
	}
}
