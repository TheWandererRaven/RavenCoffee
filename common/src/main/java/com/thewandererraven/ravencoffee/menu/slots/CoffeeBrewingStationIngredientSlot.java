package com.thewandererraven.ravencoffee.menu.slots;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CoffeeBrewingStationIngredientSlot extends Slot {
    public AbstractContainerMenu menu;
    private boolean isHidden = false;

    public CoffeeBrewingStationIngredientSlot(AbstractContainerMenu menu, Container container, int slot, int x, int y) {
        super(container, slot, x, y);
        this.menu = menu;
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

    @Override
    public void setChanged() {
        super.setChanged();
        this.menu.slotsChanged(this.container);
    }
}
