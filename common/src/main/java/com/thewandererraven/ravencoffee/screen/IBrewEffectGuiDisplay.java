package com.thewandererraven.ravencoffee.screen;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.effect.breweffect.MultiEffectInstance;
import com.thewandererraven.ravencoffee.platform.services.IBrewManagerHolder;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public interface IBrewEffectGuiDisplay {
    ResourceLocation MULTI_EFFECT_BACKGROUND_AMBIENT_SPRITE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/effect_background_ambient");
    ResourceLocation MULTI_EFFECT_BACKGROUND_SPRITE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/effect_background.png");


    static void renderMultiEffectIndicator(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
                    Minecraft mc = Minecraft.getInstance();
                    Player player = mc.player;
                    if (player == null) return;

                    if(player instanceof LocalPlayer localPlayer)
                        if(localPlayer instanceof IBrewManagerHolder holder) {
                            MultiEffectInstance current = holder.ravencoffee$getBrewEffectManager().getCurrentEffect();

                            if (current != null) {
                                ResourceLocation backgroundSprite = MULTI_EFFECT_BACKGROUND_SPRITE;
                                ResourceLocation icon = current.getIconLocation();
                                int duration = current.getRemainingDuration();

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
    }
}
