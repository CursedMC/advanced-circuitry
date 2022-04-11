package dev.cursedmc.ac.power;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface PowerableBlockState {
	boolean isPowered(World world, BlockPos pos, Direction direction);
}
