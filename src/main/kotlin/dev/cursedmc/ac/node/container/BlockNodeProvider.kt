package dev.cursedmc.ac.node.container

import com.kneelawk.graphlib.graph.BlockNode
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

// https://github.com/Kneelawk/WiredRedstone/blob/50f5ebbc285c21b8d02980604641722a40888ade/src/main/kotlin/com/kneelawk/wiredredstone/part/BlockNodeContainer.kt
interface BlockNodeProvider {
	fun createBlockNodes(world: ServerWorld, pos: BlockPos): Collection<BlockNode>
}
