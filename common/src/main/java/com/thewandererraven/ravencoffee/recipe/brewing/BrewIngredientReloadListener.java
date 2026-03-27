package com.thewandererraven.ravencoffee.recipe.brewing;

import com.thewandererraven.ravencoffee.datacomponents.BrewIngredientData;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrewIngredientReloadListener extends SimpleJsonResourceReloadListener<BrewIngredientData> {
    private Map<ResourceLocation, BrewIngredientData> byId = Map.of();
    private List<BrewIngredientData> all = List.of();

    public BrewIngredientReloadListener() {
        super(BrewIngredientData.CODEC, FileToIdConverter.json("brewing/ingredient"));
    }

    @Override
    protected void apply(Map<ResourceLocation, BrewIngredientData> entry, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<Item, BrewIngredientData> map = new HashMap<>();

        entry.forEach((id, data) -> {
            map.put(data.item(), data);
        });

        BrewIngredientRegistry.set(map);
    }

    public List<BrewIngredientData> getAll() {
        return all;
    }
}
