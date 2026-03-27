package com.thewandererraven.ravencoffee.recipe.brewing;

import com.thewandererraven.ravencoffee.datacomponents.BrewBaseData;
import net.minecraft.world.item.Item;

import java.util.Map;

public class BrewBaseRegistry {

    private static Map<Item, BrewBaseData> INGREDIENTS = Map.of();

    private BrewBaseRegistry() {}

    public static void set(Map<Item, BrewBaseData> map) {
        INGREDIENTS = Map.copyOf(map);
    }

    public static BrewBaseData get(Item item) {
        return INGREDIENTS.get(item);
    }

    public static Map<Item, BrewBaseData> getAll() {
        return INGREDIENTS;
    }
}
