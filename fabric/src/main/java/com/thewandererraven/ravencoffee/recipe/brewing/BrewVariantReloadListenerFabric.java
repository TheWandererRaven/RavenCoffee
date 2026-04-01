package com.thewandererraven.ravencoffee.recipe.brewing;

import com.thewandererraven.ravencoffee.Constants;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

public class BrewVariantReloadListenerFabric extends BrewVariantReloadListener implements IdentifiableResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
        return getReloadListenerId();
    }
}
