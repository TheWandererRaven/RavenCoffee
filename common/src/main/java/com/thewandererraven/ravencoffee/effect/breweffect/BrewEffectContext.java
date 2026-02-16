package com.thewandererraven.ravencoffee.effect.breweffect;

import net.minecraft.world.entity.LivingEntity;

public record BrewEffectContext(LivingEntity entity, double effectMainValue, double effectSecondaryValue) {
    public int effectMainValueAsDouble() {
        return (int) Math.round(effectMainValue);
    }
    public int effectSecondaryValueAsDouble() {
        return (int) Math.round(effectSecondaryValue);
    }
}
