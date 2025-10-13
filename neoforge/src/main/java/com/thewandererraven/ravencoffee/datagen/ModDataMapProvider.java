package com.thewandererraven.ravencoffee.datagen;

import com.thewandererraven.ravencoffee.item.GeneralItemsRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {

    protected ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        Builder<Compostable, Item> compostableItemBuilder = this.builder(NeoForgeDataMaps.COMPOSTABLES);
        for(DataGenItem dataGenItem : DataGenDefinitions.ITEMS)
        {
            if(dataGenItem.compostableValue > 0.0f)
                compostableItemBuilder.add(GeneralItemsRegistry.COFFEE_CHERRIES.getId(), new Compostable(dataGenItem.compostableValue), false);
        }
    }
}
