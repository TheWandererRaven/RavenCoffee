package com.thewandererraven.ravencoffee.screen;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.effect.breweffect.CoffeeBrewEffectInstance;
import com.thewandererraven.ravencoffee.platform.services.IBrewManagerHolder;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public interface IBrewEffectGuiDisplay {
    ResourceLocation MULTI_EFFECT_BACKGROUND_AMBIENT_SPRITE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/effect_background_ambient");
    ResourceLocation MULTI_EFFECT_BACKGROUND_SPRITE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/effect_background.png");
    ResourceLocation CAFFEINE_CONTENT_BAR = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/caffeine_level.png");
    ResourceLocation OVERLOAD_INDICATOR = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/caffeine_oveliad_indicator.png");


    static void renderMultiEffectIndicator(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
                    Minecraft mc = Minecraft.getInstance();
                    Player player = mc.player;
                    if (player == null) return;

                    if(player instanceof LocalPlayer localPlayer)
                        if(localPlayer instanceof IBrewManagerHolder holder) {
                            List<CoffeeBrewEffectInstance> currentEffects = holder.ravencoffee$getBrewEffectManager().getCurrentEffects();

                            int ZONE_STARTING_POS_X = 10;
                            int ZONE_STARTING_POS_Y = 10;

                            for(int i = 0; i < currentEffects.size(); i++) {
                                CoffeeBrewEffectInstance effectInstance = currentEffects.get(i);
                                ResourceLocation backgroundSprite = MULTI_EFFECT_BACKGROUND_SPRITE;
                                ResourceLocation icon = effectInstance.getIconLocation();
                                int duration = effectInstance.getRemainingDuration();
                                int WIDGET_WIDTH = 32;
                                int WIDGET_HEIGHT = 32;

                                int WIDGET_POS_X = ZONE_STARTING_POS_X + ((WIDGET_WIDTH + 10) * i);
                                int WIDGET_POS_Y = ZONE_STARTING_POS_Y;
                                guiGraphics.blit(RenderType::guiTextured, backgroundSprite, WIDGET_POS_X, WIDGET_POS_Y, 0, 0, WIDGET_WIDTH, WIDGET_HEIGHT, WIDGET_WIDTH, WIDGET_HEIGHT);
                                guiGraphics.blit(RenderType::guiTextured, icon, WIDGET_POS_X + 4, WIDGET_POS_Y + 4, 0, 0, WIDGET_WIDTH - 8, WIDGET_HEIGHT - 8, WIDGET_WIDTH - 8, WIDGET_HEIGHT - 8);

                                int remainingSeconds = (int) Math.ceil(duration / 20.0);
                                String timeLeft = String.format("%02d:%02d", (int) Math.floor((remainingSeconds / 60.0)), (int) (remainingSeconds % 60.0));
                                guiGraphics.drawString(mc.font, timeLeft, WIDGET_POS_X + 3, WIDGET_POS_Y + 32, 0xFFFFFF, true);
                            }

                            int caffeinePercentage = holder.ravencoffee$getBrewEffectManager().getCurrentCaffeinePercentage();

                            boolean isOverloaded = holder.ravencoffee$getBrewEffectManager().getOverloadStatus();
                            int CAFFEINE_BAR_WIDTH = 32;
                            int CAFFEINE_BAR_HEIGHT = 32;
                            if(caffeinePercentage > 0) {
                                int currentHeight = Math.round(CAFFEINE_BAR_HEIGHT * ((float)caffeinePercentage/100));
                                guiGraphics.blit(RenderType::guiTextured, CAFFEINE_CONTENT_BAR, ZONE_STARTING_POS_X, ZONE_STARTING_POS_Y + 40 + (CAFFEINE_BAR_HEIGHT - currentHeight), 0, CAFFEINE_BAR_HEIGHT - currentHeight, CAFFEINE_BAR_WIDTH, currentHeight, CAFFEINE_BAR_WIDTH, CAFFEINE_BAR_HEIGHT);
                            }
                            if(isOverloaded) {
                                guiGraphics.blit(RenderType::guiTextured, OVERLOAD_INDICATOR, ZONE_STARTING_POS_X, ZONE_STARTING_POS_Y + 40, 0, 0, CAFFEINE_BAR_WIDTH, CAFFEINE_BAR_HEIGHT, CAFFEINE_BAR_WIDTH, CAFFEINE_BAR_HEIGHT);
                            }
                        }
    }
}
