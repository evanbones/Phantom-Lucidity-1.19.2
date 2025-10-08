package io.github.haykam821.phantomlucidity.tag;

import io.github.haykam821.phantomlucidity.PhantomLucidity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class PhantomLucidityItemTags {
	public static final TagKey<Item> PHANTOMS_REVEALED_WHILE_USING =
			ItemTags.create(PhantomLucidity.identifier("phantoms_revealed_while_using"));

	private PhantomLucidityItemTags() {
	}
}