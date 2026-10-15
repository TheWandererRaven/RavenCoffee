package com.thewandererraven.ravencoffee.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class DataGenBlock {
    public Block mainBlock;
    public BlockModelGenTypes modelGenType;
    public List<TagKey<Block>> tags = new ArrayList<>();
    public Boolean onlyDropsSelf = false;
    public LootTable.Builder lootTable = null;
    public Function<HolderLookup.Provider, LootTable.Builder> lookupLootTable = null;

    public DataGenBlock(Block _block) {
        this(_block, BlockModelGenTypes.TRIVIAL_BLOCK);
    }

    public DataGenBlock(Block _block, BlockModelGenTypes _modelGenType) {
        this.mainBlock = _block;
        this.modelGenType = _modelGenType;
    }

    public DataGenBlock withTag(TagKey<Block> blockTag) {
        this.tags.add(blockTag);
        return this;
    }

    public DataGenBlock setBlockModelGenerationType(BlockModelGenTypes genType) {
        this.modelGenType = genType;
        return this;
    }

    public DataGenBlock setOnlyDropsSelf() {
        return setOnlyDropsSelf(true);
    }

    public DataGenBlock setOnlyDropsSelf(Boolean dropsSelf) {
        this.onlyDropsSelf = dropsSelf;
        return this;
    }

    public DataGenBlock withSingleItemLootTable(ItemLike item) {
        return this.withLootTable(
                (new LootTable.Builder())
                        .withPool(LootPool.lootPool().add(
                                LootItem.lootTableItem(item)
                        ))
        );
    }

    public DataGenBlock withLookupLootTable(Function<HolderLookup.Provider, LootTable.Builder> _lootTable) {
        this.lookupLootTable = _lootTable;
        return this;
    }

    public DataGenBlock withLootTable(LootTable.Builder _lootTable) {
        this.lootTable = _lootTable;
        return this;
    }

    public enum BlockModelGenTypes {
        IGNORE,
        CUSTOM,
        TRIVIAL_BLOCK,
        NO_TEMPLATE_HORIZONTALLY_ROTATION
    }
}
