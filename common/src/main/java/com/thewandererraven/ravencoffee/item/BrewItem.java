package com.thewandererraven.ravencoffee.item;

import com.mojang.serialization.Codec;
import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.datacomponents.CoffeeBrewData;
import com.thewandererraven.ravencoffee.datacomponents.DataComponentTypes;
import com.thewandererraven.ravencoffee.platform.services.IBrewManagerHolder;
import com.thewandererraven.ravencoffee.util.BrewEffectsUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class BrewItem extends Item {

    public BrewItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if(livingEntity instanceof Player player) {
            // If the player instance is from the server side, and the item has the brew data, add to the manager
            if(!level.isClientSide) {
                CoffeeBrewData data = stack.get(DataComponentTypes.COFFEE_BREW.get());
                if(data != null) {
                    ((IBrewManagerHolder) livingEntity).ravencoffee$getBrewEffectManager().add(data);
                }
            }
            // If player is in creative, don't shrink the stack
            if(!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

        }
        // Return the coffee mug
        // TODO: Check if this is already handled by the parent class, in the registry I'm passing the coffee mug as return item already
        return stack.isEmpty() ? new ItemStack(GeneralItemsRegistry.COFFEE_MUG.get()) : stack;
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
        if(level.isClientSide)
            return InteractionResult.PASS;
        if(player instanceof IBrewManagerHolder holder) {
            if(!holder.ravencoffee$getBrewEffectManager().getOverloadStatus())
                return ItemUtils.startUsingInstantly(level, player, hand);
        }
        return InteractionResult.FAIL;
    }

    @Override
    public Component getName(ItemStack stack) {
        // Get name for the coffee variant
        return Component.translatable(BrewEffectsUtils.getItemBrewDataComponent(stack).brewVariant().toLanguageKey());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> componentConsumer, TooltipFlag flag) {
        super.appendHoverText(stack, tooltipContext, tooltipDisplay, componentConsumer, flag);
        CoffeeBrewData brewData = stack.get(DataComponentTypes.COFFEE_BREW.get());
        stack.addToTooltip(DataComponentTypes.COFFEE_BREW.get(), tooltipContext, tooltipDisplay, componentConsumer, flag);
    }
}
