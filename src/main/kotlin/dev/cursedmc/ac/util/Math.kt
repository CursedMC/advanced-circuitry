package dev.cursedmc.ac.util

import net.minecraft.util.math.BlockPos

fun BlockPos.toIntArray(): IntArray {
	return intArrayOf(this.x, this.y, this.z)
}

object BlockPosUtil {
	fun fromIntArray(intArray: IntArray): BlockPos {
		return BlockPos(intArray[0], intArray[1], intArray[2])
	}
}
