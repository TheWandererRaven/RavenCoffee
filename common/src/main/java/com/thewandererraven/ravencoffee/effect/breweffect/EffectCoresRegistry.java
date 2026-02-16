package com.thewandererraven.ravencoffee.effect.breweffect;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.registry.RegistryObject;
import com.thewandererraven.ravencoffee.registry.RegistryProvider;
import com.thewandererraven.ravencoffee.util.RavenCoffeeRegistryKeys;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class EffectCoresRegistry {
    public static final RegistryProvider<Consumer<BrewEffectContext>> BREW_EFFECT_CORES = RegistryProvider.create(RavenCoffeeRegistryKeys.BREW_EFFECT_CORES, Constants.MOD_ID, (Class<Consumer<BrewEffectContext>>) (Class<?>) Consumer.class);

    public static final String _heal_id = "heal";
    public static final RegistryObject<Consumer<BrewEffectContext>> HEAL = BREW_EFFECT_CORES.register(
            _heal_id,
            () -> context -> context.entity().heal(context.effectSecondaryValueAsDouble())
    );

}