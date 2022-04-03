@file:Suppress("unused")

package dev.cursedmc.ac.features.pcb

import dev.cursedmc.ac.features.Initializable
import dev.cursedmc.libreg.registry.block.blockItem
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.HorizontalFacingBlock
import net.minecraft.block.ShapeContext
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction.*
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView

object Blocks : Initializable {
	val IC = blockItem(
		"ic",
		object : HorizontalFacingBlock(
			FabricBlockSettings
				.copyOf(net.minecraft.block.Blocks.STONE)
		) {
			init {
				this.defaultState = this.stateManager.defaultState.with(Properties.HORIZONTAL_FACING, NORTH)
			}
			
			override fun getPlacementState(ctx: ItemPlacementContext?): BlockState? {
				return this.defaultState.with(Properties.HORIZONTAL_FACING, ctx?.playerFacing)
			}
			
			override fun appendProperties(builder: StateManager.Builder<Block, BlockState>?) {
				builder?.add(Properties.HORIZONTAL_FACING)
			}
			
			override fun getOutlineShape(
				state: BlockState?,
				world: BlockView?,
				pos: BlockPos?,
				context: ShapeContext?,
			): VoxelShape {
				return when (state?.get(Properties.HORIZONTAL_FACING)) {
					NORTH -> VoxelShapes.cuboid(0.1875, 0.0, 0.0, 0.8125, 0.25, 1.0)
					SOUTH -> VoxelShapes.cuboid(0.1875, 0.0, 0.0, 0.8125, 0.25, 1.0)
					WEST -> VoxelShapes.cuboid(0.0, 0.0, 0.1875, 1.0, 0.25, 0.8125)
					EAST -> VoxelShapes.cuboid(0.0, 0.0, 0.1875, 1.0, 0.25, 0.8125)
					else -> VoxelShapes.fullCube()
				}
			}
		},
		FabricItemSettings()
			.group(PCBFeature.group())
	)
	
	val PCB = blockItem(
		"pcb",
		FabricBlockSettings
			.copyOf(net.minecraft.block.Blocks.STONE),
		FabricItemSettings()
			.group(PCBFeature.group())
	)
}
