package com.thewandererraven.ravencoffee.menu.slots;

import com.thewandererraven.ravencoffee.util.RavenCoffeeTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CoffeeBrewingStationIngredientSlot extends Slot {
    private boolean isHidden = false;

    public CoffeeBrewingStationIngredientSlot(AbstractContainerMenu menu, Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.is(RavenCoffeeTags.Items.COFFEE_BREW_INGREDIENT);
    }

    @Override
    public boolean isActive() {
        return !isHidden;
    }

    public void hide() {
        isHidden = true;
    }

    public void show() {
        isHidden = false;
    }
}
