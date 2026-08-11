package com.thewandererraven.ravencoffee.util;

import com.thewandererraven.ravenbrewslib.brew.effect.IBrewEffectManagerHolder;
import com.thewandererraven.ravencoffee.brew.DefaultCoffeeBrewEffectsManager;
import net.minecraft.world.entity.LivingEntity;

public class RavenCoffeeGeneralUtils {
    public static DefaultCoffeeBrewEffectsManager getCastCoffeeBrewEffectsManager(Object entity) {
        if(entity instanceof IBrewEffectManagerHolder holder)
            if(holder.ravenbrewslib$getBrewEffectManager() instanceof DefaultCoffeeBrewEffectsManager defCoffeeBrewEffManager)
                return defCoffeeBrewEffManager;
        return null;
    }
}
