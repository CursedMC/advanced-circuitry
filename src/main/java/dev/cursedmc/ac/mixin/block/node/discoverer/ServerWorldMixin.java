package dev.cursedmc.ac.mixin.block.node.discoverer;

import com.kneelawk.graphlib.graph.BlockNode;
import dev.cursedmc.ac.goose.block.node.discoverer.ServerWorldWithNodes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin(ServerWorld.class)
public final class ServerWorldMixin implements ServerWorldWithNodes {
	@Unique
	private final Map<BlockPos, BlockNode> blockNodes = new HashMap<>();
	
	@Override
	public @Nullable BlockNode adv_circ$getBlockNode(@NotNull BlockPos pos) {
		return blockNodes.get(pos);
	}
	
	@Override
	public void adv_circ$setBlockNode(@NotNull BlockPos pos, @Nullable BlockNode node) {
		if (node != null) {
			blockNodes.put(pos, node);
		} else {
			blockNodes.remove(pos);
		}
	}
}
