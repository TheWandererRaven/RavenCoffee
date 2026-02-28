package com.thewandererraven.ravencoffee.effect.breweffect;

import com.thewandererraven.ravencoffee.datacomponents.CoffeeBrewData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface IBrewEffectsManager {
    public boolean isEmpty();
    boolean add(CoffeeBrewData brewData);
    void tick();
    List<BrewEffect> getEffectsStack();
    int getCurrentCaffeine();
    void setCurrentCaffeine(int newValue);
    int getCurrentCaffeinePercentage();
    boolean getOverloadStatus();
    public void setOverloaded(boolean newValue);
    public void setEffectIcons(List<ResourceLocation> iconLocations);
    public List<ResourceLocation> getEffectIcons();
    public void clearEffects();
    public void clearCaffeine();
    public void clearAll();
    public void sendEffectIconsToClient();
    public void sendDurationsToClient();
    public void sendCaffeineToClient();
    public void sendAllInfoToClient();
    public CompoundTag serializeNBT();
    public void deserializeNBT(CompoundTag tag);
}
