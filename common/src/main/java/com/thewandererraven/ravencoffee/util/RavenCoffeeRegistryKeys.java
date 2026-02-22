package com.thewandererraven.ravencoffee.util;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.effect.breweffect.BrewEffectCore;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class RavenCoffeeRegistryKeys {
    public static final ResourceKey<Registry<BrewEffectCore>> BREW_EFFECTS_CORE = createRegistryKey("brew_effects_core");

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
        return ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
    }
}
