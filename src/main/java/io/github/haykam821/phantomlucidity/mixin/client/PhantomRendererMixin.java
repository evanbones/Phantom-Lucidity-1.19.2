package io.github.haykam821.phantomlucidity.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import io.github.haykam821.phantomlucidity.PhantomLucidity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Phantom;

@Mixin(MobRenderer.class)
public class PhantomRendererMixin<T extends Mob> {
	@Inject(
			method = "render(Lnet/minecraft/world/entity/Mob;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
			at = @At("HEAD"),
			cancellable = true
	)
	private void cancelRenderIfNotRevealed(T entity, float entityYaw, float partialTicks,
										   PoseStack poseStack, MultiBufferSource buffer,
										   int packedLight, CallbackInfo ci) {

		if (!(entity instanceof Phantom phantom)) return;

        boolean revealed = phantom.getEntityData().get(PhantomLucidity.REVEALED);
		if (!revealed) ci.cancel();
	}
}