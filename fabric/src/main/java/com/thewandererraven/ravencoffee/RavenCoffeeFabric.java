package com.thewandererraven.ravencoffee;

import com.thewandererraven.ravencoffee.datagen.DataGenDefinitions;
import com.thewandererraven.ravencoffee.datagen.DataGenItem;
import com.thewandererraven.ravencoffee.networking.SyncBrewManagerPayload;
import com.thewandererraven.ravencoffee.networking.SyncBrewPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.world.level.block.ComposterBlock;

public class RavenCoffeeFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        RavenCoffeeCommon.init();
        PayloadTypeRegistry.playS2C().register(SyncBrewPayload.TYPE, SyncBrewPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncBrewManagerPayload.TYPE, SyncBrewManagerPayload.STREAM_CODEC);
    }
}
