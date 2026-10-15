package com.thewandererraven.ravencoffee.worldgen;

import com.thewandererraven.ravencoffee.Constants;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class RavenCoffeeWorldPlacedFeatures {

    public static final ResourceLocation _coffee_tree_regular_biome_patch_id = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "coffee_tree_regular_patch");
    public static final ResourceKey<PlacedFeature> COFFEE_TREE_REGULAR_BIOME_PATCH = ResourceKey.create(
            Registries.PLACED_FEATURE,
            _coffee_tree_regular_biome_patch_id
    );

    public static final ResourceLocation _coffee_tree_dense_biome_patch_id = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "coffee_tree_dense_biome_patch");
    public static final ResourceKey<PlacedFeature> COFFEE_TREE_DENSE_BIOME_PATCH = ResourceKey.create(
            Registries.PLACED_FEATURE,
            _coffee_tree_dense_biome_patch_id
    );

    public static void configure(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        List<PlacementModifier> coffeeTreeRegularBiomePatchModifiers = List.of(
                PlacementUtils.countExtra(2, 0.1F, 2),
                RarityFilter.onAverageOnceEvery(250),
                BiomeFilter.biome(),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE
        );
        context.register(
                COFFEE_TREE_REGULAR_BIOME_PATCH,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(RavenCoffeeWorldConfiguredFeatures.COFFEE_TREE_CONFIGURED_FEATURE),
                        coffeeTreeRegularBiomePatchModifiers
                )
        );
        // NOTE: higher tries and gen probability for a dense biomes (regular jungle)
        List<PlacementModifier> coffeeTreeDenseBiomePatchModifiers = List.of(
                PlacementUtils.countExtra(10, 0.5F, 5),
                RarityFilter.onAverageOnceEvery(150),
                BiomeFilter.biome(),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE
        );
        context.register(
                COFFEE_TREE_DENSE_BIOME_PATCH,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(RavenCoffeeWorldConfiguredFeatures.COFFEE_TREE_CONFIGURED_FEATURE),
                        coffeeTreeDenseBiomePatchModifiers
                )
        );
    }
}
