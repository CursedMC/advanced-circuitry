package dev.cursedmc.ac.features.circuit.block.listener

import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent
import net.minecraft.world.event.PositionSource
import net.minecraft.world.event.listener.GameEventListener

class WireListener(private val positionSource: PositionSource, private val range: Int) : GameEventListener {
	override fun getPositionSource(): PositionSource {
		return positionSource
	}
	
	override fun getRange(): Int {
		return range
	}
	
	override fun listen(world: World?, event: GameEvent?, entity: Entity?, pos: BlockPos?): Boolean {
		TODO("Not yet implemented")
	}
}
