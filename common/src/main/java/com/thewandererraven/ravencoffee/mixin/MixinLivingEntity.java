package com.thewandererraven.ravencoffee.mixin;

import com.thewandererraven.ravenbrewslib.brew.effect.IBrewEffectManagerHolder;
import com.thewandererraven.ravencoffee.brew.DefaultCoffeeBrewEffectsManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void ravenCoffee$initBrewEffectsManager(EntityType<? extends LivingEntity> entityType, Level level, CallbackInfo ci) {
        ((IBrewEffectManagerHolder)this).ravenbrewslib$setBrewEffectManager(new DefaultCoffeeBrewEffectsManager((LivingEntity)(Object)this));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void ravenCoffee$addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        CompoundTag playerDataTag = ((IBrewEffectManagerHolder)this).ravenbrewslib$getBrewEffectManager().serializeNBT();
        tag.put("RavenCoffeePlayerEffectData", playerDataTag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void ravenCoffee$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("RavenCoffeePlayerEffectData")) {
            CompoundTag playerDataTag = tag.getCompound("RavenCoffeePlayerEffectData").get();
            ((IBrewEffectManagerHolder)this).ravenbrewslib$getBrewEffectManager().deserializeNBT(playerDataTag);
        }
    }

    @Inject(method = "removeAllEffects", at = @At("HEAD"))
    private void ravenCoffee$removeAllEffects(CallbackInfoReturnable<Boolean> ret) {
        IBrewEffectManagerHolder holder = (IBrewEffectManagerHolder)this;
        if (!((LivingEntity)(Object)this).level().isClientSide && !holder.ravenbrewslib$getBrewEffectManager().isEmpty())
            holder.ravenbrewslib$getBrewEffectManager().clearEffects();
    }
}
