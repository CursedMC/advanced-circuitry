package dev.cursedmc.ac.goose.block.node.discoverer;

import com.kneelawk.graphlib.graph.BlockNode;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ServerWorldWithNodes {
	@Nullable BlockNode adv_circ$getBlockNode(@NotNull BlockPos pos);
	
	void adv_circ$setBlockNode(@NotNull BlockPos pos, @Nullable BlockNode node);
}
