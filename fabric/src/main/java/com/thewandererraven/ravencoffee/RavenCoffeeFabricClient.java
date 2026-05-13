package com.thewandererraven.ravencoffee;

import com.thewandererraven.ravencoffee.datagen.DataGenDefinitions;
import com.thewandererraven.ravencoffee.datagen.DataGenItem;
import com.thewandererraven.ravencoffee.item.properties.BrewVariantProperty;
import com.thewandererraven.ravencoffee.menu.MenusRegistry;
import com.thewandererraven.ravencoffee.networking.*;
import com.thewandererraven.ravencoffee.platform.services.IBrewGuiDisplayHolder;
import com.thewandererraven.ravencoffee.platform.services.IBrewManagerHolder;
import com.thewandererraven.ravencoffee.screen.BrewGuiDisplay;
import com.thewandererraven.ravencoffee.screen.CoffeeBrewingStationScreen;
import com.thewandererraven.ravencoffee.screen.CoffeeGrinderScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.ComposterBlock;

public class RavenCoffeeFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(MenusRegistry.COFFEE_GRINDER.get(), CoffeeGrinderScreen::new);
        MenuScreens.register(MenusRegistry.COFFEE_BREWING_STATION.get(), CoffeeBrewingStationScreen::new);
        // TODO: If fabric implements datamaps for this, move this to its corresponding class
        for(DataGenItem dataGenItem : DataGenDefinitions.ITEMS)
            if(dataGenItem.compostableValue > 0.0f)
                ComposterBlock.COMPOSTABLES.put(dataGenItem.mainItem, dataGenItem.compostableValue);
        ClientPlayNetworking.registerGlobalReceiver(
                SyncBrewGuiDisplayCaffeinePayload.TYPE,
                (payload, context) -> {
                    context.client().execute(() -> {
                        IBrewGuiDisplayHolder holder = (IBrewGuiDisplayHolder) context.client().gui;
                        if(payload.caffeinePercentage() >= 0) {
                            holder.ravencoffee$getBrewGuiDisplayHolder().setCaffeinePercentage(payload.caffeinePercentage());
                        }
                        holder.ravencoffee$getBrewGuiDisplayHolder().setCaffeineOverload(payload.isOverloaded());
                    });
                }
        );
        ClientPlayNetworking.registerGlobalReceiver(
                SyncBrewGuiDisplayDurationsPayload.TYPE,
                (payload, context) -> {
                    context.client().execute(() -> {
                        BrewGuiDisplay effDisplay = ((IBrewGuiDisplayHolder) context.client().gui).ravencoffee$getBrewGuiDisplayHolder();
                        effDisplay.setCurrentEffectDurationSeconds(payload.currentEffectRemainingSeconds());
                        effDisplay.setBrewTotalDurationSeconds(payload.brewTotalRemainingSeconds());
                    });
                }
        );
        ClientPlayNetworking.registerGlobalReceiver(
                SyncBrewGuiDisplayIconsPayload.TYPE,
                (payload, context) -> {
                    context.client().execute(() -> {
                        BrewGuiDisplay effDisplay = ((IBrewGuiDisplayHolder) context.client().gui).ravencoffee$getBrewGuiDisplayHolder();
                        effDisplay.setEffectIcons(payload.effectIcons());
                    });
                }
        );
        SelectItemModelProperties.ID_MAPPER.put(
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "brew_variant"),
                BrewVariantProperty.TYPE
        );
    }
}
