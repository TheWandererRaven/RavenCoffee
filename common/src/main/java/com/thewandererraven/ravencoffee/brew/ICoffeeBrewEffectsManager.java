package com.thewandererraven.ravencoffee.brew;

public interface ICoffeeBrewEffectsManager {
    int getCurrentCaffeine();
    void setCurrentCaffeine(int newValue);
    int getCurrentCaffeinePercentage();
    boolean getOverloadStatus();
    public void setOverloaded(boolean newValue);
    public void clearCaffeine();
    public void sendCaffeineToClient();
}
