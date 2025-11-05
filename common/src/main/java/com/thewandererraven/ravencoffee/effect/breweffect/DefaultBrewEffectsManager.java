package com.thewandererraven.ravencoffee.effect.breweffect;

import com.thewandererraven.ravencoffee.networking.SyncBrewPayload;
import com.thewandererraven.ravencoffee.platform.Services;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class DefaultBrewEffectsManager implements IBrewEffectsManager {
    private CoffeeBrewEffectInstance currentEffect = null;
    private LivingEntity player;

    public DefaultBrewEffectsManager(LivingEntity owner) {
        this.player = owner;
    }

    @Override
    public void add(CoffeeBrewEffectInstance instance) {
        if(currentEffect == null)
            this.currentEffect = instance;
        if(currentEffect.multiEffect == instance.multiEffect)
            this.currentEffect.reset(player);
        if(player instanceof ServerPlayer serverPlayer) {
            Services.PLATFORM.sendCustomPacket(serverPlayer, new SyncBrewPayload(currentEffect.getEffect().value().getId(), currentEffect.getRemainingDuration()));
        }
    }

    public void setClientEffect(CoffeeBrewEffectInstance instance, int remainingTicks) {
        if(remainingTicks > 0)
            this.currentEffect = instance;
        else
            this.currentEffect = null;
        if(this.currentEffect != null)
            instance.setCurrentTicksWithRemainingDuration(remainingTicks);
    }

    @Override
    public void tick() {
        if(currentEffect != null) {
            if (player instanceof ServerPlayer serverPlayer) {
                int remainingDuration = currentEffect.getRemainingDuration();
                if(remainingDuration % 20 == 0) {
                    Services.PLATFORM.sendCustomPacket(serverPlayer, new SyncBrewPayload(currentEffect.getEffect().value().getId(), remainingDuration));
                }
                if (currentEffect.tick(player)) {
                    CoffeeBrewEffectInstance finishedEffect = currentEffect;
                    currentEffect = null;
                    Services.PLATFORM.sendCustomPacket(serverPlayer, new SyncBrewPayload(finishedEffect.getEffect().value().getId(), remainingDuration));
                }
            }
        }
    }

    @Override
    public CoffeeBrewEffectInstance getCurrentEffect() {
//        return Collections.unmodifiableList(currentEffect);
        return currentEffect;
    }

    public DefaultBrewEffectsManager copy() {
        DefaultBrewEffectsManager copy = new DefaultBrewEffectsManager(this.player);
        copy.currentEffect = this.currentEffect;
        return copy;
    }
}
