package dev.cursedmc.ac.mixin.power;

import dev.cursedmc.ac.features.circuit.block.component.PoweredComponentBlock;
import dev.cursedmc.ac.goose.power.PowerableBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin implements PowerableBlock {
	@Deprecated
	public boolean adv_circ$isPowered(BlockState state, World world, BlockPos pos, Direction direction) {
		if (state.contains(PoweredComponentBlock.Companion.getPOWERED())) return state.get(PoweredComponentBlock.Companion.getPOWERED());
		return state.getStrongRedstonePower(world, pos, direction) > 0;
	}
}
