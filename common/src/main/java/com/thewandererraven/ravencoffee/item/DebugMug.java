package com.thewandererraven.ravencoffee.item;

import com.thewandererraven.ravencoffee.item.data.CoffeeBrewData;
import com.thewandererraven.ravencoffee.item.data.CoffeeBrewEffectData;
import com.thewandererraven.ravencoffee.item.data.DataComponentTypes;
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
        ItemStack newStack = new ItemStack(BrewItemsRegistry.BASIC_BREW_MUG.get());
        newStack.set(DataComponentTypes.COFFEE_BREW.get(), new CoffeeBrewData(
                23,
                1.0,
                List.of(
                        new CoffeeBrewEffectData(
                                "speed",
                                "attribute_modifier",
                                25
                        ),
                        new CoffeeBrewEffectData(
                                "slowness",
                                "attribute_modifier",
                                -30// TODO: CHANGE TO JUST THE VALUE
                        )
                )
        ));
        player.drop(newStack, true);
        return super.use(level, player, hand);
    }
}
