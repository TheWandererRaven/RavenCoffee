package com.thewandererraven.ravencoffee.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class CoffeeGrindingRecipeSerializer implements RecipeSerializer<CoffeeGrindingRecipe> {

    public static final MapCodec<CoffeeGrindingRecipe> CODEC = RecordCodecBuilder.mapCodec(ints -> ints.group(
            Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(CoffeeGrindingRecipe::getInputs),
            ItemStack.CODEC.fieldOf("result").forGetter(CoffeeGrindingRecipe::getOutput)
            ).apply(ints, CoffeeGrindingRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, CoffeeGrindingRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), CoffeeGrindingRecipe::getInputs,
                    ItemStack.STREAM_CODEC, CoffeeGrindingRecipe::getOutput,
                    CoffeeGrindingRecipe::new
            );

    @Override
    public MapCodec<CoffeeGrindingRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, CoffeeGrindingRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
