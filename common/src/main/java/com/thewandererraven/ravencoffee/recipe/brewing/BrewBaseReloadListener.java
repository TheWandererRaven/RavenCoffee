package com.thewandererraven.ravencoffee.recipe.brewing;

import com.thewandererraven.ravenbrewslib.brew.data.BrewBase;
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

public class BrewBaseReloadListener extends SimpleJsonResourceReloadListener<BrewBase> {
    private Map<ResourceLocation, BrewBase> byId = Map.of();
    private List<BrewBase> all = List.of();
    private static final ResourceLocation reloadListenerId = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "brewing/base");

    public static ResourceLocation getReloadListenerId() {
        return reloadListenerId;
    }

    public BrewBaseReloadListener() {
        super(BrewBase.CODEC, FileToIdConverter.json("brewing/base"));
    }

    @Override
    protected void apply(Map<ResourceLocation, BrewBase> entry, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<Item, BrewBase> map = new HashMap<>();

        entry.forEach((id, data) -> {
            map.put(data.item(), data);
        });

        BrewBaseRegistry.set(map);
    }

    public List<BrewBase> getAll() {
        return all;
    }
}
