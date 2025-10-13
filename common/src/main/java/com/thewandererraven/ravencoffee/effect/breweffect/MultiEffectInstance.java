package com.thewandererraven.ravencoffee.effect.breweffect;

import net.minecraft.client.gui.Gui;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiEffectInstance {
    private int ticks;
    public final Holder<MultiEffect> multiEffect;
    private int finishedEffects = 0;
    private final List<TriggerableEffect.Lifecycle> stateOfSubEffects = new ArrayList<>();

    public MultiEffectInstance(Holder<MultiEffect> effect) {
        this.multiEffect = effect;
        this.ticks = 0;
        this.stateOfSubEffects.addAll(Collections.nCopies(multiEffect.value().getCount(), TriggerableEffect.Lifecycle.PENDING));
    }

    public boolean tick(LivingEntity player) {
        ticks++;
        for(int i = 0; i < stateOfSubEffects.size(); i++) {
            TriggerableEffect effect = this.multiEffect.value().getEffect(i);
            TriggerableEffect.Lifecycle currentState = this.stateOfSubEffects.get(i);
            if(ticks >= effect.triggerTicks && currentState.equals(TriggerableEffect.Lifecycle.PENDING)) {
                effect.applyEffectStart(player);
                this.stateOfSubEffects.set(i, TriggerableEffect.Lifecycle.TRIGGERED);
            }
            if(ticks >= effect.getEffectLastTick() && currentState.equals(TriggerableEffect.Lifecycle.TRIGGERED)) {
                effect.applyEffectEnd(player);
                finishedEffects++;
                this.stateOfSubEffects.set(i, TriggerableEffect.Lifecycle.FINISHED);
            }
        }
        return finishedEffects >= this.multiEffect.value().getCount();
    }

    public Holder<MultiEffect> getEffect() {
        return this.multiEffect;
    }

    public boolean isAmbient() {
        return false;
    }

    public int getRemainingDuration() {
        return this.multiEffect.value().totalDuration - this.ticks - 1;
    }

    public int getDuration() {
        return  this.ticks;
    }

    public boolean endsWithin(int duration) {
        return this.getRemainingDuration() <= duration;
    }

    public ResourceLocation getIconLocation() {
        return this.multiEffect.value().getId();
    }

    public void reset(LivingEntity player) {
        this.ticks = 0;
        this.resetAllEffects(player);
        this.stateOfSubEffects.replaceAll(state -> TriggerableEffect.Lifecycle.PENDING);
        this.finishedEffects = 0;
    }

    public void resetAllEffects(LivingEntity player) {
        for(int i = 0; i < this.multiEffect.value().getCount(); i++) {
            TriggerableEffect effect = this.multiEffect.value().getEffect(i);
            TriggerableEffect.Lifecycle currentState = this.stateOfSubEffects.get(i);
            if(currentState.equals(TriggerableEffect.Lifecycle.TRIGGERED)) {
                effect.applyEffectEnd(player);
                this.stateOfSubEffects.set(i, TriggerableEffect.Lifecycle.PENDING);
            }
        }
    }
}
