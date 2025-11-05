package com.thewandererraven.ravencoffee.util;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.effect.breweffect.CoffeeBrewEffect;
import com.thewandererraven.ravencoffee.effect.breweffect.MultiEffect;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class RavenCoffeeRegistryKeys {
    public static final ResourceKey<Registry<CoffeeBrewEffect>> BREW_EFFECTS = createRegistryKey("brew_effects");

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
        return ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
    }
}
