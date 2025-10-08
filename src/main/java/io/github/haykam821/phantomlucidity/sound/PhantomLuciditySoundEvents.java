package io.github.haykam821.phantomlucidity.sound;

import io.github.haykam821.phantomlucidity.PhantomLucidity;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class PhantomLuciditySoundEvents {
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
			DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, PhantomLucidity.MOD_ID);

	public static final RegistryObject<SoundEvent> ENTITY_PHANTOM_FADE =
			SOUND_EVENTS.register("entity.phantom.fade",
					() -> new SoundEvent(PhantomLucidity.identifier("entity.phantom.fade")));

	private PhantomLuciditySoundEvents() {
	}

	public static void register(IEventBus modEventBus) {
		SOUND_EVENTS.register(modEventBus);
	}
}