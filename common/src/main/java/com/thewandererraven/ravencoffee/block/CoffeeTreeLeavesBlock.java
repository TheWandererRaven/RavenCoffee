package com.thewandererraven.ravencoffee.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CoffeeTreeLeavesBlock extends CoffeeTreeBlock {
    public static final IntegerProperty AGE;
    private static final VoxelShape[] SHAPE;

    public CoffeeTreeLeavesBlock(Properties p_i48421_1_) {
        super(p_i48421_1_);
        this.registerDefaultState(this.getStateDefinition().any().setValue(this.getAgeProperty(), 0));
    }

    @Override
    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AGE});
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE[blockState.getValue(this.getAgeProperty())];
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false; //blockState.is(BlocksRegistry.COFFEE_TREE_TRUNK.get());
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader blockGetter, BlockPos blockPos) {
        boolean flag = false;
        BlockState belowBlock = blockGetter.getBlockState(blockPos.below());
        if(belowBlock.is(BlocksRegistry.COFFEE_TREE_TRUNK.get()))
            flag = ((CoffeeTreeTrunkBlock)belowBlock.getBlock()).isMaxAge(belowBlock);
        return super.canSurvive(blockState, blockGetter, blockPos) && flag;
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos blockPos, BlockState state) {
        super.destroy(level, blockPos, state);
        if(!level.isClientSide()) {
            BlockState blockDown = level.getBlockState(blockPos.below());
            if (blockDown.is(BlocksRegistry.COFFEE_TREE_TRUNK.get()))
                if (blockDown.getValue(CoffeeTreeTrunkBlock.HAS_LEAVES))
                    level.setBlock(blockPos.below(), blockDown.setValue(CoffeeTreeTrunkBlock.HAS_LEAVES, false), 2);
        }
    }

    static {
        AGE = BlockStateProperties.AGE_3;
        SHAPE = new VoxelShape[]{
                Block.box(
                        4.0D,// BOTTOM
                        0.0D,// VOLUME BOTTOM
                        5.0D,// LEFT
                        12.0D,// TOP
                        8.0D,// VOLUME TOP
                        12.0D// RIGHT
                ),
                Block.box(
                        1.0D,
                        0.0D,
                        1.0D,
                        15.0D,
                        15.0D,
                        14.0D
                ),
                Block.box(
                        0.0D,
                        0.0D,
                        0.0D,
                        16.0D,
                        16.0D,
                        16.0D
                ),
                Block.box(
                        0.0D,
                        0.0D,// volume bottom
                        0.0D,
                        16.0D,// top
                        16.0D,// volume top
                        16.0D// right
                )
        };
    }
}