package com.thewandererraven.ravencoffee.platform;

import com.thewandererraven.ravencoffee.platform.services.IRegistryUtil;
import com.thewandererraven.ravencoffee.platform.services.IMenuFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public class NeoForgeRegistryUtil implements IRegistryUtil {

    @SuppressWarnings("all")
    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(
            BiFunction<BlockPos, BlockState, T> builder, Block... blocks) {
        return new BlockEntityType<T>(builder::apply, blocks);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> buildMenuType(IMenuFactory<T> factory) {
        return new MenuType<>(factory::create, FeatureFlagSet.of());
    }

    @Override
    public Holder<MobEffect> getMobEffect(ResourceLocation resourceLocation) {
        return BuiltInRegistries.MOB_EFFECT.get(resourceLocation).orElse(null);
    }
}
