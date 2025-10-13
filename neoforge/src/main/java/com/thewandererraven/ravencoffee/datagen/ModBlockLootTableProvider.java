package com.thewandererraven.ravencoffee.datagen;

import com.thewandererraven.ravencoffee.block.BlocksRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        for(DataGenBlock dataGenBlock : DataGenDefinitions.BLOCKS) {
            if(dataGenBlock.onlyDropsSelf)
                this.dropSelf(dataGenBlock.mainBlock);
            else if(dataGenBlock.lootTable != null)
                this.add(dataGenBlock.mainBlock, dataGenBlock.lootTable);
        }
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BlocksRegistry.BLOCKS.getEntries().stream().map(entry -> entry.get())::iterator;
    }
}
