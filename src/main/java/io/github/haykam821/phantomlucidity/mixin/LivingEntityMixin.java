package io.github.haykam821.phantomlucidity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.haykam821.phantomlucidity.PhantomLucidity;
import io.github.haykam821.phantomlucidity.tag.PhantomLucidityItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	private LivingEntityMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Inject(method = "updatingUsingItem", at = @At("HEAD"))
	private void tickRevealPhantomsUsingSpyglass(CallbackInfo ci) {
		LivingEntity entity = (LivingEntity) (Object) this;
		ItemStack stack = entity.getUseItem();

		if (!this.level.isClientSide && stack.is(PhantomLucidityItemTags.PHANTOMS_REVEALED_WHILE_USING)) {
			PhantomLucidity.tryRevealPhantom(entity);
		}
	}
}