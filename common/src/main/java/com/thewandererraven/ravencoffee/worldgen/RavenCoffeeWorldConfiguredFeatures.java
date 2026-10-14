package com.thewandererraven.ravencoffee.worldgen;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.block.BlocksRegistry;
import com.thewandererraven.ravencoffee.block.CoffeeTreeTrunkBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.BiasedToBottomInt;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CaveVines;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public class RavenCoffeeWorldConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> COFFEE_TREE_CONFIGURED_FEATURE =
            ResourceKey.create(
                    Registries.CONFIGURED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "coffee_tree_configured_feature")
            );

    public static void configure(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        WeightedStateProvider weightedstateprovider = new WeightedStateProvider(
                WeightedList.<BlockState>builder()
                        .add(BlocksRegistry.COFFEE_TREE_LEAVES.get().defaultBlockState().setValue(CoffeeTreeTrunkBlock.AGE, 3), 3)
                        .add(BlocksRegistry.COFFEE_TREE_LEAVES.get().defaultBlockState().setValue(CoffeeTreeTrunkBlock.AGE, 2), 3)
                        .add(BlocksRegistry.COFFEE_TREE_LEAVES.get().defaultBlockState().setValue(CoffeeTreeTrunkBlock.AGE, 1), 2)
                        .add(BlocksRegistry.COFFEE_TREE_LEAVES.get().defaultBlockState().setValue(CoffeeTreeTrunkBlock.AGE, 0), 1)
        );
        BlockColumnConfiguration coffeeTreeConfig = new BlockColumnConfiguration(
                List.of(
                        BlockColumnConfiguration.layer(ConstantInt.of(1),
                                BlockStateProvider.simple(BlocksRegistry.COFFEE_TREE_TRUNK.get().defaultBlockState()
                                        .setValue(CoffeeTreeTrunkBlock.AGE, 3)
                                        .setValue(CoffeeTreeTrunkBlock.HAS_LEAVES, true)
                                )
                        ),
                        BlockColumnConfiguration.layer(ConstantInt.of(1), weightedstateprovider)
                ),
        Direction.UP,
                BlockPredicate.replaceable(),
                false
        );
        context.register(COFFEE_TREE_CONFIGURED_FEATURE, new ConfiguredFeature(
                Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        6,
                        5,
                        0,
                        PlacementUtils.inlinePlaced(
                                Feature.BLOCK_COLUMN,
                                coffeeTreeConfig,
                                BlockPredicateFilter.forPredicate(
                                            BlockPredicate.wouldSurvive(BlocksRegistry.COFFEE_TREE_TRUNK.get().defaultBlockState(), BlockPos.ZERO)
                                )
                        )
                )
        ));
    }
}
