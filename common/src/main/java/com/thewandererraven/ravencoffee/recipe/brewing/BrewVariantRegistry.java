package com.thewandererraven.ravencoffee.recipe.brewing;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.datacomponents.BrewIngredientData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BrewVariantRegistry {

    private static Map<List<ResourceLocation>, ResourceLocation> VARIANTS = Map.of();

    private BrewVariantRegistry() {}

    public static void set(Map<List<Item>, ResourceLocation> map) {
        VARIANTS = map.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> normalize(e.getKey()),
                        Map.Entry::getValue
                ));
    }

    public static Optional<ResourceLocation> get(List<Item> items) {
        return Optional.ofNullable(VARIANTS.get(normalize(items)));
    }

    private static List<ResourceLocation> normalize(List<Item> items) {
        return items.stream()
                .map(BuiltInRegistries.ITEM::getKey)
                .sorted()
                .toList();
    }

    public static Map<List<ResourceLocation>, ResourceLocation> getAll() {
        return VARIANTS;
    }
}