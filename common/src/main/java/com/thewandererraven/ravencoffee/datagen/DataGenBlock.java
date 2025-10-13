package com.thewandererraven.ravencoffee.datagen;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.ArrayList;
import java.util.List;

public class DataGenBlock {
    public Block mainBlock;
    public BlockModelGenTypes modelGenType;
    public List<TagKey<Block>> tags = new ArrayList<>();
    public Boolean onlyDropsSelf = false;
    public LootTable.Builder lootTable = null;

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
