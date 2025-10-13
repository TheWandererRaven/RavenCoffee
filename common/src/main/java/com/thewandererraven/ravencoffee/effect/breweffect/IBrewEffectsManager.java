package com.thewandererraven.ravencoffee.effect.breweffect;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;

public interface IBrewEffectsManager {
    void add(MultiEffectInstance instance);
    void tick();
    MultiEffectInstance getCurrentEffect();
}
