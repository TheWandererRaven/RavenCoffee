package com.thewandererraven.ravencoffee.util;

import com.thewandererraven.ravencoffee.effect.breweffect.BrewEffectContext;
import com.thewandererraven.ravencoffee.effect.breweffect.EffectCoresRegistry;
import com.thewandererraven.ravencoffee.registry.RegistryObject;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class BrewEffectsUtils {
    public static Consumer<BrewEffectContext> findEffectInRegistry(ResourceLocation effectId) {
        RegistryObject<Consumer<BrewEffectContext>> foundEffect = EffectCoresRegistry.BREW_EFFECT_CORES.getEntries().stream()
                .filter(param -> param.getId().equals(effectId)).findFirst()
                .orElse(null);
        if(foundEffect != null)
            return foundEffect.get();
        return null;
    }
    public static Holder<Attribute> findAttributeByItsId(Level level, ResourceLocation attributeId) {
        Registry<Attribute> reg = level.registryAccess().lookup(Registries.ATTRIBUTE).orElse(null);
        if(reg == null)
            return null;
        return reg.get(attributeId).orElse(null);
    }
}
