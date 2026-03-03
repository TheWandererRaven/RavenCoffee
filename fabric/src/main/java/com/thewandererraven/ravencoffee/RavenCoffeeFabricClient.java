package com.thewandererraven.ravencoffee;

import com.thewandererraven.ravencoffee.block.CoffeeBrewingStation;
import com.thewandererraven.ravencoffee.datagen.DataGenDefinitions;
import com.thewandererraven.ravencoffee.datagen.DataGenItem;
import com.thewandererraven.ravencoffee.item.properties.BrewVariantProperty;
import com.thewandererraven.ravencoffee.menu.MenusRegistry;
import com.thewandererraven.ravencoffee.networking.SyncBrewManagerCaffeinePayload;
import com.thewandererraven.ravencoffee.networking.SyncBrewManagerDurationPayload;
import com.thewandererraven.ravencoffee.networking.SyncBrewManagerIconsPayload;
import com.thewandererraven.ravencoffee.platform.services.IBrewManagerHolder;
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
                SyncBrewManagerCaffeinePayload.TYPE,
                (payload, context) -> {
                    Minecraft client = Minecraft.getInstance();
                    client.execute(() -> {
                        Player player = client.player;
                        IBrewManagerHolder holder = (IBrewManagerHolder) player;
                        if(payload.currentCaffeine() >= 0) {
                            holder.ravencoffee$getBrewEffectManager().setCurrentCaffeine(payload.currentCaffeine());
                        }
                        holder.ravencoffee$getBrewEffectManager().setOverloaded(payload.isOverloaded());
                    });
                }
        );
        ClientPlayNetworking.registerGlobalReceiver(
                SyncBrewManagerDurationPayload.TYPE,
                (payload, context) -> {
                    Minecraft client = Minecraft.getInstance();
                    client.execute(() -> {
                        Player player = client.player;
                        IBrewManagerHolder holder = (IBrewManagerHolder) player;
                        holder.ravencoffee$getBrewEffectManager().setCurrentEffectRemainingTicks(payload.currentEffectRemainingTicks());
                        holder.ravencoffee$getBrewEffectManager().setTotalRemainingTicks(payload.totalEffectRemainingTicks());
                    });
                }
        );
        ClientPlayNetworking.registerGlobalReceiver(
                SyncBrewManagerIconsPayload.TYPE,
                (payload, context) -> {
                    Minecraft client = Minecraft.getInstance();
                    client.execute(() -> {
                        Player player = client.player;
                        IBrewManagerHolder holder = (IBrewManagerHolder) player;
                        holder.ravencoffee$getBrewEffectManager().setEffectIcons(payload.effectsIcons());
                    });
                }
        );
        SelectItemModelProperties.ID_MAPPER.put(
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "brew_variant"),
                BrewVariantProperty.TYPE
        );
    }
}
