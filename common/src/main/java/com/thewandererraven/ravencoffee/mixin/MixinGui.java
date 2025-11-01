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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(Gui.class)
public class MixinGui implements IMultiEffectIndicator {
    @Shadow
    private LayeredDraw layers;
    @Shadow @Final private Minecraft minecraft;
    private static final ResourceLocation MULTI_EFFECT_BACKGROUND_AMBIENT_SPRITE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/effect_background_ambient");
    private static final ResourceLocation MULTI_EFFECT_BACKGROUND_SPRITE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/effect_background.png");

//    @Redirect(
//            method = "<init>",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/gui/LayeredDraw;add(Lnet/minecraft/client/gui/LayeredDraw;Ljava/util/function/BooleanSupplier;)Lnet/minecraft/client/gui/LayeredDraw;"
//            )
//    )
//    private LayeredDraw onInit(LayeredDraw instance, LayeredDraw layeredDraw, BooleanSupplier renderInner) {
//        Gui self = (Gui)(Object)this;
//        this.layers.add((guiGraphics, deltaTracker) -> {
//            Minecraft mc = Minecraft.getInstance();
//            Player player = mc.player;
//            if (player == null) return;
//
//            if(player instanceof LocalPlayer localPlayer)
//                if(localPlayer instanceof IBrewManagerHolder holder) {
//                    MultiEffectInstance current = holder.ravencoffee$getBrewEffectManager().getCurrentEffect();
//
//                    if (current != null) {
//                        //ResourceLocation icon = current.getIconLocation();
//                        ResourceLocation backgroundSprite = MULTI_EFFECT_BACKGROUND_SPRITE;
//                        //ResourceLocation icon = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/item/coffee_mug/basic_brew.png");
//                        ResourceLocation icon = current.getIconLocation();
//                        int duration = current.getRemainingDuration();
//                        Constants.LOG.info("Current duration ticks from gui class: " + duration);
//
//                        int WIDGET_POS_X = 10;
//                        int WIDGET_POS_Y = 10;
//                        int WIDGET_WIDTH = 32;
//                        int WIDGET_HEIGHT = 32;
//                        guiGraphics.blit(RenderType::guiTextured, backgroundSprite, WIDGET_POS_X, WIDGET_POS_Y, 0, 0, WIDGET_WIDTH, WIDGET_HEIGHT, WIDGET_WIDTH, WIDGET_HEIGHT);
//                        guiGraphics.blit(RenderType::guiTextured, icon, WIDGET_POS_X + 4, WIDGET_POS_Y + 4, 0, 0, WIDGET_WIDTH - 8, WIDGET_HEIGHT - 8, WIDGET_WIDTH - 8, WIDGET_HEIGHT - 8);
//
//                        int remainingSeconds = (int) Math.ceil(duration / 20.0);
//                        String timeLeft = String.format("%02d:%02d", (int) Math.floor((remainingSeconds / 60.0)), (int) (remainingSeconds % 60.0));
//                        guiGraphics.drawString(mc.font, timeLeft, WIDGET_POS_X + 3, WIDGET_POS_Y + 32, 0xFFFFFF, true);
//                    }
//                }
//        });
//        return instance.add(layeredDraw, renderInner);
//    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(Minecraft mc, CallbackInfo ci) {
        LayeredDraw layereddraw2 = (new LayeredDraw())
                .add((guiGraphics, deltaTracker) -> {
                    Constants.LOG.info("From Gui mixin");
                    Player player = this.minecraft.player;
                    if (player == null) return;

                    if(player instanceof LocalPlayer localPlayer)
                        if(localPlayer instanceof IBrewManagerHolder holder) {
                        MultiEffectInstance current = holder.ravencoffee$getBrewEffectManager().getCurrentEffect();

                        if (current != null) {
                            //ResourceLocation icon = current.getIconLocation();
                            ResourceLocation backgroundSprite = MULTI_EFFECT_BACKGROUND_SPRITE;
                            //ResourceLocation icon = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/item/coffee_mug/basic_brew.png");
                            ResourceLocation icon = current.getIconLocation();
                            int duration = current.getRemainingDuration();
                            Constants.LOG.info("Current duration ticks from gui class: " + duration);

                            int WIDGET_POS_X = 10;
                            int WIDGET_POS_Y = 10;
                            int WIDGET_WIDTH = 32;
                            int WIDGET_HEIGHT = 32;
                            guiGraphics.blit(RenderType::guiTextured, backgroundSprite, WIDGET_POS_X, WIDGET_POS_Y, 0, 0, WIDGET_WIDTH, WIDGET_HEIGHT, WIDGET_WIDTH, WIDGET_HEIGHT);
                            guiGraphics.blit(RenderType::guiTextured, icon, WIDGET_POS_X + 4, WIDGET_POS_Y + 4, 0, 0, WIDGET_WIDTH - 8, WIDGET_HEIGHT - 8, WIDGET_WIDTH - 8, WIDGET_HEIGHT - 8);

                            int remainingSeconds = (int) Math.ceil(duration / 20.0);
                            String timeLeft = String.format("%02d:%02d", (int) Math.floor((remainingSeconds / 60.0)), (int) (remainingSeconds % 60.0));
                            guiGraphics.drawString(mc.font, timeLeft, WIDGET_POS_X + 3, WIDGET_POS_Y + 32, 0xFFFFFF, true);
                        }
                    }
        });
        //this.layers.add(layereddraw2, () -> !minecraft.options.hideGui);
        this.layerManager.add(layereddraw2, () -> !minecraft.options.hideGui);
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
