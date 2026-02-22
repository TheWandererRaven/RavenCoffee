package com.thewandererraven.ravencoffee.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.*;

public class ModModelProvider extends FabricModelProvider {

//    public static final VariantMutator NOP;
//    public static final VariantMutator UV_LOCK;
//    public static final VariantMutator X_ROT_90;
//    public static final VariantMutator X_ROT_180;
//    public static final VariantMutator X_ROT_270;
//    public static final VariantMutator Y_ROT_90;
//    public static final VariantMutator Y_ROT_180;
//    public static final VariantMutator Y_ROT_270;
//    private static final PropertyDispatch<VariantMutator> ROTATION_HORIZONTAL_FACING;

    public ModModelProvider(FabricDataOutput output) {
        FabricSpriteProvider
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        for(DataGenBlock dataGenBlock : DataGenDefinitions.BLOCKS) {
            if (dataGenBlock.modelGenType == DataGenBlock.BlockModelGenTypes.TRIVIAL_BLOCK)
                blockStateModelGenerator.createTrivialCube(dataGenBlock.mainBlock);
            else if (dataGenBlock.modelGenType == DataGenBlock.BlockModelGenTypes.NO_TEMPLATE_HORIZONTALLY_ROTATION)
                blockStateModelGenerator.createNonTemplateHorizontalBlock(dataGenBlock.mainBlock);
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        for(DataGenItem dataGenItem : DataGenDefinitions.ITEMS) {
            if(dataGenItem.modelGenType == DataGenItem.ItemModelGenTypes.FLAT_ITEM) {
                generateFlatItem(itemModelGenerator, dataGenItem);
            }
            if(dataGenItem.modelGenType == DataGenItem.ItemModelGenTypes.BLOCK)
                itemModelGenerator.itemModelOutput.accept(dataGenItem.mainItem, ItemModelUtils.plainModel(dataGenItem.blockResourceLocation));
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

//    static {
//            NOP = (variant) -> variant;
//            UV_LOCK = VariantMutator.UV_LOCK.withValue(true);
//            X_ROT_90 = VariantMutator.X_ROT.withValue(Quadrant.R90);
//            X_ROT_180 = VariantMutator.X_ROT.withValue(Quadrant.R180);
//            X_ROT_270 = VariantMutator.X_ROT.withValue(Quadrant.R270);
//            Y_ROT_90 = VariantMutator.Y_ROT.withValue(Quadrant.R90);
//            Y_ROT_180 = VariantMutator.Y_ROT.withValue(Quadrant.R180);
//            Y_ROT_270 = VariantMutator.Y_ROT.withValue(Quadrant.R270);
//            ROTATION_HORIZONTAL_FACING = PropertyDispatch.modify(BlockStateProperties.HORIZONTAL_FACING).select(Direction.EAST, Y_ROT_90).select(Direction.SOUTH, Y_ROT_180).select(Direction.WEST, Y_ROT_270).select(Direction.NORTH, NOP);
//    }
}
