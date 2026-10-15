package com.thewandererraven.ravencoffee.block;

import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RoscaDeReyesBlock extends Block {
    public static final int MAX_BITES = 11;
    public static final IntegerProperty BITES;
    public static final int FULL_SIGNAL;
    protected static final VoxelShape[] SHAPE_BY_BITE;

    public final int HUNGER_PER_BITE;
    public final float SATURATION_PER_BITE;

    public RoscaDeReyesBlock(Properties props, int hunger, float saturation) {
        super(props);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(BITES, 0));
        this.HUNGER_PER_BITE = hunger;
        this.SATURATION_PER_BITE = saturation;
    }

    public RoscaDeReyesBlock(Properties props) {
        this(props, 1, 1.0F);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{BITES});
    }

    protected int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        return CakeBlock.getOutputSignal((Integer)blockState.getValue(BITES));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_BITE[(Integer)state.getValue(BITES)];
    }

    @Override
    protected InteractionResult useItemOn(ItemStack p_316238_, BlockState p_316837_, Level p_316766_, BlockPos p_316227_, Player p_316853_, InteractionHand p_316422_, BlockHitResult p_316869_) {
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            if (eatRosca(level, blockPos, blockState, player).consumesAction()) {
                return InteractionResult.SUCCESS;
            }

            if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                return InteractionResult.CONSUME;
            }
        }

        InteractionResult eatRes = eatRosca(level, blockPos, blockState, player);

        int rand = level.getRandom().nextInt(5);//60000);
        if (rand < 1) {
            ItemEntity droppedItem = new ItemEntity(
                    player.level(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(Items.TOTEM_OF_UNDYING)
            );
            droppedItem.setNoPickUpDelay();
            level.addFreshEntity(droppedItem);
        }
        return eatRes;
    }

    public InteractionResult eatRosca(LevelAccessor level, BlockPos pos, BlockState state, Player player) {
        if (!player.canEat(false)) {
            return InteractionResult.PASS;
        } else {
            player.awardStat(Stats.EAT_CAKE_SLICE);
            player.getFoodData().eat(this.HUNGER_PER_BITE, this.SATURATION_PER_BITE);
            int i = (Integer)state.getValue(BITES);
            level.gameEvent(player, GameEvent.EAT, pos);
            if (i < this.MAX_BITES) {
                level.setBlock(pos, (BlockState)state.setValue(BITES, i + 1), 3);
            } else {
                level.removeBlock(pos, false);
                level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            }

            return InteractionResult.SUCCESS;
        }
    }

    static {
        BITES = IntegerProperty.create("bites", 0, 11);
        FULL_SIGNAL = CakeBlock.getOutputSignal(0);
        SHAPE_BY_BITE = new VoxelShape[]{
                Block.box(0, 0, 0, 16, 4, 16),
                Block.box(0, 0, 0, 16, 4, 16),
                Block.box(0, 0, 0, 16, 4, 16),
                Block.box(0, 0, 0, 16, 4, 16),
                Block.box(4, 0, 0, 16, 4, 16),
                Block.box(4, 0, 0, 16, 4, 16),
                Block.box(4, 0, 0, 16, 4, 16),
                Block.box(4, 0, 0, 16, 4, 12),
                Block.box(4, 0, 0, 16, 4, 8),
                Block.box(4, 0, 0, 16, 4, 4),
                Block.box(4, 0, 0, 12, 4, 4),
                Block.box(4, 0, 0, 8, 4, 4)
        };
    }
}
