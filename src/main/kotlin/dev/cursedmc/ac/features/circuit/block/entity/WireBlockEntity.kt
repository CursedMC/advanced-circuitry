package dev.cursedmc.ac.features.circuit.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class WireBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(BlockEntityType.WIRE_BLOCK, pos, state)
