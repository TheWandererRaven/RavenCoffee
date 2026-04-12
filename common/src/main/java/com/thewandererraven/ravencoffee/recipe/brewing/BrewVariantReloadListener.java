package com.thewandererraven.ravencoffee.recipe.brewing;

import com.thewandererraven.ravenbrewslib.brew.data.BrewVariant;
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

public class BrewVariantReloadListener extends SimpleJsonResourceReloadListener<BrewVariant> {
    private Map<ResourceLocation, BrewVariant> byId = Map.of();
    private List<BrewVariant> all = List.of();
    private static final ResourceLocation reloadListenerId = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "brewing/variant");

    public static ResourceLocation getReloadListenerId() {
        return reloadListenerId;
    }

    public BrewVariantReloadListener() {
        super(BrewVariant.CODEC, FileToIdConverter.json("brewing/variant"));
    }

    @Override
    protected void apply(Map<ResourceLocation, BrewVariant> entry, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<List<Item>, ResourceLocation> map = new HashMap<>();

        entry.forEach((items, data) -> {
            map.put(data.items(), data.variantId());
        });

        BrewVariantRegistry.set(map);
    }

    public List<BrewVariant> getAll() {
        return all;
    }
}
