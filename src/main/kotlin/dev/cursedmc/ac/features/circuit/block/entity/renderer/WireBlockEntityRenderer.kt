package dev.cursedmc.ac.features.circuit.block.entity.renderer

import dev.cursedmc.ac.features.circuit.block.entity.WireBlockEntity
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack

class WireBlockEntityRenderer : BlockEntityRenderer<WireBlockEntity> {
	override fun render(
		entity: WireBlockEntity,
		tickDelta: Float,
		matrices: MatrixStack,
		vertexConsumers: VertexConsumerProvider,
		light: Int,
		overlay: Int
	) {
	
	}
}