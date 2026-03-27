package com.thewandererraven.ravencoffee.effect.breweffect;


import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class AttributeModifierEffect extends BrewEffect {

    public AttributeModifierEffect(ResourceLocation effectId, ResourceLocation attributeId, AttributeModifier.Operation attributeOperation, int effectTicksDuration, double mainValue, double secondaryValue, Holder<Attribute> attributeHolder) {
        this(effectId, attributeId, attributeOperation, effectTicksDuration, mainValue, secondaryValue, attributeHolder, context -> {}, context -> {});
    }

    public AttributeModifierEffect(ResourceLocation effectId, ResourceLocation attributeId, AttributeModifier.Operation attributeOperation, int effectTicksDuration, double mainValue, double secondaryValue, Holder<Attribute> attributeHolder, Consumer<BrewEffectContext> primaryEffect) {
        this(effectId, attributeId, attributeOperation, effectTicksDuration, mainValue, secondaryValue, attributeHolder, primaryEffect, context -> {});
    }

    public AttributeModifierEffect(ResourceLocation effectId, ResourceLocation attributeId, AttributeModifier.Operation attributeOperation, int effectTicksDuration, double mainValue, double secondaryValue, Holder<Attribute> attributeHolder, Consumer<BrewEffectContext> primaryEffect, Consumer<BrewEffectContext> additionalEffect) {
        super(effectId, effectTicksDuration, mainValue, secondaryValue,
                brewEffectContext -> {
            AttributeModifierEffect.addAttributeModifierToPlayer(brewEffectContext.entity(), attributeHolder,
                    new AttributeModifierEffect.AttributeTemplate(
                            attributeId,
                            brewEffectContext.effectMainValue(),
                            attributeOperation
                    ).create(1));
            primaryEffect.accept(brewEffectContext);
            },
                brewEffectContext -> {
            AttributeModifierEffect.removeAttributeModifierToPlayer(brewEffectContext.entity(), attributeHolder,
                    new AttributeModifierEffect.AttributeTemplate(
                            attributeId,
                            brewEffectContext.effectSecondaryValue() * -1,
                            attributeOperation
                    ).create(1));
            additionalEffect.accept(brewEffectContext);
                }
        );
    }

    private static void addAttributeModifierToPlayer(LivingEntity entity, Holder<Attribute> attribute, AttributeModifier modifier) {
        if(entity instanceof Player player) {
            AttributeInstance instance = player.getAttributes().getInstance(attribute);
            if (instance != null)
                if(!instance.hasModifier(modifier.id()))
                    instance.addTransientModifier(modifier);
        }
    }

    private static void removeAttributeModifierToPlayer(LivingEntity entity, Holder<Attribute> attribute, AttributeModifier modifier) {
        if(entity instanceof Player player) {
            AttributeInstance instance = player.getAttributes().getInstance(attribute);
            if (instance != null)
                instance.removeModifier(modifier);
        }
    }

    public record AttributeTemplate(ResourceLocation id, double amount, AttributeModifier.Operation operation) {
        public AttributeModifier create(int level) {
            return new AttributeModifier(this.id, this.amount * (double)(level + 1), this.operation);
        }
    }
}
