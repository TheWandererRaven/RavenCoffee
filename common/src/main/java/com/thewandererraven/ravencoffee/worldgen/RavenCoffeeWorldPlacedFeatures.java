package com.thewandererraven.ravencoffee.worldgen;

import com.thewandererraven.ravencoffee.Constants;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.heightproviders.BiasedToBottomHeight;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class RavenCoffeeWorldPlacedFeatures {

    public static final ResourceLocation _coffee_tree_savannah_patch_id = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "coffee_tree_savannah_patch");
    public static final ResourceKey<PlacedFeature> COFFEE_TREE_SAVANNAH_PATCH = ResourceKey.create(
            Registries.PLACED_FEATURE,
            _coffee_tree_savannah_patch_id
    );

    public static final ResourceLocation _coffee_tree_jungle_patch_id = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "coffee_tree_jungle_patch");
    public static final ResourceKey<PlacedFeature> COFFEE_TREE_JUNGLE_PATCH = ResourceKey.create(
            Registries.PLACED_FEATURE,
            _coffee_tree_jungle_patch_id
    );

    public static void configure(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        List<PlacementModifier> coffeeTreeSavannahPatchModifiers = List.of(
                PlacementUtils.countExtra(2, 0.1F, 2),
                RarityFilter.onAverageOnceEvery(300),
                BiomeFilter.biome(),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE
        );
        context.register(
                COFFEE_TREE_SAVANNAH_PATCH,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(RavenCoffeeWorldConfiguredFeatures.COFFEE_TREE_CONFIGURED_FEATURE),
                        coffeeTreeSavannahPatchModifiers
                )
        );
        List<PlacementModifier> coffeeTreeJunglePatchModifiers = List.of(
                PlacementUtils.countExtra(5, 0.5F, 2),
                RarityFilter.onAverageOnceEvery(250),
                BiomeFilter.biome(),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE
        );
        context.register(
                COFFEE_TREE_JUNGLE_PATCH,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(RavenCoffeeWorldConfiguredFeatures.COFFEE_TREE_CONFIGURED_FEATURE),
                        coffeeTreeJunglePatchModifiers
                )
        );
    }
}
