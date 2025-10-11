package io.github.haykam821.phantomlucidity.mixin.client;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Phantom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import io.github.haykam821.phantomlucidity.PhantomLucidity;

@Mixin(LivingEntityRenderer.class)
public abstract class PhantomRendererMixin {

	@Inject(
			method = "isBodyVisible",
			at = @At("HEAD"),
			cancellable = true
	)
	private void makeUnrevealedPhantomInvisible(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		if (!(entity instanceof Phantom phantom)) return;

		boolean revealed = phantom.getEntityData().get(PhantomLucidity.REVEALED);

		if (!revealed) {
			cir.setReturnValue(false);
		}
	}
}