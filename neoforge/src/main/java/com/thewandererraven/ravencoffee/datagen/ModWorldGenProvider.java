package com.thewandererraven.ravencoffee.datagen;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.worldgen.RavenCoffeeWorldConfiguredFeatures;
import com.thewandererraven.ravencoffee.worldgen.RavenCoffeeWorldPlacedFeatures;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

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

                bootstrap.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                                RavenCoffeeWorldPlacedFeatures._coffee_tree_savannah_patch_id
                        ),
                        new BiomeModifiers.AddFeaturesBiomeModifier(
                                biomes.getOrThrow(BiomeTags.IS_SAVANNA),
                                HolderSet.direct(placedFeatures.getOrThrow(RavenCoffeeWorldPlacedFeatures.COFFEE_TREE_SAVANNAH_PATCH)),
                                GenerationStep.Decoration.VEGETAL_DECORATION
                        )
                );
                bootstrap.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                                RavenCoffeeWorldPlacedFeatures._coffee_tree_jungle_patch_id
                        ),
                        new BiomeModifiers.AddFeaturesBiomeModifier(
                                biomes.getOrThrow(BiomeTags.IS_JUNGLE),
                                HolderSet.direct(placedFeatures.getOrThrow(RavenCoffeeWorldPlacedFeatures.COFFEE_TREE_JUNGLE_PATCH)),
                                GenerationStep.Decoration.VEGETAL_DECORATION
                        )
                );
            })
            ;

    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Constants.MOD_ID));
    }
}
