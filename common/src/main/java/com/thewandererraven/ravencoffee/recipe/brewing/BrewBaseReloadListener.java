package com.thewandererraven.ravencoffee.recipe.brewing;

import com.thewandererraven.ravencoffee.datacomponents.BrewBaseData;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrewBaseReloadListener extends SimpleJsonResourceReloadListener<BrewBaseData> {
    private Map<ResourceLocation, BrewBaseData> byId = Map.of();
    private List<BrewBaseData> all = List.of();

    public BrewBaseReloadListener() {
        super(BrewBaseData.CODEC, FileToIdConverter.json("brewing/base"));
    }

    @Override
    protected void apply(Map<ResourceLocation, BrewBaseData> entry, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<Item, BrewBaseData> map = new HashMap<>();

        entry.forEach((id, data) -> {
            map.put(data.item(), data);
        });

        BrewBaseRegistry.set(map);
    }

    public List<BrewBaseData> getAll() {
        return all;
    }
}
