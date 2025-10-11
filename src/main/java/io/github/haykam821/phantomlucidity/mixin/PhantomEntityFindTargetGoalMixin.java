package io.github.haykam821.phantomlucidity.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.phantomlucidity.PhantomLucidity;
import net.minecraft.world.entity.monster.Phantom;

@Mixin(targets = "net.minecraft.world.entity.monster.Phantom$PhantomAttackPlayerTargetGoal")
public abstract class PhantomEntityFindTargetGoalMixin {
	@Shadow @Final Phantom this$0;

	@Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
	private void preventFindingTargetsWhileNotRevealed(CallbackInfoReturnable<Boolean> cir) {
		boolean revealed = this$0.getEntityData().get(PhantomLucidity.REVEALED);
		if (!revealed) {
			cir.setReturnValue(false);
		}
	}
}
