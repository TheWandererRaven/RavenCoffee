package com.thewandererraven.ravencoffee.item;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.datacomponents.CoffeeBrewData;
import com.thewandererraven.ravencoffee.datacomponents.BrewEffectData;
import com.thewandererraven.ravencoffee.datacomponents.DataComponentTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class DebugMug extends Item {
    public DebugMug(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack1 = new ItemStack(GeneralItemsRegistry.COFFEE_BREW.get());
        ItemStack stack2 = new ItemStack(GeneralItemsRegistry.COFFEE_BREW.get());
        stack1.set(DataComponentTypes.COFFEE_BREW.get(), new CoffeeBrewData(
                BrewItem.BrewVariant.COOKIES_AND_CREAM,
                23 * 20,
                List.of(
                        new BrewEffectData(
                                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "effect.speed"),
                                5 * 20,
                                1,
                                0
                        ),
                        new BrewEffectData(
                                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "effect.slowness"),
                                10 * 20,
                                -0.2,
                                0
                        )
                )
        ));
        stack2.set(DataComponentTypes.COFFEE_BREW.get(), new CoffeeBrewData(
                BrewItem.BrewVariant.MELON_GOLDEN,
                23 * 20,
                List.of(
                        new BrewEffectData(
                                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "effect.heal"),
                                0,
                                5,
                                0
                        )
                )
        ));
        player.drop(stack1, true);
        player.drop(stack2, true);
        return super.use(level, player, hand);
    }
}
