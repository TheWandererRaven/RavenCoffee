package com.thewandererraven.ravencoffee;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.thewandererraven.ravencoffee.datagen.DataGenDefinitions;
import com.thewandererraven.ravencoffee.datagen.DataGenItem;
import com.thewandererraven.ravencoffee.effect.breweffect.MultiEffectInstance;
import com.thewandererraven.ravencoffee.effect.breweffect.MultiEffectsRegistry;
import com.thewandererraven.ravencoffee.menu.MenusRegistry;
import com.thewandererraven.ravencoffee.networking.SyncBrewPayload;
import com.thewandererraven.ravencoffee.platform.services.IBrewManagerHolder;
import com.thewandererraven.ravencoffee.screen.CoffeeGrinderScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.ComposterBlock;

public class RavenCoffeeFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(MenusRegistry.COFFEE_GRINDER.get(), CoffeeGrinderScreen::new);
        // TODO: If fabric implements datamaps for this, move this to its corresponding class
        for(DataGenItem dataGenItem : DataGenDefinitions.ITEMS)
            if(dataGenItem.compostableValue > 0.0f)
                ComposterBlock.COMPOSTABLES.put(dataGenItem.mainItem, dataGenItem.compostableValue);
        ClientPlayNetworking.registerGlobalReceiver(
                SyncBrewPayload.TYPE,
                (payload, context) -> {
                    Minecraft client = Minecraft.getInstance();
                    client.execute(() -> {
                        Player player = client.player;
                        IBrewManagerHolder holder = (IBrewManagerHolder) player;
                        if(payload.brewId() != null) {
                        var holderEffect = MultiEffectsRegistry.BREW_EFFECTS.getEntries().stream()
                                .filter(entry -> entry.get().getId().equals(payload.brewId()))
                                .findFirst().orElse(null);
                        if (holderEffect == null) return;

                        MultiEffectInstance instance = new MultiEffectInstance(holderEffect.asHolder());
                        holder.ravencoffee$getBrewEffectManager()
                                .setClientEffect(instance, payload.duration());
                        }
                    });
                }
        );
//        HudRenderCallback.EVENT.register((guiGraphics, listener) -> {
//            Minecraft minecraft = Minecraft.getInstance();
//            if(minecraft.player == null) return;
//            g
//        });
    }
}
