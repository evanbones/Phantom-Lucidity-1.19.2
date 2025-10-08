package io.github.haykam821.phantomlucidity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import io.github.haykam821.phantomlucidity.PhantomLucidity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.Level;

@Mixin(Phantom.class)
public abstract class PhantomEntityMixin extends Mob {
	private PhantomEntityMixin(EntityType<? extends Mob> type, Level level) {
		super(type, level);
	}

	@Inject(method = "defineSynchedData", at = @At("TAIL"))
	private void initRevealedTrackedData(CallbackInfo ci) {
		this.entityData.define(PhantomLucidity.REVEALED, false);
	}

	@Inject(method = "getAmbientSound", at = @At("HEAD"), cancellable = true)
	private void preventUnrevealedAmbientSound(CallbackInfoReturnable<SoundEvent> ci) {
		if (!this.getEntityData().get(PhantomLucidity.REVEALED)) {
			ci.setReturnValue(null);
		}
	}

	@WrapWithCondition(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V"
			)
	)
	private boolean preventUnrevealedFlapSound(Level level, double x, double y, double z,
											   SoundEvent sound, SoundSource category,
											   float volume, float pitch, boolean distanceDelay) {
		return this.getEntityData().get(PhantomLucidity.REVEALED);
	}

	@WrapOperation(
			method = "aiStep",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/monster/Phantom;setSecondsOnFire(I)V"
			)
	)
	private void discardWhenUnrevealedInDaylight(Phantom entity, int seconds, Operation<Void> operation) {
		if (this.getEntityData().get(PhantomLucidity.REVEALED)) {
			operation.call(entity, seconds);
		} else {
			PhantomLucidity.poof(entity);
			entity.discard();
		}
	}

	@Override
	public boolean shouldDropExperience() {
		return super.shouldDropExperience() && this.getEntityData().get(PhantomLucidity.REVEALED);
	}

	@Override
	protected boolean shouldDropLoot() {
		return super.shouldDropLoot() && this.getEntityData().get(PhantomLucidity.REVEALED);
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void readRevealedNbt(CompoundTag nbt, CallbackInfo ci) {
		if (nbt.contains(PhantomLucidity.REVEALED_KEY)) {
			boolean revealed = nbt.getBoolean(PhantomLucidity.REVEALED_KEY);
			this.getEntityData().set(PhantomLucidity.REVEALED, revealed);
		} else {
			this.getEntityData().set(PhantomLucidity.REVEALED, true);
		}
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	private void writeRevealedNbt(CompoundTag nbt, CallbackInfo ci) {
		nbt.putBoolean(PhantomLucidity.REVEALED_KEY, this.getEntityData().get(PhantomLucidity.REVEALED));
	}
}