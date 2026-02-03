package com.thewandererraven.ravencoffee.effect.breweffect;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.networking.SyncBrewManagerPayload;
import com.thewandererraven.ravencoffee.networking.SyncBrewPayload;
import com.thewandererraven.ravencoffee.platform.Services;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class DefaultBrewEffectsManager implements IBrewEffectsManager {
    private List<CoffeeBrewEffectInstance> currentEffects = null;
    private final LivingEntity ownerEntity;
    private final int maxCaffeine = 30 * 20;
    private int currentCaffeine = 0;
    private boolean isOverloaded = false;

    public DefaultBrewEffectsManager(LivingEntity owner) {
        this.ownerEntity = owner;
        this.currentEffects = new ArrayList<>();
    }

    @Override
    public boolean add(CoffeeBrewEffectInstance instance) {
        if(!this.isOverloaded) {
            CoffeeBrewEffectInstance foundEffect = currentEffects.stream().filter( eff -> eff.multiEffect.value().equals(instance.multiEffect.value())).findFirst().orElse(null);
            if(foundEffect == null) {
                this.currentEffects.add(instance);
            } else {
                foundEffect.reset(this.ownerEntity);
            }
            this.addCaffeine(instance.getCaffeineContent());
            if(this.currentCaffeine >= this.maxCaffeine) {
                // TODO: Here goes te drawback of
                Constants.LOG.info("CAFFEINE OVERLOAD!");
                this.isOverloaded = true;
            }
            if (ownerEntity instanceof ServerPlayer serverPlayer) {
                Services.PLATFORM.sendCustomPacket(serverPlayer, new SyncBrewPayload(instance.getEffectId(), instance.getRemainingDuration()));
                Services.PLATFORM.sendCustomPacket(serverPlayer, new SyncBrewManagerPayload(this.currentCaffeine, this.isOverloaded));
            }
            return true;
        }
        return false;
    }

    public void setClientEffects(List<CoffeeBrewEffectInstance> list) {
        this.currentEffects = list;
    }

    public void setClientEffect(CoffeeBrewEffectInstance newEffect, int remainingTicks) {
        int foundIndex = -1;
        for(int i = 0; i < this.currentEffects.size(); i++) {
            if(this.currentEffects.get(i).getEffectId().equals(newEffect.getEffectId()))
                foundIndex = i;
        }
        if(foundIndex >= 0)
            this.setClientEffect(newEffect, foundIndex, remainingTicks);
        else
            this.add(newEffect);
    }

    public void setClientEffect(CoffeeBrewEffectInstance newEffect, int index, int remainingTicks) {
        if(remainingTicks >= 0) {
            this.currentEffects.set(index, newEffect);
            this.currentEffects.get(index).setCurrentTicksWithRemainingDuration(remainingTicks);
        } else
            this.currentEffects.remove(index);
    }

    public boolean isAnyEffectInAFullSecond() {
        for(CoffeeBrewEffectInstance instance : this.currentEffects) {
            if(instance.getRemainingDuration() % 20 == 0)
                return true;
        }
        return false;
    }

    @Override
    public void tick() {
        if (ownerEntity instanceof ServerPlayer serverPlayer) {
            if(this.currentCaffeine > 0) {
                this.currentCaffeine -= 1;
                Services.PLATFORM.sendCustomPacket(serverPlayer, new SyncBrewManagerPayload(this.currentCaffeine, this.isOverloaded));
            } else if(this.isOverloaded) {
                this.isOverloaded = false;
                Services.PLATFORM.sendCustomPacket(serverPlayer, new SyncBrewManagerPayload(this.currentCaffeine, this.isOverloaded));
            }
            if(!this.currentEffects.isEmpty()) {
                if(this.isAnyEffectInAFullSecond()) {
                    for(CoffeeBrewEffectInstance instance : this.currentEffects) {
                        Services.PLATFORM.sendCustomPacket(serverPlayer, new SyncBrewPayload(instance.getEffectId(), instance.getRemainingDuration()));
                    }
                }
                List<CoffeeBrewEffectInstance> finishedEffects = new ArrayList<>();
                for(CoffeeBrewEffectInstance instance : this.currentEffects) {
                    if (instance.tick(ownerEntity)) {
                        finishedEffects.add(instance);
                        Services.PLATFORM.sendCustomPacket(serverPlayer, new SyncBrewPayload(instance.getEffectId(), instance.getRemainingDuration()));
                    }
                }
                this.currentEffects.removeAll(finishedEffects);
            }
        }
    }

    @Override
    public List<CoffeeBrewEffectInstance> getCurrentEffects() {
//        return Collections.unmodifiableList(currentEffect);
        return this.currentEffects;
    }

    @Override
    public int getCurrentCaffeine() {
        return this.currentCaffeine;
    }

    @Override
    public void setCurrentCaffeine(int newValue) {
        this.currentCaffeine = newValue;
    }

    public void addCaffeine(int addedCaffeine) {
        if(this.ownerEntity instanceof Player player)
            if(!player.getAbilities().instabuild)
                this.setCurrentCaffeine(Math.min(this.currentCaffeine + addedCaffeine, this.maxCaffeine));
    }

    @Override
    public int getCurrentCaffeinePercentage() {
        return Math.min(((this.currentCaffeine * 100) / this.maxCaffeine), 100);
    }

    @Override
    public boolean getOverloadStatus() {
        return this.isOverloaded;
    }

    @Override
    public void setOverloaded(boolean newValue) {
        this.isOverloaded = newValue;
    }

//    public DefaultBrewEffectsManager copy() {
//        DefaultBrewEffectsManager copy = new DefaultBrewEffectsManager(this.player);
//        copy.currentEffect = this.currentEffect;
//        return copy;
//    }
}
