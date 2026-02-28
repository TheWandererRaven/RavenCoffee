package com.thewandererraven.ravencoffee.effect.breweffect;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.registry.RegistryObject;
import com.thewandererraven.ravencoffee.registry.RegistryProvider;
import com.thewandererraven.ravencoffee.util.RavenCoffeeRegistryKeys;

public class BrewEffectCoresRegistry {
    public static final RegistryProvider<BrewEffectCore> BREW_EFFECT_CORES = RegistryProvider.create(RavenCoffeeRegistryKeys.BREW_EFFECT_CORES, Constants.MOD_ID, BrewEffectCore.class);

    public static final String _heal_id = "effect.heal";
    public static final RegistryObject<BrewEffectCore> HEAL = BREW_EFFECT_CORES.register(
            _heal_id,
            () -> BrewEffectCore.instant(context -> context.entity().heal(context.effectMainValueAsInt()))
    );

    public static final String _speed_id = "effect.speed";
    public static final RegistryObject<BrewEffectCore> SPEED = BREW_EFFECT_CORES.register(
            _speed_id,
            () -> BrewEffectCore.attributeModifier("movement_speed")
    );
    // YES, it's basically the same, I just want the different id. I might add more functionality later so the difference is actually different
    public static final String _slowness_id = "effect.slowness";
    public static final RegistryObject<BrewEffectCore> SLOWNESS = BREW_EFFECT_CORES.register(
            _slowness_id,
            () -> BrewEffectCore.attributeModifier("movement_speed")
    );

    public static void init() {

    }
}
