package com.thewandererraven.ravencoffee.recipe.brewing;

import com.thewandererraven.ravencoffee.Constants;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

public class BrewBaseReloadListenerFabric extends BrewBaseReloadListener implements IdentifiableResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "brewing/base");
    }
}
