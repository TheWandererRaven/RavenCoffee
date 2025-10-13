package com.thewandererraven.ravencoffee.recipe;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.registry.RegistryObject;
import com.thewandererraven.ravencoffee.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class RecipesRegistry {
    public static final RegistryProvider<RecipeType<?>> TYPES = RegistryProvider.get(Registries.RECIPE_TYPE, Constants.MOD_ID);
    public static final RegistryProvider<RecipeSerializer<?>> SERIALIZERS = RegistryProvider.get(Registries.RECIPE_SERIALIZER, Constants.MOD_ID);

    private static final String coffee_grinding_id = "coffee_grinding";
    public static final RegistryObject<RecipeType<CoffeeGrindingRecipe>> COFFEE_GRINDING = TYPES.register(coffee_grinding_id, () -> new RecipeType<CoffeeGrindingRecipe>() {
        @Override
        public String toString() {
            return coffee_grinding_id;
        }
    });
    public static final RegistryObject<RecipeSerializer<CoffeeGrindingRecipe>> COFFEE_GRINDING_SERIALIZER = SERIALIZERS.register(coffee_grinding_id, CoffeeGrindingRecipeSerializer::new);

    public static void init() {

    }
}
