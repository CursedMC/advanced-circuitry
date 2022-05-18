package dev.cursedmc.ac.goose.power;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface PowerableBlockState {
	boolean adv_circ$isPowered(World world, BlockPos pos, Direction direction);
}
