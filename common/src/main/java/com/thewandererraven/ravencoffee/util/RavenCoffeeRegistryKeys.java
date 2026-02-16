package com.thewandererraven.ravencoffee.util;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.effect.breweffect.BrewEffectContext;
import com.thewandererraven.ravencoffee.effect.breweffect.CoffeeBrewEffect;
import com.thewandererraven.ravencoffee.effect.breweffect.MultiEffect;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class RavenCoffeeRegistryKeys {
    public static final ResourceKey<Registry<CoffeeBrewEffect>> BREW_EFFECTS = createRegistryKey("brew_effects");
    public static final ResourceKey<Registry<Consumer<BrewEffectContext>>> BREW_EFFECT_CORES = createRegistryKey("brew_effect_cores");

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
        return ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
    }
}
