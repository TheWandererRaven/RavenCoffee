package com.thewandererraven.ravencoffee.mixin;

import com.thewandererraven.ravencoffee.effect.breweffect.DefaultBrewEffectsManager;
import com.thewandererraven.ravencoffee.platform.services.IBrewManagerHolder;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class MixinServerPlayerEntity {
    @Inject(method = "tick", at = @At("TAIL"))
    private void ravencoffee$tickBrewEffect(CallbackInfo ci) {
        ((IBrewManagerHolder)this).ravencoffee$getBrewEffectManager().tick();
    }
}
