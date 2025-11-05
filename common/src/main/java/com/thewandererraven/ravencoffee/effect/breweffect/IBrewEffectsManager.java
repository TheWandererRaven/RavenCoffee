package com.thewandererraven.ravencoffee.effect.breweffect;

public interface IBrewEffectsManager {
    void add(CoffeeBrewEffectInstance instance);
    void tick();
    CoffeeBrewEffectInstance getCurrentEffect();
}
