package com.thewandererraven.ravencoffee.effect.breweffect;

import java.util.List;

public interface IBrewEffectsManager {
    boolean add(CoffeeBrewEffectInstance instance);
    void tick();
    List<CoffeeBrewEffectInstance> getCurrentEffects();
    int getCurrentCaffeine();
    void setCurrentCaffeine(int newValue);
    int getCurrentCaffeinePercentage();
    boolean getOverloadStatus();
    public void setOverloaded(boolean newValue);
}
