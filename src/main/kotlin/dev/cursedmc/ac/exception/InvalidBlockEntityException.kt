package dev.cursedmc.ac.exception

import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * very useful class for when you want to annoy modpack creators with mod incompatibilities
 */
class InvalidBlockEntityException(pos: BlockPos, world: World, blockEntityType: BlockEntityType<*>) : ModException("${blockEntityType.javaClass.typeName} is not a valid BlockEntity at world position $pos in dimension ${world.dimension.javaClass.typeName}")
