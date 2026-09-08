package com.thewandererraven.ravencoffee.util;

import com.thewandererraven.ravenbrewslib.brew.effect.IBrewEffectManagerHolder;
import com.thewandererraven.ravencoffee.brew.CoffeeBrewEffectsManager;

public class RavenCoffeeGeneralUtils {
    public static CoffeeBrewEffectsManager getCastCoffeeBrewEffectsManager(Object entity) {
        if(entity instanceof IBrewEffectManagerHolder holder)
            if(holder.ravenbrewslib$getBrewEffectManager() instanceof CoffeeBrewEffectsManager defCoffeeBrewEffManager)
                return defCoffeeBrewEffManager;
        return null;
    }
}
