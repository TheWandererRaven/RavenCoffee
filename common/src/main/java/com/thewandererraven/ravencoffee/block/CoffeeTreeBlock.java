package com.thewandererraven.ravencoffee.block;

import com.thewandererraven.ravencoffee.item.GeneralItemsRegistry;
import com.thewandererraven.ravencoffee.platform.Services;
import com.thewandererraven.ravencoffee.util.RavenCoffeeGeneralUtils;
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
                float f = improvisedGetGrowthSpeed(this, serverLevel, blockPos);
//                float f = getGrowthSpeed(this, serverLevel, blockPos);
                if (randomSource.nextInt((int) (25.0F / f) + 1) == 0) {
                    this.growTree(serverLevel, blockPos, blockState, false);
                }
            }
        }
    }

    // TODO: Remove this later ----- Something got updated and I was unable to cal the parent class method
    //                         ----- In NeoForge it is using blockstate instead of block, so this might change later
    public static float improvisedGetGrowthSpeed(Block block, BlockGetter level, BlockPos pos) {
        float f = 1.0F;
        BlockPos blockPos = pos.below();

        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                float g = 0.0F;
                BlockState blockState = level.getBlockState(blockPos.offset(i, 0, j));
                if (blockState.is(Blocks.FARMLAND)) {
                    g = 1.0F;
                    if ((Integer)blockState.getValue(FarmBlock.MOISTURE) > 0) {
                        g = 3.0F;
                    }
                }

                if (i != 0 || j != 0) {
                    g /= 4.0F;
                }

                f += g;
            }
        }

        BlockPos blockPos2 = pos.north();
        BlockPos blockPos3 = pos.south();
        BlockPos blockPos4 = pos.west();
        BlockPos blockPos5 = pos.east();
        boolean bl = level.getBlockState(blockPos4).is(block) || level.getBlockState(blockPos5).is(block);
        boolean bl2 = level.getBlockState(blockPos2).is(block) || level.getBlockState(blockPos3).is(block);
        if (bl && bl2) {
            f /= 2.0F;
        } else {
            boolean bl3 = level.getBlockState(blockPos4.north()).is(block) || level.getBlockState(blockPos5.north()).is(block) || level.getBlockState(blockPos5.south()).is(block) || level.getBlockState(blockPos4.south()).is(block);
            if (bl3) {
                f /= 2.0F;
            }
        }

        return f;
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
                        && !levelReader.getFluidState(blockPos.relative(direction)).is(FluidTags.LAVA));
        return false;
    }

    static {
        AGE = BlockStateProperties.AGE_3;
    }
}