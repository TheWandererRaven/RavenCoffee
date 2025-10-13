package com.thewandererraven.ravencoffee.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public interface IRegistryUtil {
    <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(
            BiFunction<BlockPos, BlockState, T> builder, Block... blocks);

    <T extends AbstractContainerMenu> MenuType<T> buildMenuType(IMenuFactory<T> factory);

    Holder<MobEffect> getMobEffect(ResourceLocation resourceLocation);
}
