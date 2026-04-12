package com.thewandererraven.ravencoffee.recipe.brewing;

import com.thewandererraven.ravenbrewslib.brew.data.BrewIngredient;
import net.minecraft.world.item.Item;

import java.util.Map;

public class BrewIngredientRegistry {

    private static Map<Item, BrewIngredient> INGREDIENTS = Map.of();

    private BrewIngredientRegistry() {}

    public static void set(Map<Item, BrewIngredient> map) {
        INGREDIENTS = Map.copyOf(map);
    }

    public static BrewIngredient get(Item item) {
        return INGREDIENTS.get(item);
    }

    public static Map<Item, BrewIngredient> getAll() {
        return INGREDIENTS;
    }
}
