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

public class SingleEffect extends TriggerableEffect {
    public int triggerTick;
    public int effectDuration;
    public Consumer<LivingEntity> effect;

    public SingleEffect(int triggerTicks, int effectDuration) {
        this(triggerTicks, effectDuration,
                entity -> Constants.LOG.info("Effect on the entity " + entity.getDisplayName() + " has no start effect...")
        );
    }

    public SingleEffect(int triggerTick, int effectDuration, Consumer<LivingEntity> effectStart) {
        super(triggerTick, effectDuration);
        this.effect = effectStart;
    }

    public int getEffectLastTick() {
        return this.triggerTick + this.effectDuration;
    }

    @Override
    public Lifecycle applyEffectStart(LivingEntity player) {
        effect.accept(player);
        return Lifecycle.FINISHED;
    }

    @Override
    public Lifecycle applyEffectEnd(LivingEntity player) {
        return Lifecycle.FINISHED;
    }
}
