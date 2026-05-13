package com.thewandererraven.ravencoffee.mixin;

import com.thewandererraven.ravencoffee.platform.services.IBrewGuiDisplayHolder;
import com.thewandererraven.ravencoffee.screen.BrewGuiDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.LayeredDraw;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class MixinGui implements IBrewGuiDisplayHolder {
    private final BrewGuiDisplay brewGuiDisplay = new BrewGuiDisplay();
    @Shadow
    private LayeredDraw layers;
    @Shadow @Final
    private Minecraft minecraft;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(Minecraft mc, CallbackInfo ci) {
        this.layers.add(
                (new LayeredDraw()).add(
                        brewGuiDisplay::renderMultiEffectIndicator
                ), () -> !minecraft.options.hideGui
        );
    }

    @Override
    public BrewGuiDisplay ravencoffee$getBrewGuiDisplayHolder() {
        return brewGuiDisplay;
    }
}
