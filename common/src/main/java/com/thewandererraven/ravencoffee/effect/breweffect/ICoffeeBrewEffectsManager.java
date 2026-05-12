package com.thewandererraven.ravencoffee.effect.breweffect;

public interface ICoffeeBrewEffectsManager {
    int getCurrentCaffeine();
    void setCurrentCaffeine(int newValue);
    int getCurrentCaffeinePercentage();
    boolean getOverloadStatus();
    public void setOverloaded(boolean newValue);
    public void clearCaffeine();
    public void sendCaffeineToClient();
}
