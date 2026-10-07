package com.thewandererraven.ravencoffee.block;

import com.thewandererraven.ravencoffee.item.GeneralItemsRegistry;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Iterator;

public class CoffeeTreeBlock extends CropBlock implements BonemealableBlock {
    public static final IntegerProperty AGE;
    private static final VoxelShape[] SHAPE_BY_AGE;

    public CoffeeTreeBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(this.getAgeProperty(), 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AGE});
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader p_304482_, BlockPos p_52255_, BlockState p_52256_, boolean p_387989_) {
        return new ItemStack(this.getBaseSeedId());
    }

    // ##################################### AGE #####################################

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

    @Override
    public int getAge(BlockState blockState) {
        return blockState.getValue(this.getAgeProperty());
    }

    @Override
    public BlockState getStateForAge(int age) {
        return this.defaultBlockState().setValue(this.getAgeProperty(), age);
    }

    // ##################################### CROPS #####################################

    @Override
    protected ItemLike getBaseSeedId() {
        return GeneralItemsRegistry.COFFEE_CHERRIES.get();
    }


//    @Override
//    public PlantType getPlantType(BlockGetter world, BlockPos pos) {
//        return PlantType.CROP;
//    }

//    @Override
//    public BlockState getPlant(BlockGetter world, BlockPos pos) {
//        return defaultBlockState();
//    }

    @Override
    public void growCrops(Level level, BlockPos blockPos, BlockState blockState) {
        int i = this.getAge(blockState) + this.getBonemealAgeIncrease(level);
        int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }

        level.setBlock(blockPos, blockState.setValue(AGE, i), 2);
    }

    // ##################################### TICKS #####################################

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        if (serverLevel.getLightEmission(blockPos) >= 9) {
            int i = this.getAge(blockState);
            if (i < this.getMaxAge()) {
                if (randomSource.nextInt((int) (25.0F) + 1) == 0) {
                    serverLevel.setBlock(blockPos, blockState.setValue(AGE, i + 1), 2);
                }
            }
        }
    }

    public void tickGrow(int age, BlockState blockState, ServerLevel server, BlockPos blockPos) {
        if (age < this.getMaxAge()) {
            server.setBlock(blockPos, blockState.setValue(AGE, age + 1), 2);
        }
    }

    public boolean isAboveBlockAcceptable(Level level, BlockPos blockPos) {
        // TODO: Check if the growing replaces the above block
        return true;
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel server, BlockPos blockPos, RandomSource rand) {
        if (server.isLoaded(blockPos))
            if (isAboveBlockAcceptable(server, blockPos))
                if (server.getRawBrightness(blockPos, 0) >= 9 || server.canSeeSky(blockPos)) {
                    if (rand.nextInt((int) (25.0F) + 1) == 0) {
                        this.tickGrow(blockState.getValue(AGE), blockState, server, blockPos);
                    }
                }
    }

    // ##################################### BONEMEAL #####################################

    @Override
    protected int getBonemealAgeIncrease(Level level) {
        return Mth.nextInt(level.random, 1, 3);
    }

    public void applyGrowth(ServerLevel world, BlockPos pos, BlockState state) {
        BlockState latestState = world.getBlockState(pos.below());
        int i = this.getAge(state) + this.getBonemealAgeIncrease(world);
        int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }
        latestState = world.getBlockState(pos.below());
        world.setBlock(pos, state.setValue(AGE, i), 2);
        latestState = world.getBlockState(pos.below());
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader reader, BlockPos blockPos, BlockState blockState) {
        return !this.isMaxAge(blockState);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel server, RandomSource rand, BlockPos blockPos, BlockState blockState) {
        this.growCrops(server, blockPos, blockState);
    }

    // ##################################### WORLD #####################################

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter getter, BlockPos pos) {
        return floor.is(BlockTags.DIRT);
    }

    public BlockState getBiomeGenState() {
        return this.stateDefinition.any().setValue(this.getAgeProperty(), 3);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        BlockPos belowPos = blockPos.below();

        Iterator<Direction> var4 = Direction.Plane.HORIZONTAL.iterator();
        Direction direction;
        BlockState blockstate;

        do {
            if (!var4.hasNext()) {
                return this.mayPlaceOn(levelReader.getBlockState(belowPos), levelReader, belowPos);
            }
            direction = var4.next();
            blockstate = levelReader.getBlockState(blockPos.relative(direction));
        } while (
                (!blockstate.isSolid() || blockstate.is(BlockTags.LEAVES) || blockstate.is(BlockTags.FENCES) || blockstate.is(BlockTags.FENCE_GATES) || blockstate.is(Blocks.JIGSAW))
                        && !levelReader.getFluidState(blockPos.relative(direction)).is(FluidTags.LAVA));
        return false;
    }

    // ##################################### BLOCK SHAPE #####################################

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE_BY_AGE[blockState.getValue(this.getAgeProperty())];
    }

//    @Override
//    protected BlockState updateShape(BlockState blockState, LevelReader levelReader, ScheduledTickAccess tickAccess, BlockPos blockPos, Direction direction, BlockPos firstPos, BlockState secondPos, RandomSource rand) {
//        if (!blockState.canSurvive(levelReader, blockPos)) {
//            levelReader..scheduleTick(pos, this, 1);
//        }
//
//        return super.updateShape(blockState, p_51158_, secondState, levelAccessor, pos, secondPos);
//    }

    static {
        AGE = BlockStateProperties.AGE_3;
        SHAPE_BY_AGE = new VoxelShape[]{
                Block.box(
                        0.0D,
                        0.0D,// volume bottom
                        0.0D,
                        16.0D,// top
                        16.0D,// volume top
                        16.0D// right
                ),
                Block.box(
                        0.0D,
                        0.0D,// volume bottom
                        0.0D,
                        16.0D,// top
                        16.0D,// volume top
                        16.0D// right
                ),
                Block.box(
                        0.0D,
                        0.0D,// volume bottom
                        0.0D,
                        16.0D,// top
                        16.0D,// volume top
                        16.0D// right
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