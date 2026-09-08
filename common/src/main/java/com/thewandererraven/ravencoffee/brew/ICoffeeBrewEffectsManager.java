package com.thewandererraven.ravencoffee.brew;

import net.minecraft.server.level.ServerPlayer;

public interface ICoffeeBrewEffectsManager {
    int getMaxCaffeine();
    int getCurrentCaffeine();
    void setCurrentCaffeine(int newValue);
    void addCaffeine(int addedCaffeine);
    boolean getOverloadStatus();
    void setOverloadedStatus(boolean newValue);
    boolean isOverloaded();
    void tickCaffeine(ServerPlayer player);

    default int getCurrentCaffeinePercentage() {
        return Math.min(((getCurrentCaffeine() * 100) / this.getMaxCaffeine()), 100);
    }

    default void clearCaffeine() {
        this.setOverloadedStatus(false);
        this.setCurrentCaffeine(0);
    }
}
