package com.thewandererraven.ravencoffee.effect.breweffect;

import com.thewandererraven.ravencoffee.item.data.CoffeeBrewData;

import java.util.List;

public interface IBrewEffectsManager {
    boolean add(CoffeeBrewData brewData);
    void tick();
    List<CoffeeBrewEffectInstance> getCurrentEffects();
    int getCurrentCaffeine();
    void setCurrentCaffeine(int newValue);
    int getCurrentCaffeinePercentage();
    boolean getOverloadStatus();
    public void setOverloaded(boolean newValue);
}
