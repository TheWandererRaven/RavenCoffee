package com.thewandererraven.ravencoffee.block;

import com.thewandererraven.ravencoffee.item.GeneralItemsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class TiramisuBlock extends Block {
    public static final int MAX_SLICES;
    public static final IntegerProperty SLICES;
    public static final int FULL_SIGNAL;
    protected static final VoxelShape[] SHAPE_BY_BITE;

    public final int HUNGER_PER_BITE;
    public final float SATURATION_PER_BITE;

    public TiramisuBlock(Properties props, int hunger, float saturation) {
        super(props);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(SLICES, 16));
        this.HUNGER_PER_BITE = hunger;
        this.SATURATION_PER_BITE = saturation;
    }

    public TiramisuBlock(Properties props) {
        this(props, 1, 1.0F);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{SLICES});
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        if(context.getItemInHand().is(GeneralItemsRegistry.TIRAMISU_SLICE.get()))
            return (BlockState)((BlockState)this.stateDefinition.any()).setValue(SLICES, 1);
        else
            return this.defaultBlockState();
    }

    protected int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        return CakeBlock.getOutputSignal((Integer)blockState.getValue(SLICES));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_BITE[(Integer)state.getValue(SLICES) - 1];
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult hitResult) {
        InteractionResult result;
        if(itemStack.is(GeneralItemsRegistry.TIRAMISU_SLICE.get())) {
            result = addSlice(level, blockPos, blockState, player);
            if(result == InteractionResult.CONSUME)
                itemStack.setCount(itemStack.getCount() - 1);
        } else {
            result = removeSlice(level, blockPos, blockState, player);
            if(result == InteractionResult.CONSUME) {
                ItemStack slice = new ItemStack(GeneralItemsRegistry.TIRAMISU_SLICE.get());
                ItemEntity droppedItem = new ItemEntity(level, hitResult.getLocation().x, hitResult.getLocation().y + 0.1, hitResult.getLocation().z, slice);
                droppedItem.setDefaultPickUpDelay();
                level.addFreshEntity(droppedItem);
            }
            //result = this.useWithoutItem(blockState, level, blockPos, player, hitResult);
        }
        return result;
    }

//    @Override
//    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult hitResult) {
//        InteractionResult result = InteractionResult.FAIL;
//        if(!level.isClientSide) {
//            result = removeSlice(level, blockPos, blockState, player);
//            if(result == InteractionResult.SUCCESS_SERVER) {
//                ItemStack slice = new ItemStack(GeneralItemsRegistry.TIRAMISU_SLICE.get());
//                ItemEntity droppedItem = new ItemEntity(level, hitResult.getLocation().x, hitResult.getLocation().y + 0.1, hitResult.getLocation().z, slice);
//                droppedItem.setDefaultPickUpDelay();
//                level.addFreshEntity(droppedItem);
//            }
//        }
//        return result;
//    }
    private InteractionResult addSlice(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, Player player) {
        Integer slices = blockState.getValue(SLICES);
        if(slices >= MAX_SLICES) return InteractionResult.PASS;
        updateBlcokstate(levelAccessor, blockPos, blockState, player, blockState.getValue(SLICES) + 1);
        return InteractionResult.CONSUME;
    }

    private InteractionResult removeSlice(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, Player player) {
        updateBlcokstate(levelAccessor, blockPos, blockState, player, blockState.getValue(SLICES) - 1);
        return InteractionResult.CONSUME;
    }

    private static void updateBlcokstate(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, Player player, Integer newSlices) {
        if (newSlices > 0) {
            levelAccessor.setBlock(blockPos, blockState.setValue(SLICES, newSlices), 3);
        } else {
            levelAccessor.removeBlock(blockPos, false);
            levelAccessor.gameEvent(player, GameEvent.BLOCK_DESTROY, blockPos);
        }
    }

    static {
        MAX_SLICES = 16;
        SLICES = IntegerProperty.create("slices", 1, 16);
        FULL_SIGNAL = CakeBlock.getOutputSignal(0);
        SHAPE_BY_BITE = new VoxelShape[]{
                Block.box(11, 0, 2, 14, 4, 5),
                Block.box(11, 0, 2, 14, 4, 8),
                Block.box(11, 0, 2, 14, 4, 11),
                Block.box(11, 0, 2, 14, 4, 14),
                Block.box(8, 0, 2, 14, 4, 14),
                Block.box(8, 0, 2, 14, 4, 14),
                Block.box(8, 0, 2, 14, 4, 14),
                Block.box(8, 0, 2, 14, 4, 14),
                Block.box(5, 0, 2, 14, 4, 14),
                Block.box(5, 0, 2, 14, 4, 14),
                Block.box(5, 0, 2, 14, 4, 14),
                Block.box(5, 0, 2, 14, 4, 14),
                Block.box(2, 0, 2, 14, 4, 14),
                Block.box(2, 0, 2, 14, 4, 14),
                Block.box(2, 0, 2, 14, 4, 14),
                Block.box(2, 0, 2, 14, 4, 14),
        };
    }
}
