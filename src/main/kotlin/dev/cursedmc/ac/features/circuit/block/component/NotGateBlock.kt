package dev.cursedmc.ac.features.circuit.block.component

import com.kneelawk.graphlib.graph.BlockNode
import dev.cursedmc.ac.features.circuit.block.Blocks
import dev.cursedmc.ac.features.circuit.block.node.NotGateNode
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.HorizontalFacingBlock
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

class NotGateBlock : GateBlock(
	FabricBlockSettings
		.copyOf(Blocks.WIRE_BLOCK.first)
		.collidable(true)
) {
	override fun createBlockNodes(world: ServerWorld, pos: BlockPos): Collection<BlockNode> {
		val inputDir = world.getBlockState(pos).get(HorizontalFacingBlock.FACING).opposite
		return listOf(NotGateNode.Output(inputDir), NotGateNode.Input(inputDir))
	}
	
	override fun condition(input: Array<Boolean>): Array<Boolean> {
		return arrayOf(!input[0])
	}
}
