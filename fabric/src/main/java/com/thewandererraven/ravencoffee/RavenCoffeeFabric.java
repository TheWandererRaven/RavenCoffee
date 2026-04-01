package com.thewandererraven.ravencoffee;

import com.thewandererraven.ravencoffee.effect.breweffect.DefaultBrewEffectsManager;
import com.thewandererraven.ravencoffee.networking.SyncBrewManagerCaffeinePayload;
import com.thewandererraven.ravencoffee.networking.SyncBrewManagerDurationPayload;
import com.thewandererraven.ravencoffee.networking.SyncBrewManagerIconsPayload;
import com.thewandererraven.ravencoffee.platform.services.IBrewManagerHolder;
import com.thewandererraven.ravencoffee.recipe.brewing.BrewBaseReloadListenerFabric;
import com.thewandererraven.ravencoffee.recipe.brewing.BrewIngredientReloadListenerFabric;
import com.thewandererraven.ravencoffee.recipe.brewing.BrewVariantReloadListenerFabric;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;

public class RavenCoffeeFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        RavenCoffeeCommon.init();
        PayloadTypeRegistry.playS2C().register(SyncBrewManagerIconsPayload.TYPE, SyncBrewManagerIconsPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncBrewManagerDurationPayload.TYPE, SyncBrewManagerDurationPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncBrewManagerCaffeinePayload.TYPE, SyncBrewManagerCaffeinePayload.STREAM_CODEC);

        ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> {
            ServerPlayer player = listener.player;
            DefaultBrewEffectsManager manager = ((IBrewManagerHolder) player).ravencoffee$getBrewEffectManager();
            manager.sendAllInfoToClient();
        });
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new BrewIngredientReloadListenerFabric());
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new BrewBaseReloadListenerFabric());
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new BrewVariantReloadListenerFabric());
    }
}
