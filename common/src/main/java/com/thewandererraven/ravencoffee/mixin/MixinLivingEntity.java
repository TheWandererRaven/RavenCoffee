package com.thewandererraven.ravencoffee.mixin;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.effect.breweffect.DefaultBrewEffectsManager;
import com.thewandererraven.ravencoffee.platform.services.IBrewManagerHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity implements IBrewManagerHolder {
    private final DefaultBrewEffectsManager brewEffectsManager = new DefaultBrewEffectsManager((LivingEntity)(Object)this);

    @Override
    public DefaultBrewEffectsManager ravencoffee$getBrewEffectManager() {
        return brewEffectsManager;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void ravenCoffee$addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        CompoundTag playerDataTag = ravencoffee$getBrewEffectManager().serializeNBT();
        tag.put("RavenCoffeePlayerEffectData", playerDataTag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void ravenCoffee$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("RavenCoffeePlayerEffectData")) {
            CompoundTag playerDataTag = tag.getCompound("RavenCoffeePlayerEffectData").get();
            ravencoffee$getBrewEffectManager().deserializeNBT(playerDataTag);
        }
    }

    @Inject(method = "removeAllEffects", at = @At("HEAD"))
    private void raven_coffee$removeAllEffects(CallbackInfoReturnable<Boolean> ret) {
        if (!((LivingEntity)(Object)this).level().isClientSide && !ravencoffee$getBrewEffectManager().isEmpty())
            ravencoffee$getBrewEffectManager().clearEffects();
    }
}
