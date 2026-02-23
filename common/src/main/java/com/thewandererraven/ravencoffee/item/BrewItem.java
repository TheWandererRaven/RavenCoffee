package com.thewandererraven.ravencoffee.item;

import com.thewandererraven.ravencoffee.datacomponents.CoffeeBrewData;
import com.thewandererraven.ravencoffee.datacomponents.DataComponentTypes;
import com.thewandererraven.ravencoffee.platform.services.IBrewManagerHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class BrewItem extends Item {

    public BrewItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if(livingEntity instanceof Player player) {
            if(!level.isClientSide) {
                CoffeeBrewData data = stack.get(DataComponentTypes.COFFEE_BREW.get());
                if(data != null) {
                    ((IBrewManagerHolder) livingEntity).ravencoffee$getBrewEffectManager().add(data);
                }
            }
            if(!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

        }
        return stack.isEmpty() ? new ItemStack(BrewItemsRegistry.COFFEE_MUG.get()) : stack;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.DRINK;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if(player instanceof IBrewManagerHolder holder) {
            if(!holder.ravencoffee$getBrewEffectManager().getOverloadStatus())
                return ItemUtils.startUsingInstantly(level, player, hand);
        }
        return InteractionResult.FAIL;
    }
}
