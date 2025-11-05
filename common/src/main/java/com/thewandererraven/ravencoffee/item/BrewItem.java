package com.thewandererraven.ravencoffee.item;

import com.thewandererraven.ravencoffee.effect.breweffect.CoffeeBrewEffect;
import com.thewandererraven.ravencoffee.effect.breweffect.CoffeeBrewEffectInstance;
import com.thewandererraven.ravencoffee.platform.services.IBrewManagerHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class BrewItem extends Item {
    private Supplier<CoffeeBrewEffect> effectHolder;

    public BrewItem(Properties properties, Supplier<CoffeeBrewEffect> holder) {
        super(properties);
        this.effectHolder = holder;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if(livingEntity instanceof Player player) {
            if(!level.isClientSide) {
                ((IBrewManagerHolder) livingEntity).ravencoffee$getBrewEffectManager().add(
                        new CoffeeBrewEffectInstance(effectHolder.get().asHolder())
                );
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
        return ItemUtils.startUsingInstantly(level, player, hand);
    }
}
