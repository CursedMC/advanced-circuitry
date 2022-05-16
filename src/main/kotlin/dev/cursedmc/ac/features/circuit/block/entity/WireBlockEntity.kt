package dev.cursedmc.ac.features.circuit.block.entity

import dev.cursedmc.ac.util.BlockPosUtil
import dev.cursedmc.ac.util.toIntArray
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.Packet
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.util.math.BlockPos

class WireBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(BlockEntityTypes.WIRE_BLOCK, pos, state) {
	var powerSource: BlockPos? = pos
	var iteration: Int = 0
	
	override fun writeNbt(nbt: NbtCompound) {
		nbt.putIntArray("powerSource", powerSource?.toIntArray() ?: pos.toIntArray())
		nbt.putInt("iteration", iteration)
		
		super.writeNbt(nbt)
	}
	
	override fun readNbt(nbt: NbtCompound) {
		super.readNbt(nbt)
		
		powerSource = BlockPosUtil.fromIntArray(nbt.getIntArray("powerSource"))
		iteration = nbt.getInt("iteration")
	}
	
	override fun toUpdatePacket(): Packet<ClientPlayPacketListener> {
		return BlockEntityUpdateS2CPacket.create(this)
	}
	
	override fun toInitialChunkDataNbt(): NbtCompound {
		return createNbt()
	}
}
