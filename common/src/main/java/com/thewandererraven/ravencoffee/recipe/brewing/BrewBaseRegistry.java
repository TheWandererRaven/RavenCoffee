package com.thewandererraven.ravencoffee.recipe.brewing;

import com.thewandererraven.ravenbrewslib.brew.data.BrewBase;
import net.minecraft.world.item.Item;

import java.util.Map;

public class BrewBaseRegistry {

    private static Map<Item, BrewBase> INGREDIENTS = Map.of();

    private BrewBaseRegistry() {}

    public static void set(Map<Item, BrewBase> map) {
        INGREDIENTS = Map.copyOf(map);
    }

    public static BrewBase get(Item item) {
        return INGREDIENTS.get(item);
    }

    public static Map<Item, BrewBase> getAll() {
        return INGREDIENTS;
    }
}
