package com.thewandererraven.ravencoffee.effect.breweffect;


import com.thewandererraven.ravencoffee.Constants;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class TriggerableEffect {
    public int triggerTicks;
    public int effectDuration;
    public Consumer<LivingEntity> effectStart;
    public Consumer<LivingEntity> effectEnd;

    public TriggerableEffect(int triggerTicks, int effectDuration) {
        this(triggerTicks, effectDuration,
                entity -> Constants.LOG.info("Effect on the entity " + entity.getDisplayName() + " has no start effect..."),
                entity -> Constants.LOG.info("Effect on the entity " + entity.getDisplayName() + " has no end effect...")
        );
    }

    public TriggerableEffect(int triggerSecond, int effectDurationInSeconds, Holder<Attribute> attribute, AttributeModifier modifier) {
        this(triggerSecond * 20, effectDurationInSeconds * 20,
                entity -> TriggerableEffect.addAttributeModifierToPlayer(entity, attribute, modifier),//(new ChainableEffect.AttributeTemplate(ResourceLocation.withDefaultNamespace(effectId), (double)2.0F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)).create(1)),
                entity -> TriggerableEffect.removeAttributeModifierToPlayer(entity, attribute, modifier)//(new ChainableEffect.AttributeTemplate(ResourceLocation.withDefaultNamespace(effectId), (double)2.0F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)).create(1))
        );
    }

    public TriggerableEffect(int triggerTicks, int effectDuration, Consumer<LivingEntity> effectStart, Consumer<LivingEntity> effectEnd) {
        this.triggerTicks = triggerTicks;
        this.effectDuration = effectDuration;
        this.effectStart = effectStart;
        this.effectEnd = effectEnd;
    }

    public int getEffectLastTick() {
        return this.triggerTicks + this.effectDuration;
    }

    public void applyEffectStart(LivingEntity player) {
        effectStart.accept(player);
    }

    public void applyEffectEnd(LivingEntity player) {
        effectEnd.accept(player);
    }

    private static void addAttributeModifierToPlayer(LivingEntity entity, Holder<Attribute> attribute, AttributeModifier modifier) {
        if(entity instanceof Player player) {
            AttributeInstance instance = player.getAttributes().getInstance(attribute);
            if (instance != null)
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

    public enum Lifecycle {
        PENDING,
        TRIGGERED,
        FINISHED
    }
}
