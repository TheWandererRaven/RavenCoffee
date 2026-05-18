package com.thewandererraven.ravencoffee.brew;

import com.thewandererraven.ravenbrewslib.brew.effect.BrewEffectBehaviour;
import com.thewandererraven.ravenbrewslib.registry.RavenBrewsLibRegistryKeys;
import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.registry.RegistryObject;
import com.thewandererraven.ravencoffee.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

public class RavenBrewEffectsRegistry {
    public static final RegistryProvider<BrewEffectBehaviour> BREW_EFFECT_BEHAVIOURS = RegistryProvider.get(RavenBrewsLibRegistryKeys.BREW_EFFECT_BEHAVIOUR, Constants.MOD_ID);

    public static final String _jump_boost_id = "effect.jump_boost";
    public static final RegistryObject<BrewEffectBehaviour> JUMP_BOOST = RavenBrewEffectsRegistry.BREW_EFFECT_BEHAVIOURS.register(
            _jump_boost_id,
            () -> BrewEffectBehaviour.attributeModifier("jump_strength")
    );
    public static void init() {

    }
}