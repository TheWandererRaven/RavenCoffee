package com.thewandererraven.ravencoffee.screen;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.platform.services.IBrewManagerHolder;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class BrewGuiDisplay {
    ResourceLocation MULTI_EFFECT_BACKGROUND_AMBIENT_SPRITE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/effect_background_ambient");
    ResourceLocation MULTI_EFFECT_BACKGROUND_SPRITE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/effect_background.png");
    ResourceLocation CAFFEINE_CONTENT_BAR = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/caffeine_level.png");
    ResourceLocation OVERLOAD_INDICATOR = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/caffeine_oveliad_indicator.png");

    public List<ResourceLocation> effectIcons = List.of();
    int currentEffectDurationSeconds = 0;
    int brewTotalDurationSeconds = 0;
    int caffeinePercentage = 0;
    boolean isCaffeineOverloaded = false;

    public void setEffectIcons(List<ResourceLocation> icons) {
        this.effectIcons = icons;
    }

    public void setCurrentEffectDurationSeconds(int seconds)
    {
        this.currentEffectDurationSeconds = seconds;
    }

    public void setBrewTotalDurationSeconds(int seconds)
    {
        this.brewTotalDurationSeconds = seconds;
    }
    public void setCaffeinePercentage(int percentage)
    {
        this.caffeinePercentage = percentage;
    }

    public void setCaffeineOverload(boolean isOverloaded)
    {
        this.isCaffeineOverloaded = isOverloaded;
    }

    public void renderMultiEffectIndicator(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        if(player instanceof LocalPlayer) {
            int ZONE_STARTING_POS_X = 10;
            int ZONE_STARTING_POS_Y = 10;
            if (this.brewTotalDurationSeconds > 0) {
                for (int i = 0; i < this.effectIcons.size(); i++) {
                    ResourceLocation effectIcon = this.effectIcons.get(i);
                    ResourceLocation backgroundSprite = this.MULTI_EFFECT_BACKGROUND_SPRITE;
                    int WIDGET_WIDTH = 32;
                    int WIDGET_HEIGHT = 32;
                    if (i == 0) {
                        WIDGET_WIDTH = 40;
                        WIDGET_HEIGHT = 40;
                    }

                    int WIDGET_POS_X = ZONE_STARTING_POS_X + ((WIDGET_WIDTH + 10) * i);
                    int WIDGET_POS_Y = ZONE_STARTING_POS_Y;
                    guiGraphics.blit(RenderType::guiTextured, backgroundSprite, WIDGET_POS_X, WIDGET_POS_Y, 0, 0, WIDGET_WIDTH, WIDGET_HEIGHT, WIDGET_WIDTH, WIDGET_HEIGHT);
                    guiGraphics.blit(RenderType::guiTextured, effectIcon, WIDGET_POS_X + 4, WIDGET_POS_Y + 4, 0, 0, WIDGET_WIDTH - 8, WIDGET_HEIGHT - 8, WIDGET_WIDTH - 8, WIDGET_HEIGHT - 8);

                }
                String remainingDuration = String.format("%02d:%02d", (int) Math.floor((this.currentEffectDurationSeconds / 60.0)), (int) (this.currentEffectDurationSeconds % 60.0));
                if (this.effectIcons.size() > 1)
                    remainingDuration = String.format("%s of -> T:%02d:%02d", remainingDuration, (int) Math.floor((this.brewTotalDurationSeconds / 60.0)), (int) (this.brewTotalDurationSeconds % 60.0));
                guiGraphics.drawString(mc.font, remainingDuration, ZONE_STARTING_POS_X + 3, ZONE_STARTING_POS_Y + 32, 0xFFFFFF, true);
            }

            int CAFFEINE_BAR_WIDTH = 32;
            int CAFFEINE_BAR_HEIGHT = 32;
            if (this.caffeinePercentage > 0) {
                int currentHeight = Math.round(CAFFEINE_BAR_HEIGHT * ((float) this.caffeinePercentage / 100));
                guiGraphics.blit(RenderType::guiTextured, this.CAFFEINE_CONTENT_BAR, ZONE_STARTING_POS_X, ZONE_STARTING_POS_Y + 40 + (CAFFEINE_BAR_HEIGHT - currentHeight), 0, CAFFEINE_BAR_HEIGHT - currentHeight, CAFFEINE_BAR_WIDTH, currentHeight, CAFFEINE_BAR_WIDTH, CAFFEINE_BAR_HEIGHT);
            }
            if (this.isCaffeineOverloaded) {
                guiGraphics.blit(RenderType::guiTextured, this.OVERLOAD_INDICATOR, ZONE_STARTING_POS_X, ZONE_STARTING_POS_Y + 40, 0, 0, CAFFEINE_BAR_WIDTH, CAFFEINE_BAR_HEIGHT, CAFFEINE_BAR_WIDTH, CAFFEINE_BAR_HEIGHT);
            }
        }
    }
}
