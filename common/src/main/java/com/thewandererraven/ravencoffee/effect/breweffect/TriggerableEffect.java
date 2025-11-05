package com.thewandererraven.ravencoffee.effect.breweffect;

import com.thewandererraven.ravencoffee.Constants;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public abstract class TriggerableEffect {
    public int triggerTicks;
    public int effectDuration;

    public TriggerableEffect(int triggerTicks, int effectDuration) {
        this.triggerTicks = triggerTicks;
        this.effectDuration = effectDuration;
    }

    public int getEffectLastTick() {
        return this.triggerTicks + this.effectDuration;
    }

    public record AttributeTemplate(ResourceLocation id, double amount, AttributeModifier.Operation operation) {
        public AttributeModifier create(int level) {
            return new AttributeModifier(this.id, this.amount * (double)(level + 1), this.operation);
        }
    }

    public abstract Lifecycle applyEffectStart(LivingEntity player);

    public abstract Lifecycle applyEffectEnd(LivingEntity player);

    public enum Lifecycle {
        PENDING,
        TRIGGERED,
        FINISHED
    }
}
