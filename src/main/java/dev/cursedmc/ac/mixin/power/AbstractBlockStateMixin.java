package dev.cursedmc.ac.mixin.power;

import dev.cursedmc.ac.goose.power.PowerableBlockState;
import dev.cursedmc.ac.goose.power.PowerableBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin implements PowerableBlockState {
	@Shadow public abstract Block getBlock();
	
	@Shadow protected abstract BlockState asBlockState();
	
	@Override
	public boolean adv_circ$isPowered(World world, BlockPos pos, Direction direction) {
		return ((PowerableBlock) this.getBlock()).adv_circ$isPowered(this.asBlockState(), world, pos, direction);
	}
}
