package dev.cursedmc.ac.features.pcb.block

import dev.cursedmc.ac.features.pcb.PCBFeature
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView

class PCBBlock : BlockWithEntity(
	FabricBlockSettings
		.copyOf(Blocks.IRON_BLOCK)
) {
	init {
		this.defaultState = this.stateManager.defaultState.with(Companion.WITH_IC, false)
	}
	
	override fun appendProperties(builder: StateManager.Builder<Block, BlockState>?) {
		builder?.add(WITH_IC)
	}
	
	override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity? {
		return PCBFeature.blockEntityType().PCB.instantiate(pos, state)
	}
	
	@Suppress("OVERRIDE_DEPRECATION")
	override fun getOutlineShape(
		state: BlockState?,
		world: BlockView?,
		pos: BlockPos?,
		context: ShapeContext?,
	): VoxelShape {
		return VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0)
	}
	
	companion object {
		private val WITH_IC = BooleanProperty.of("with_ic")!!
	}
}
