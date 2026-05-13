package com.thewandererraven.ravencoffee.util;

import com.thewandererraven.ravenbrewslib.brew.effect.BrewEffectBehaviour;
import com.thewandererraven.ravencoffee.Constants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class RavenCoffeeRegistryKeys {
    public static final ResourceKey<Registry<BrewEffectBehaviour>> COFFEE_BREW_EFFECT_BEHAVIOURS = createRegistryKey("coffee_brew_effect_behaviours");

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
        return ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
    }
}
