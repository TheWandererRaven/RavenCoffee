package com.thewandererraven.ravencoffee.effect.breweffect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.apache.logging.log4j.util.BiConsumer;

import java.util.function.Consumer;

public class BrewEffectCore {
    public int effectDuration;
    public double mainValue;
    public double secondaryValue;
    public Consumer<BrewEffectContext> primaryEffect;
    public Consumer<BrewEffectContext> additionalEffect;

    public BrewEffectCore(int effectDuration, double mainValue, double secondaryValue, Consumer<BrewEffectContext> primaryEffect, Consumer<BrewEffectContext> additionalEffect) {
        this.effectDuration = effectDuration;
        this.mainValue = mainValue;
        this.secondaryValue = secondaryValue;
        this.primaryEffect = primaryEffect;
        this.additionalEffect = additionalEffect;
    }

    public boolean applyPrimaryEffect(LivingEntity player)
    {
        if(this.primaryEffect != null)
        {
            this.primaryEffect.accept(new BrewEffectContext(player, this.mainValue, this.secondaryValue));
            return true;
        }
        return false;
    }

    public boolean applyAdditionalEffect(LivingEntity player)
    {
        if(this.additionalEffect != null)
        {
            this.additionalEffect.accept(new BrewEffectContext(player, this.mainValue, this.secondaryValue));
            return true;
        }
        return false;
    }

    public record AttributeTemplate(ResourceLocation id, double amount, AttributeModifier.Operation operation) {
        public AttributeModifier create(int level) {
            return new AttributeModifier(this.id, this.amount * (double)(level + 1), this.operation);
        }
    }
}
