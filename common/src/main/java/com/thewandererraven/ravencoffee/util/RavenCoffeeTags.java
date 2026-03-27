package com.thewandererraven.ravencoffee.util;

import com.thewandererraven.ravencoffee.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class RavenCoffeeTags {
    public static class Items {
        public static final TagKey<Item> COFFEE_BREW_BASE = createTag("coffee_brew_base");
        public static final TagKey<Item> COFFEE_BREW_CONTAINER = createTag("coffee_brew_container");
        public static final TagKey<Item> COFFEE_BREW_INGREDIENT = createTag("coffee_brew_ingredient");

        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
        }
    }
}
