package dev.cursedmc.ac.goose.power;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface PowerableBlock {
	boolean adv_circ$isPowered(BlockState state, World world, BlockPos pos, Direction direction);
}
