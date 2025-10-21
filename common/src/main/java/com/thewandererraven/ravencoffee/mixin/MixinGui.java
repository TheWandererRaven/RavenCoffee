package com.thewandererraven.ravencoffee.mixin;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.effect.breweffect.MultiEffectInstance;
import com.thewandererraven.ravencoffee.platform.services.IBrewManagerHolder;
import com.thewandererraven.ravencoffee.util.IMultiEffectIndicator;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(Gui.class)
public class MixinGui implements IMultiEffectIndicator {
    @Shadow
    private LayeredDraw layers;
    private static final ResourceLocation MULTI_EFFECT_BACKGROUND_AMBIENT_SPRITE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/effect_background_ambient");
    private static final ResourceLocation MULTI_EFFECT_BACKGROUND_SPRITE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/effect_background.png");

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/LayeredDraw;add(Lnet/minecraft/client/gui/LayeredDraw;Ljava/util/function/BooleanSupplier;)Lnet/minecraft/client/gui/LayeredDraw;"
            )
    )
    private LayeredDraw onInit(LayeredDraw instance, LayeredDraw layeredDraw, BooleanSupplier renderInner) {
        Gui self = (Gui)(Object)this;
        this.layers.add((guiGraphics, deltaTracker) -> {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player == null) return;

            if(player instanceof LocalPlayer localPlayer)
                if(localPlayer instanceof IBrewManagerHolder holder) {
                    MultiEffectInstance current = holder.ravencoffee$getBrewEffectManager().getCurrentEffect();

                    if (current != null) {
                        //ResourceLocation icon = current.getIconLocation();
                        ResourceLocation icon = MULTI_EFFECT_BACKGROUND_SPRITE;
                        int duration = current.getRemainingDuration();
                        Constants.LOG.info("Current duration ticks from gui class: " + duration);
                        // Example: draw it like vanilla potions
                        guiGraphics.blit(RenderType::guiTextured, icon, 10, 10, 0, 0, 18, 18, 18, 18);

                        // optionally draw remaining duration
                        guiGraphics.drawString(mc.font, String.valueOf((int) Math.ceil(duration / 20.0)), 10, 28, 0xFFFFFF, true);
                    }
                }
        });
        return instance.add(layeredDraw, renderInner);
    }

    @Override
    public void ravencoffee$renderMultiEffectIndicator(GuiGraphics graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        if(player instanceof IBrewManagerHolder holder) {
            MultiEffectInstance current = holder.ravencoffee$getBrewEffectManager().getCurrentEffect();

            if (current != null) {
                ResourceLocation icon = current.getIconLocation();
                int duration = current.getRemainingDuration();
                // Example: draw it like vanilla potions
                graphics.blit(RenderType::guiTextured, icon, 10, 10, 0, 0, 18, 18, 18, 18);

                // optionally draw remaining duration
                graphics.drawString(mc.font, String.valueOf(duration / 20), 10, 28, 0xFFFFFF, true);
            }
        }
    }
}
