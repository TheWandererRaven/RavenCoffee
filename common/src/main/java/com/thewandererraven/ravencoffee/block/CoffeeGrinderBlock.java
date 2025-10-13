package com.thewandererraven.ravencoffee.block;

import com.mojang.serialization.MapCodec;
import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.item.GeneralItemsRegistry;
import com.thewandererraven.ravencoffee.menu.CoffeeGrinderMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;


public class CoffeeGrinderBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<CoffeeGrinderBlock> CODEC = simpleCodec(CoffeeGrinderBlock::new);
    public static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 7, 12);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    public CoffeeGrinderBlock(Properties p_54120_) {
        super(p_54120_);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING});
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
//            player.openMenu(new SimpleMenuProvider(
//                    (syncId, inv, platerEntity) -> new CoffeeGrinderMenu(syncId, inv),
//                    Component.translatable("item." + Constants.MOD_ID + "." + GeneralItemsRegistry._coffee_grinder_item_id)
//            ));
            player.openMenu(this.getMenuProvider(state, level, pos));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected @Nullable MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider(
                (id, playerInv, player) -> new CoffeeGrinderMenu(id, playerInv),
                    Component.translatable("item." + Constants.MOD_ID + "." + GeneralItemsRegistry._coffee_grinder_item_id)
        );
    }
}
