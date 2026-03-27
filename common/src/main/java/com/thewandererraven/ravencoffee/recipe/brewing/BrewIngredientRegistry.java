package com.thewandererraven.ravencoffee.recipe.brewing;

import com.thewandererraven.ravencoffee.datacomponents.BrewIngredientData;
import net.minecraft.world.item.Item;

import java.util.Map;

public class BrewIngredientRegistry {

    private static Map<Item, BrewIngredientData> INGREDIENTS = Map.of();

    private BrewIngredientRegistry() {}

    public static void set(Map<Item, BrewIngredientData> map) {
        INGREDIENTS = Map.copyOf(map);
    }

    public static BrewIngredientData get(Item item) {
        return INGREDIENTS.get(item);
    }

    public static Map<Item, BrewIngredientData> getAll() {
        return INGREDIENTS;
    }
}
