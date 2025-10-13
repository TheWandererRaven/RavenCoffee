package com.thewandererraven.ravencoffee.util;

import com.thewandererraven.ravencoffee.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class RavenCoffeeTags {
    public static class Items {
        public static final TagKey<Item> ROASTED_COMMON_BEANS = createTag("roasted_common_beans");

        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
        }
    }
}
