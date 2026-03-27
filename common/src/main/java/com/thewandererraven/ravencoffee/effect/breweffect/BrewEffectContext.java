package com.thewandererraven.ravencoffee.effect.breweffect;

import net.minecraft.world.entity.LivingEntity;

public record BrewEffectContext(LivingEntity entity, double effectMainValue, double effectSecondaryValue) {
    public int effectMainValueAsInt() {
        return (int) Math.round(effectMainValue);
    }
    public float effectMainValueAsFloat() {
        return (float) Math.round(effectMainValue);
    }
    public int effectSecondaryValueAsInt() {
        return (int) Math.round(effectSecondaryValue);
    }
    public float effectSecondaryValueAsFloat() {
        return (float) Math.round(effectSecondaryValue);
    }
}
