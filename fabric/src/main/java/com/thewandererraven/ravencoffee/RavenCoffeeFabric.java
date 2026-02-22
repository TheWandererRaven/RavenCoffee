package com.thewandererraven.ravencoffee;

import com.thewandererraven.ravencoffee.networking.SyncBrewManagerCaffeinePayload;
import com.thewandererraven.ravencoffee.networking.SyncBrewManagerDurationPayload;
import com.thewandererraven.ravencoffee.networking.SyncBrewManagerIconsPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

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
    }
}
