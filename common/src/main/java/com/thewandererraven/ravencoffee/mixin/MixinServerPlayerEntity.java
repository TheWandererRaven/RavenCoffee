package com.thewandererraven.ravencoffee.mixin;

import com.mojang.datafixers.util.Either;
import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.platform.services.IBrewManagerHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
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
        ((IBrewManagerHolder)this).ravencoffee$getBrewEffectManager().tick();
    }

    @Inject(method = "startSleepInBed", at = @At("HEAD"), cancellable = true)
    private void ravenCoffee$validateSleep(BlockPos at, CallbackInfoReturnable<Either<Player.BedSleepingProblem, Unit>> ret) {
        Constants.LOG.info("Current Caffeine: " + ((IBrewManagerHolder)this).ravencoffee$getBrewEffectManager().getCurrentCaffeine());
        if(((IBrewManagerHolder)this).ravencoffee$getBrewEffectManager().getCurrentCaffeine() > 0) {
            ret.setReturnValue(
                    Either.left(Player.BedSleepingProblem.OTHER_PROBLEM)
            );
            ((ServerPlayer)(Object)this).displayClientMessage(Component.translatable("block.ravencoffee.bed.caffeinated"), true);
        }
    }
}
