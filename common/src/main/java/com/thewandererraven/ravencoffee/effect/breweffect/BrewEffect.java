package com.thewandererraven.ravencoffee.effect.breweffect;

import com.thewandererraven.ravencoffee.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class BrewEffect {
    public ResourceLocation effectId;
    public int effectTicksDuration;
    public double mainValue;
    public double secondaryValue;
    public Consumer<BrewEffectContext> primaryEffect;
    public Consumer<BrewEffectContext> additionalEffect;

    public BrewEffect(ResourceLocation effectId, int effectTicksDuration, double mainValue, double secondaryValue, Consumer<BrewEffectContext> primaryEffect, Consumer<BrewEffectContext> additionalEffect) {
        this.effectId = effectId;
        this.effectTicksDuration = effectTicksDuration;
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

    public static final BrewEffect EMPTY = new BrewEffect(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "effect.empty"),
            0,
            0,
            0,
            context -> {},
            context -> {}
    );

}
