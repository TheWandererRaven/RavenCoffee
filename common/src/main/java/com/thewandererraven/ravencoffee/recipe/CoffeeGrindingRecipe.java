package com.thewandererraven.ravencoffee.recipe;

import com.thewandererraven.ravencoffee.menu.CoffeeGrinderIngredientsContainer;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class CoffeeGrindingRecipe implements Recipe<CoffeeGrinderIngredientsContainer> {

    private final List<Ingredient> ingredients;
    private final ItemStack output;

    public CoffeeGrindingRecipe(List<Ingredient> ingredients, ItemStack output) {
        this.ingredients = ingredients;
        this.output = output;
    }

    public List<Ingredient> getInputs() {
        return ingredients;
    }

    public ItemStack getOutput() {
        return output;
    }

    @Override
    public boolean matches(CoffeeGrinderIngredientsContainer inputContainer, Level level) {
        int foundMatches = 0;
        for(Ingredient ingredient : this.ingredients) {
            for(ItemStack stack : inputContainer.getItems()) {
                if(ingredient.test(stack))
                {
                    foundMatches++;
                    break;
                }
            }
        }
        return foundMatches == ingredients.size();
    }

    @Override
    public ItemStack assemble(CoffeeGrinderIngredientsContainer inputContainer, HolderLookup.Provider provider) {
        return output.copy();
    }

    @Override
    public RecipeSerializer<? extends Recipe<CoffeeGrinderIngredientsContainer>> getSerializer() {
        return RecipesRegistry.COFFEE_GRINDING_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<CoffeeGrinderIngredientsContainer>> getType() {
        return RecipesRegistry.COFFEE_GRINDING.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(ingredients);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        // TODO: I believe I can add this to the recipe jsons
        return RecipeBookCategories.CRAFTING_MISC;
    }
}
