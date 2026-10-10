package com.thewandererraven.ravencoffee.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CoffeeTreeTrunkBlock extends CoffeeTreeBlock {
    public static final IntegerProperty AGE;
    public static final BooleanProperty HAS_LEAVES;
    private static final VoxelShape[] SHAPES;
    private static final Block LEAVES_BLOCK;

    public CoffeeTreeTrunkBlock(Properties p_i48421_1_) {
        super(p_i48421_1_);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(this.getAgeProperty(), 0)
                .setValue(HAS_LEAVES, false)
        );
    }

    @Override
    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPES[blockState.getValue(this.getAgeProperty())];
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AGE});
        builder.add(HAS_LEAVES);
    }

    public Block getLeavesBlock() {
        return LEAVES_BLOCK;
    }

    @Override
    protected SoundType getSoundType(BlockState state) {
        if(state.getValue(this.getAgeProperty()) <= 1)
            return SoundType.CHERRY_LEAVES;
        return super.getSoundType(state);
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter getter, BlockPos pos) {
        return floor.is(BlockTags.DIRT);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return !super.isMaxAge(state) || !state.getValue(HAS_LEAVES);
    }

    @Override
    public boolean canGrowByTick(BlockState blockState) {
        return !blockState.getValue(HAS_LEAVES) || !this.isMaxAge(blockState);
    }

    public void growLeaves(ServerLevel level, BlockPos trunkPos, BlockState trunkState, BlockPos leavesPos) {
        this.growLeaves(level, trunkPos, trunkState, leavesPos, 0);
    }

    public void growLeaves(ServerLevel level, BlockPos trunkPos, BlockState trunkState, BlockPos leavesPos, int age) {
        level.setBlock(trunkPos, trunkState.setValue(HAS_LEAVES, true), 2);
        level.setBlock(leavesPos, this.getLeavesBlock().defaultBlockState().setValue(CoffeeTreeTrunkBlock.AGE, age), 2);
    }

    @Override
    public void growTree(ServerLevel level, BlockPos pos, BlockState state, boolean isBonemealGrow) {
        if(!this.isMaxAge(state))
            super.growTree(level, pos, state, isBonemealGrow);
        if(state.getValue(AGE) >= this.getMaxAge() - 1 && !state.getValue(HAS_LEAVES))
            growLeaves(level, pos, level.getBlockState(pos), pos.above());
    }

    @Override
    public void growCrops(Level level, BlockPos blockPos, BlockState blockState) {
        super.growCrops(level, blockPos, blockState);
        BlockState newState = level.getBlockState(blockPos);
        if (isMaxAge(newState) && level instanceof ServerLevel serverLevel) {
            growLeaves(serverLevel, blockPos, newState, blockPos.above(), 0);
        }
    }

    // ##################################### BONEMEAL #####################################

    @Override
    public boolean isValidBonemealTarget(LevelReader reader, BlockPos blockPos, BlockState blockState) {
        BlockState aboveBlock = reader.getBlockState(blockPos.above());
        if(aboveBlock.is(this.getLeavesBlock())) {
            return !((CoffeeTreeLeavesBlock) aboveBlock.getBlock()).isMaxAge(aboveBlock);
        }
        return !this.isMaxAge(blockState);
    }

    public void applyGrowth(ServerLevel world, BlockPos pos, BlockState state) {
        super.applyGrowth(world, pos, state);
        BlockState trunkState = world.getBlockState(pos);
        if (isMaxAge(trunkState)) {
            growLeaves(world, pos, trunkState, pos.above());
        }
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos blockPos, BlockState blockState) {
        if (this.isMaxAge(blockState)) {
            BlockState aboveState = level.getBlockState(blockPos.above());
            if (aboveState.is(this.getLeavesBlock())) {
                CoffeeTreeLeavesBlock upBlock = ((CoffeeTreeLeavesBlock) aboveState.getBlock());
                if (upBlock.isBonemealSuccess(level, rand, blockPos.above(), aboveState)) {
                    upBlock.applyGrowth(level, blockPos.above(), aboveState);
                }
            } else {
                growLeaves(level, blockPos, blockState, blockPos.above(), getBonemealAgeIncrease(level) - 1);
            }
        } else
            this.growCrops(level, blockPos, blockState);
        BlockState latestState = level.getBlockState(blockPos);
    }

    static {
        AGE = BlockStateProperties.AGE_3;
        LEAVES_BLOCK = BlocksRegistry.COFFEE_TREE_LEAVES.get();
        HAS_LEAVES = BooleanProperty.create("has_leaves");
        SHAPES = new VoxelShape[]{
                Block.box(
                        6.0D,//
                        0.0D,// VOLUME BOTTOM
                        6.0D,//
                        10.0D,// TOP
                        5.0D,// VOLUME TOP
                        10.0D// RIGHT
                ), Block.box(
                4.0D,
                0.0D,
                4.0D,
                12.0D,
                8.0D,
                12.0D
        ), Block.box(
                3.0D,
                0.0D,
                3.0D,
                14.0D,
                15.0D,
                14.0D
        ), Block.box(
                1.0D,
                0.0D,// volume bottom
                1.0D,
                15.0D,// top
                16.0D,// volume top
                15.0D// right
        )
        };
    }
}