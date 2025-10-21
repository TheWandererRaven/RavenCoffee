package com.thewandererraven.ravencoffee.util;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface IMultiEffectIndicator {
    public void ravencoffee$renderMultiEffectIndicator(GuiGraphics graphics, DeltaTracker deltaTracker, CallbackInfo ci);
}
