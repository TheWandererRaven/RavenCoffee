package com.thewandererraven.ravencoffee.block;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.item.GeneralItemsRegistry;
import com.thewandererraven.ravencoffee.menu.CoffeeBrewingStationMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class CoffeeBrewingStation extends Block {
    public CoffeeBrewingStation(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            player.openMenu(this.getMenuProvider(state, level, pos));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected @Nullable MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider(
                (id, playerInv, player) -> new CoffeeBrewingStationMenu(id, playerInv),
                Component.translatable("item." + Constants.MOD_ID + "." + GeneralItemsRegistry._coffee_brewing_station_id)
        );
    }
}
