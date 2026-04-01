package com.thewandererraven.ravencoffee.effect.breweffect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.function.Consumer;

public class BrewEffectCore {
    ResourceLocation attributeId;
    AttributeModifier.Operation attributeOperation;
    String type;
    public Consumer<BrewEffectContext> primaryEffect;
    public Consumer<BrewEffectContext> additionalEffect;

    public BrewEffectCore(String type, ResourceLocation attributeId, AttributeModifier.Operation attributeOperation, Consumer<BrewEffectContext> primaryEffect, Consumer<BrewEffectContext> additionalEffect) {
        this.type = type;
        this.attributeId = attributeId;
        this.attributeOperation = attributeOperation;
        this.primaryEffect = primaryEffect;
        this.additionalEffect = additionalEffect;
    }

    public static BrewEffectCore instant(Consumer<BrewEffectContext> primaryEffect, Consumer<BrewEffectContext> additionalEffect) {
        return new BrewEffectCore("instant", (ResourceLocation) null, null, primaryEffect, additionalEffect);
    }

    public static BrewEffectCore instant(Consumer<BrewEffectContext> primaryEffect) {
        return new BrewEffectCore("instant", (ResourceLocation) null, null, primaryEffect, context -> {});
    }

    public static BrewEffectCore attributeModifier(ResourceLocation attributeId) {
        return attributeModifier(attributeId, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, context -> {}, context -> {});
    }

    public static BrewEffectCore attributeModifier(ResourceLocation attributeId, Consumer<BrewEffectContext> primaryEffect) {
        return attributeModifier(attributeId, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, primaryEffect, context -> {});
    }

    public static BrewEffectCore attributeModifier(ResourceLocation attributeId, AttributeModifier.Operation attributeOperation) {
        return attributeModifier(attributeId, attributeOperation, context -> {}, context -> {});
    }

    public static BrewEffectCore attributeModifier(ResourceLocation attributeId, AttributeModifier.Operation attributeOperation, Consumer<BrewEffectContext> primaryEffect) {
        return attributeModifier(attributeId, attributeOperation, primaryEffect, context -> {});
    }

    public static BrewEffectCore attributeModifier(ResourceLocation attributeId, Consumer<BrewEffectContext> primaryEffect, Consumer<BrewEffectContext> additionalEffect) {
        return attributeModifier(attributeId, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, primaryEffect, additionalEffect);
    }

    public static BrewEffectCore attributeModifier(ResourceLocation attributeId, AttributeModifier.Operation attributeOperation, Consumer<BrewEffectContext> primaryEffect, Consumer<BrewEffectContext> additionalEffect) {
        return new BrewEffectCore("attribute_modifier", attributeId, attributeOperation, primaryEffect, additionalEffect);
    }

    public static BrewEffectCore attributeModifier(String attributeId) {
        return attributeModifier(ResourceLocation.withDefaultNamespace(attributeId));
    }

    public static BrewEffectCore attributeModifier(String attributeId, Consumer<BrewEffectContext> primaryEffect) {
        return attributeModifier(ResourceLocation.withDefaultNamespace(attributeId), primaryEffect);
    }

    public static BrewEffectCore attributeModifier(String attributeId, AttributeModifier.Operation attributeOperation) {
        return attributeModifier(ResourceLocation.withDefaultNamespace(attributeId), attributeOperation);
    }

    public static BrewEffectCore attributeModifier(String attributeId, AttributeModifier.Operation attributeOperation, Consumer<BrewEffectContext> primaryEffect) {
        return attributeModifier(ResourceLocation.withDefaultNamespace(attributeId), attributeOperation, primaryEffect);
    }

    public static BrewEffectCore attributeModifier(String attributeId, Consumer<BrewEffectContext> primaryEffect, Consumer<BrewEffectContext> additionalEffect) {
        return attributeModifier(ResourceLocation.withDefaultNamespace(attributeId), primaryEffect, additionalEffect);
    }

    public static BrewEffectCore attributeModifier(String attributeId, AttributeModifier.Operation attributeOperation, Consumer<BrewEffectContext> primaryEffect, Consumer<BrewEffectContext> additionalEffect) {
        return attributeModifier(ResourceLocation.withDefaultNamespace(attributeId), attributeOperation, primaryEffect, additionalEffect);
    }
}
