package dev.cursedmc.ac.features.pcb.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class PCBBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(BlockEntityType.PCB, pos, state)
