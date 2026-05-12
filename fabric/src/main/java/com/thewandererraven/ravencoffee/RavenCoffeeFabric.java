package com.thewandererraven.ravencoffee;

import com.thewandererraven.ravencoffee.effect.breweffect.DefaultCoffeeBrewEffectsManager;
import com.thewandererraven.ravencoffee.networking.SyncBrewGuiDisplayCaffeinePayload;
import com.thewandererraven.ravencoffee.networking.SyncBrewGuiDisplayDurationsPayload;
import com.thewandererraven.ravencoffee.networking.SyncBrewGuiDisplayIconsPayload;
import com.thewandererraven.ravencoffee.platform.services.IBrewManagerHolder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;

public class RavenCoffeeFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        RavenCoffeeCommon.init();
        PayloadTypeRegistry.playS2C().register(SyncBrewGuiDisplayIconsPayload.TYPE, SyncBrewGuiDisplayIconsPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncBrewGuiDisplayDurationsPayload.TYPE, SyncBrewGuiDisplayDurationsPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncBrewGuiDisplayCaffeinePayload.TYPE, SyncBrewGuiDisplayCaffeinePayload.STREAM_CODEC);

        ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> {
            ServerPlayer player = listener.player;
            DefaultCoffeeBrewEffectsManager manager = ((IBrewManagerHolder) player).ravencoffee$getBrewEffectManager();
            manager.sendAllInfoToClient();
        });
    }
}
