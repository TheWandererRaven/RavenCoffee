package com.thewandererraven.ravencoffee.mixin;

import com.mojang.datafixers.util.Either;
import com.thewandererraven.ravenbrewslib.brew.effect.IBrewEffectManagerHolder;
import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.brew.DefaultCoffeeBrewEffectsManager;
import com.thewandererraven.ravencoffee.util.CoffeeBrewEffectsUtils;
import com.thewandererraven.ravencoffee.util.RavenCoffeeGeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public class MixinServerPlayerEntity {
    @Inject(method = "tick", at = @At("TAIL"))
    private void ravencoffee$tickBrewEffect(CallbackInfo ci) {
        ((IBrewEffectManagerHolder)this).ravenbrewslib$getBrewEffectManager().tick();
    }

    @Inject(method = "startSleepInBed", at = @At("HEAD"), cancellable = true)
    private void ravenCoffee$validateSleep(BlockPos at, CallbackInfoReturnable<Either<Player.BedSleepingProblem, Unit>> ret) {
        DefaultCoffeeBrewEffectsManager effManager = RavenCoffeeGeneralUtils.getCastCoffeeBrewEffectsManager(this);
        Constants.LOG.info("Current Caffeine: " + effManager.getCurrentCaffeine());
        if(effManager.getCurrentCaffeine() > 0) {
            ret.setReturnValue(
                    Either.left(Player.BedSleepingProblem.OTHER_PROBLEM)
            );
            ((ServerPlayer)(Object)this).displayClientMessage(Component.translatable("block.ravencoffee.bed.caffeinated"), true);
        }
    }
}
