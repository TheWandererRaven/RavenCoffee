package com.thewandererraven.ravencoffee;

import com.thewandererraven.ravencoffee.block.BlocksRegistry;
import com.thewandererraven.ravencoffee.datagen.DataGenDefinitions;
import com.thewandererraven.ravencoffee.datagen.DataGenItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.mixin.screen.HandledScreenMixin;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
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
        // TODO: If fabric implements datamaps for this, move this to its corresponding class
        for(DataGenItem dataGenItem : DataGenDefinitions.ITEMS)
            if(dataGenItem.compostableValue > 0.0f)
                ComposterBlock.COMPOSTABLES.put(dataGenItem.mainItem, dataGenItem.compostableValue);
    }
}
