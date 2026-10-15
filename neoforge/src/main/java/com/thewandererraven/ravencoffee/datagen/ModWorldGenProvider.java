package com.thewandererraven.ravencoffee.datagen;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.worldgen.RavenCoffeeWorldConfiguredFeatures;
import com.thewandererraven.ravencoffee.worldgen.RavenCoffeeWorldPlacedFeatures;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(
                    Registries.CONFIGURED_FEATURE,
                    RavenCoffeeWorldConfiguredFeatures::configure
            )
            .add(
                    Registries.PLACED_FEATURE,
                    RavenCoffeeWorldPlacedFeatures::configure
            )
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, bootstrap -> {
                HolderGetter<Biome> biomes = bootstrap.lookup(Registries.BIOME);

                HolderGetter<PlacedFeature> placedFeatures = bootstrap.lookup(Registries.PLACED_FEATURE);

                // Define the list of biomes for the regular configuration
                List<Holder<Biome>> biomeHolders = new ArrayList<>();
                biomeHolders.add(biomes.getOrThrow(Biomes.SAVANNA));
                biomeHolders.add(biomes.getOrThrow(Biomes.SAVANNA_PLATEAU));
                biomeHolders.add(biomes.getOrThrow(Biomes.WINDSWEPT_SAVANNA));
                biomeHolders.add(biomes.getOrThrow(Biomes.SPARSE_JUNGLE));
                biomeHolders.add(biomes.getOrThrow(Biomes.BAMBOO_JUNGLE));

                bootstrap.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                                RavenCoffeeWorldPlacedFeatures._coffee_tree_regular_biome_patch_id
                        ),
                        new BiomeModifiers.AddFeaturesBiomeModifier(
                                HolderSet.direct(biomeHolders),
                                HolderSet.direct(placedFeatures.getOrThrow(RavenCoffeeWorldPlacedFeatures.COFFEE_TREE_REGULAR_BIOME_PATCH)),
                                GenerationStep.Decoration.VEGETAL_DECORATION
                        )
                );
                bootstrap.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                                RavenCoffeeWorldPlacedFeatures._coffee_tree_dense_biome_patch_id
                        ),
                        new BiomeModifiers.AddFeaturesBiomeModifier(
                                HolderSet.direct(biomes.getOrThrow(Biomes.JUNGLE)),
                                HolderSet.direct(placedFeatures.getOrThrow(RavenCoffeeWorldPlacedFeatures.COFFEE_TREE_DENSE_BIOME_PATCH)),
                                GenerationStep.Decoration.VEGETAL_DECORATION
                        )
                );
            })
            ;

    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Constants.MOD_ID));
    }
}
