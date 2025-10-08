package io.github.haykam821.phantomlucidity;

import io.github.haykam821.phantomlucidity.sound.PhantomLuciditySoundEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(PhantomLucidity.MOD_ID)
public class PhantomLucidity {
    public static final String MOD_ID = "phantomlucidity";
    public static final String REVEALED_KEY = "Revealed";

    public static final EntityDataAccessor<Boolean> REVEALED =
            SynchedEntityData.defineId(Phantom.class, EntityDataSerializers.BOOLEAN);

    public PhantomLucidity() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        PhantomLuciditySoundEvents.register(modEventBus);
    }

    private static boolean isRevealable(Entity entity) {
        return !entity.isSpectator();
    }

    public static void poof(LivingEntity entity) {
        if (entity.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.POOF,
                    entity.getX(), entity.getY() + 1.0, entity.getZ(),
                    20, 0.5, 0.5, 0.5, 0.02);
        }

        entity.playSound(PhantomLuciditySoundEvents.ENTITY_PHANTOM_FADE.get(), 1.0F, 1.0F);
    }

    public static void tryRevealPhantom(LivingEntity entity) {
        Vec3 rotation = entity.getLookAngle();
        Vec3 startPos = entity.getEyePosition();
        Vec3 endPos = startPos.add(rotation.x * 100, rotation.y * 100, rotation.z * 100);
        AABB box = new AABB(startPos, endPos).inflate(1);

        EntityHitResult hitResult = ProjectileUtil.getEntityHitResult(
                entity.level, entity, startPos, endPos, box, PhantomLucidity::isRevealable
        );

        if (hitResult == null) return;

        Entity target = hitResult.getEntity();
        if (!entity.hasLineOfSight(target)) return;

        if (target instanceof Phantom phantom && !phantom.getEntityData().get(REVEALED)) {
            phantom.getEntityData().set(REVEALED, true);
            PhantomLucidity.poof(phantom);
        }
    }

    public static ResourceLocation identifier(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}