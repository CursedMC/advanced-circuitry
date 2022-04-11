package dev.cursedmc.ac.power;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface PowerableBlock {
	boolean isPowered(BlockState state, World world, BlockPos pos, Direction direction);
}
