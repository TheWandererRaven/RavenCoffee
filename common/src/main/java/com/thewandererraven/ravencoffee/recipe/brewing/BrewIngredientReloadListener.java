package com.thewandererraven.ravencoffee.recipe.brewing;

import com.thewandererraven.ravenbrewslib.brew.data.BrewIngredient;
import com.thewandererraven.ravencoffee.Constants;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrewIngredientReloadListener extends SimpleJsonResourceReloadListener<BrewIngredient> {
    private Map<ResourceLocation, BrewIngredient> byId = Map.of();
    private List<BrewIngredient> all = List.of();
    private static final ResourceLocation reloadListenerId = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "brewing/ingredient");

    public static ResourceLocation getReloadListenerId() {
        return reloadListenerId;
    }

    public BrewIngredientReloadListener() {
        super(BrewIngredient.CODEC, FileToIdConverter.json("brewing/ingredient"));
    }

    @Override
    protected void apply(Map<ResourceLocation, BrewIngredient> entry, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<Item, BrewIngredient> map = new HashMap<>();

        entry.forEach((id, data) -> {
            map.put(data.item(), data);
        });

        BrewIngredientRegistry.set(map);
    }

    public List<BrewIngredient> getAll() {
        return all;
    }
}
