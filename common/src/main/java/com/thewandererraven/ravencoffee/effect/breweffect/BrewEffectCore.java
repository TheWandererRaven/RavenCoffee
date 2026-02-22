package com.thewandererraven.ravencoffee.effect.breweffect;

import com.thewandererraven.ravencoffee.Constants;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class BrewEffectCore {
    ResourceLocation attributeId;
    String type;
    public Consumer<BrewEffectContext> primaryEffect;
    public Consumer<BrewEffectContext> additionalEffect;

    public BrewEffectCore(String type, ResourceLocation attributeId, Consumer<BrewEffectContext> primaryEffect, Consumer<BrewEffectContext> additionalEffect) {
        this.type = type;
        this.attributeId = attributeId;
        this.primaryEffect = primaryEffect;
        this.additionalEffect = additionalEffect;
    }

    public static BrewEffectCore instant(Consumer<BrewEffectContext> primaryEffect, Consumer<BrewEffectContext> additionalEffect) {
        return new BrewEffectCore("instant", (ResourceLocation) null, primaryEffect, additionalEffect);
    }

    public static BrewEffectCore instant(Consumer<BrewEffectContext> primaryEffect) {
        return new BrewEffectCore("instant", (ResourceLocation) null, primaryEffect, context -> {});
    }

    public static BrewEffectCore attributeModifier(ResourceLocation attributeId) {
        return new BrewEffectCore("attribute_modifier", attributeId, context -> {}, context -> {});
    }

    public static BrewEffectCore attributeModifier(String attributeId) {
        return new BrewEffectCore("attribute_modifier", ResourceLocation.withDefaultNamespace(attributeId), context -> {}, context -> {});
    }
}
