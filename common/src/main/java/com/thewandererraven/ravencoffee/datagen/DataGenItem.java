package com.thewandererraven.ravencoffee.datagen;

import com.thewandererraven.ravencoffee.Constants;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class DataGenItem {
    public Item mainItem;
    public ResourceLocation alternateTextureLocation;
    public ItemModelGenTypes modelGenType;
    public ModelTemplate modelTemplate;
    public float compostableValue = 0.0f;
    public ResourceLocation blockResourceLocation = null;
    public List<TagKey<Item>> tags = new ArrayList<>();
    public List<RecipeBuilder> recipes = new ArrayList<>();

    public DataGenItem(Item _item) {
        this(_item, ItemModelGenTypes.FLAT_ITEM, ModelTemplates.FLAT_ITEM);
    }

    public DataGenItem(Item _item, ItemModelGenTypes _modelGenType, ModelTemplate _modelTemplate) {
        this.mainItem = _item;
        this.modelGenType = _modelGenType;
        this.modelTemplate = _modelTemplate;
        this.alternateTextureLocation = null;
    }

    public DataGenItem withTag(TagKey<Item> blockTag) {
        this.tags.add(blockTag);
        return this;
    }

    public DataGenItem setItemModelGenerationType(DataGenItem.ItemModelGenTypes genType) {
        this.modelGenType = genType;
        return this;
    }

    public DataGenItem setCompostableValue(float _compostableValue) {
        this.compostableValue = _compostableValue;
        return this;
    }

    public DataGenItem addRecipe(RecipeBuilder recipeBuilder) {
        this.recipes.add(recipeBuilder);
        return this;
    }

    public DataGenItem addShapelessRecipe(ShapelessRecipeBuilder shapelessRecipeBuilder) {
        this.recipes.add(shapelessRecipeBuilder);
        return this;
    }

    public DataGenItem setBlockItemPlainModelFromBlock(Block block, String suffix) {
        this.blockResourceLocation = ModelLocationUtils.getModelLocation(block, suffix);
        return this;
    }

    public DataGenItem setBlockItemPlainModelFromBlock(String suffix) {
        return this.setBlockItemPlainModelFromBlock(((BlockItem)this.mainItem.asItem()).getBlock(), suffix);
    }

    public DataGenItem setBlockItemPlainModelFromBlock() {
        return this.setBlockItemPlainModelFromBlock(((BlockItem)this.mainItem.asItem()).getBlock(), "");
    }

    public DataGenItem setAlternateTextureResourceLocation(ResourceLocation resourceLocation) {
        this.alternateTextureLocation = resourceLocation;
        return this;
    }

    public DataGenItem setAlternateTextureResourceLocation(String path) {
        return this.setAlternateTextureResourceLocation(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path));
    }

    public enum ItemModelGenTypes {
        IGNORE,
        CUSTOM,
        FLAT_ITEM,
        BLOCK
    }
}
