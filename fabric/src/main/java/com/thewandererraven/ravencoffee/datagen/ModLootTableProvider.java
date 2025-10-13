package com.thewandererraven.ravencoffee.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        for(DataGenBlock dataGenBlock : DataGenDefinitions.BLOCKS) {
            if(dataGenBlock.onlyDropsSelf)
                this.dropSelf(dataGenBlock.mainBlock);
            else if(dataGenBlock.lootTable != null)
                this.add(dataGenBlock.mainBlock, dataGenBlock.lootTable);
        }
    }
}
