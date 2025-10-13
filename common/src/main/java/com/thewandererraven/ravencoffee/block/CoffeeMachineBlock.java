package com.thewandererraven.ravencoffee.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CoffeeMachineBlock extends HorizontalDirectionalBlock {

    public static final MapCodec<CoffeeMachineBlock> CODEC = simpleCodec(CoffeeMachineBlock::new);
    public static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 15, 15);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final BooleanProperty ENABLED = BooleanProperty.create("enabled");
    public static final BooleanProperty HAS_INPUT_CUP = BooleanProperty.create("has_input_cup");
    public static final BooleanProperty HAS_COFFEE = BooleanProperty.create("has_coffee");
    public static final BooleanProperty HAS_OUTPUT = BooleanProperty.create("has_output");

    public CoffeeMachineBlock(Properties p_54120_) {
        super(p_54120_);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(ENABLED, true)
                .setValue(ACTIVE, false)
                .setValue(HAS_OUTPUT, false)
                .setValue(HAS_INPUT_CUP, false)
                .setValue(HAS_COFFEE, false)
        );

    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, ENABLED, ACTIVE, HAS_OUTPUT, HAS_INPUT_CUP, HAS_COFFEE});
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
