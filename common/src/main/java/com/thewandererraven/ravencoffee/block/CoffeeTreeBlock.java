package com.thewandererraven.ravencoffee.block;

import com.thewandererraven.ravencoffee.item.GeneralItemsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.Iterator;

public class CoffeeTreeBlock extends CropBlock implements BonemealableBlock {
    public static final IntegerProperty AGE;

    public CoffeeTreeBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(this.getAgeProperty(), 0));
    }

    // ##################################### AGE #####################################

    @Override
    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

    // ##################################### CROPS #####################################

    @Override
    protected ItemLike getBaseSeedId() {
        return GeneralItemsRegistry.COFFEE_CHERRIES.get();
    }

    // ##################################### TICKS #####################################

    public boolean canGrowByTick(BlockState blockState) {
        int age = this.getAge(blockState);
        return age < this.getMaxAge();
    }

    public void growTree(ServerLevel level, BlockPos pos, BlockState state, boolean isBonemealGrow) {
        int i = Math.min(this.getMaxAge(), this.getAge(state) + (isBonemealGrow ? this.getBonemealAgeIncrease(level) : 1));
        level.setBlock(pos, this.getStateForAge(i), 2);
    }

    @Override
    protected void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        if (serverLevel.getRawBrightness(blockPos, 0) >= 9) {
            int i = this.getAge(blockState);
            if (this.canGrowByTick(blockState)) {
                float f = getGrowthSpeed(this, serverLevel, blockPos);
                if (randomSource.nextInt((int) (25.0F / f) + 1) == 0) {
                    this.growTree(serverLevel, blockPos, blockState, false);
                }
            }
        }
    }

    // ##################################### BONEMEAL #####################################

    @Override
    protected int getBonemealAgeIncrease(Level level) {
        return Mth.nextInt(level.random, 1, 2);
    }

    public void applyGrowth(ServerLevel world, BlockPos pos, BlockState state) {
        int i = this.getAge(state) + this.getBonemealAgeIncrease(world);
        int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }
        world.setBlock(pos, state.setValue(AGE, i), 2);
    }

    // ##################################### WORLD #####################################

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
                        && !levelReader.getFluidState(blockPos.relative(direction)).is(FluidTags.LAVA) && super.hasSufficientLight(levelReader, blockPos));
        return false;
    }

    static {
        AGE = BlockStateProperties.AGE_3;
    }
}