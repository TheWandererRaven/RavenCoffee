package com.thewandererraven.ravencoffee.item;

import com.thewandererraven.ravencoffee.entitites.ThrowableFoodEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.SnowballItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class MuffinItem extends Item implements ProjectileItem {
    public MuffinItem(Properties props) {
        super(props);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        super.onUseTick(level, livingEntity, stack, remainingUseDuration);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return super.useOn(context);
//        Player player = context.getPlayer();
//        InteractionHand hand = context.getHand();
//        ItemStack itemInHand = context.getItemInHand();
//
//        return this.eatMuffin(player, hand, itemInHand);
    }

    private InteractionResult eatMuffin(Player player, InteractionHand hand, ItemStack itemInHand) {
        if(player == null) return InteractionResult.FAIL;
        FoodProperties foodProps = this.components().getOrDefault(DataComponents.FOOD, null);
        if (player.canEat(foodProps.canAlwaysEat())) {
            player.startUsingItem(hand);
            return InteractionResult.CONSUME.heldItemTransformedTo(itemInHand);
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);

        if(player.isCrouching())
            return eatMuffin(player, hand, itemInHand);

        level.playSound((Entity)null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        if (level instanceof ServerLevel serverLevel) {
            Projectile.spawnProjectileFromRotation(ThrowableFoodEntity::new, serverLevel, itemInHand, player, 0.0F, 1.5F, 0.25F);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.isCreative()) {
            itemInHand.shrink(1);
        }

        return InteractionResult.SUCCESS_SERVER;
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
        return null;
    }
}
