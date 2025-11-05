package com.thewandererraven.ravencoffee.effect.breweffect;


import com.thewandererraven.ravencoffee.Constants;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class AttributeModifierEffect extends TriggerableEffect {
    public Consumer<LivingEntity> effectStart;
    public Consumer<LivingEntity> effectEnd;

    public AttributeModifierEffect(int triggerTicks, int effectDuration) {
        this(triggerTicks, effectDuration,
                entity -> Constants.LOG.info("Effect on the entity " + entity.getDisplayName() + " has no start effect..."),
                entity -> Constants.LOG.info("Effect on the entity " + entity.getDisplayName() + " has no end effect...")
        );
    }

    public AttributeModifierEffect(int triggerTicks, int effectDuration, Consumer<LivingEntity> effectStart, Consumer<LivingEntity> effectEnd) {
        super(triggerTicks, effectDuration);
        this.effectStart = effectStart;
        this.effectEnd = effectEnd;
    }

    public static AttributeModifierEffect of(int triggerSecond, int effectDurationInSeconds, Holder<Attribute> attribute, AttributeModifier modifier) {
        return new AttributeModifierEffect(triggerSecond * 20, effectDurationInSeconds * 20,
                entity -> AttributeModifierEffect.addAttributeModifierToPlayer(entity, attribute, modifier),//(new ChainableEffect.AttributeTemplate(ResourceLocation.withDefaultNamespace(effectId), (double)2.0F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)).create(1)),
                entity -> AttributeModifierEffect.removeAttributeModifierToPlayer(entity, attribute, modifier)//(new ChainableEffect.AttributeTemplate(ResourceLocation.withDefaultNamespace(effectId), (double)2.0F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)).create(1))
        );
    }

    @Override
    public Lifecycle applyEffectStart(LivingEntity player) {
        effectStart.accept(player);
        return Lifecycle.TRIGGERED;
    }

    @Override
    public Lifecycle applyEffectEnd(LivingEntity player) {
        effectEnd.accept(player);
        return Lifecycle.FINISHED;
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

}
