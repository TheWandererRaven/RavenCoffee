package com.thewandererraven.ravencoffee.datagen;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.block.BlocksRegistry;
import com.thewandererraven.ravencoffee.item.GeneralItemsRegistry;
import com.thewandererraven.ravencoffee.registry.RegistryObject;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.*;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ModModelProvider extends ModelProvider {

    List<String> ignoredBlocks = new ArrayList<String>();
    List<String> ignoredItems = new ArrayList<String>();

    public ModModelProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        for(DataGenItem dataGenItem : DataGenDefinitions.ITEMS) {
            if(dataGenItem.modelGenType == DataGenItem.ItemModelGenTypes.FLAT_ITEM)
                generateFlatItem(itemModels, dataGenItem);
            else if(dataGenItem.modelGenType == DataGenItem.ItemModelGenTypes.BLOCK)
                itemModels.itemModelOutput.accept(dataGenItem.mainItem, ItemModelUtils.plainModel(dataGenItem.blockResourceLocation));
            else
                ignoredItems.add(dataGenItem.mainItem.getDescriptionId());
        }
        for(DataGenBlock dataGenBlock : DataGenDefinitions.BLOCKS) {
            if(dataGenBlock.modelGenType == DataGenBlock.BlockModelGenTypes.TRIVIAL_BLOCK)
                blockModels.createTrivialCube(dataGenBlock.mainBlock);
            else if (dataGenBlock.modelGenType == DataGenBlock.BlockModelGenTypes.NO_TEMPLATE_HORIZONTALLY_ROTATION)
                blockModels.createNonTemplateHorizontalBlock(dataGenBlock.mainBlock);
            else
                ignoredBlocks.add(dataGenBlock.mainBlock.getDescriptionId());
        }
    }

    public final void generateFlatItem(ItemModelGenerators itemModelGenerators, DataGenItem dataGenItem) {
        if(dataGenItem.alternateTextureLocation == null)
            itemModelGenerators.generateFlatItem(dataGenItem.mainItem, dataGenItem.modelTemplate);
        else
            itemModelGenerators.itemModelOutput.accept(dataGenItem.mainItem, ItemModelUtils.plainModel(
                    dataGenItem.modelTemplate.create(
                            ModelLocationUtils.getModelLocation(dataGenItem.mainItem),
                            (new TextureMapping()).put(TextureSlot.LAYER0, dataGenItem.alternateTextureLocation),
                            itemModelGenerators.modelOutput
                    ))
            );
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return BlocksRegistry.BLOCKS.getEntries().stream().filter(
                (registryObject -> !ignoredBlocks.contains(registryObject.get().getDescriptionId()))
        ).map(RegistryObject::asHolder);
        //return BlocksRegistry.BLOCKS.getEntries().stream().map(RegistryObject::asHolder);
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return GeneralItemsRegistry.ITEMS.getEntries().stream().filter(
                (registryObject -> !ignoredItems.contains(registryObject.get().getDescriptionId()))
        ).map(RegistryObject::asHolder);
    }
}