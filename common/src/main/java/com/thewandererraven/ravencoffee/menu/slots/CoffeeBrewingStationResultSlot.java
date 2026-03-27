package com.thewandererraven.ravencoffee.menu.slots;

import com.thewandererraven.ravencoffee.menu.CoffeeBrewingStationMenu;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CoffeeBrewingStationResultSlot extends Slot {
    private final CoffeeBrewingStationMenu menu;
    public CoffeeBrewingStationResultSlot(Container container, int slot, int x, int y, CoffeeBrewingStationMenu menu) {
        super(container, slot, x, y);
        this.menu = menu;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        super.onTake(player, stack);
        menu.consumeIngredients(stack.getCount());
        menu.reorganizeSlots();
        menu.updateResultSlot();
    }
}
