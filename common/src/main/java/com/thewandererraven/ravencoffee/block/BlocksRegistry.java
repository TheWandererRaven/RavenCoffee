package com.thewandererraven.ravencoffee.block;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.registry.RegistryObject;
import com.thewandererraven.ravencoffee.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

public class BlocksRegistry {
    public static final RegistryProvider<Block> BLOCKS = RegistryProvider.get(Registries.BLOCK, Constants.MOD_ID);

    public static final String _brownie_block_id = "brownie_block";
    public static final RegistryObject<Block> BROWNIE_BLOCK =
            BLOCKS.register(_brownie_block_id, () -> new Block(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(
                            Registries.BLOCK,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _brownie_block_id)
                    ))
                    .forceSolidOn()
                    .pushReaction(PushReaction.NORMAL)
                    .sound(SoundType.WOOL)
                    .destroyTime(0.5F)
            ));

    public static final String _coffee_grinder_id = "coffee_grinder";
    public static final RegistryObject<Block> COFFEE_GRINDER =
            BLOCKS.register(_coffee_grinder_id, () -> new CoffeeGrinderBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(
                            Registries.BLOCK,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _coffee_grinder_id)
                    ))
                    .forceSolidOn()
                    .pushReaction(PushReaction.NORMAL)
                    .sound(SoundType.WOOD)
                    .destroyTime(0.8F)
            ));

    public static final String _coffee_machine_id = "coffee_machine";
    public static final RegistryObject<Block> COFFEE_MACHINE =
            BLOCKS.register(_coffee_machine_id, () -> new CoffeeMachineBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(
                            Registries.BLOCK,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _coffee_machine_id)
                    ))
                    .forceSolidOn()
                    .pushReaction(PushReaction.NORMAL)
                    .sound(SoundType.METAL)
                    .destroyTime(0.8F)
                    //.noOcclusion() //nonOpaque()????
            ));

    public static final String _coffee_brewing_station_id = "coffee_brewing_station";
    public static final RegistryObject<Block> COFFEE_BREWING_STATION =
            BLOCKS.register(_coffee_brewing_station_id, () -> new CoffeeBrewingStation(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(
                            Registries.BLOCK,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _coffee_brewing_station_id)
                    ))
                    .forceSolidOn()
                    .pushReaction(PushReaction.NORMAL)
                    .sound(SoundType.WOOD)
                    .destroyTime(1.0F)
            ));

    public static void init() {

    }
}
