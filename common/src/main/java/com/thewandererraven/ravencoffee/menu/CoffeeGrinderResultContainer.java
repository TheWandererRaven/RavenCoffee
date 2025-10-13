package com.thewandererraven.ravencoffee.menu;

import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;

public class CoffeeGrinderResultContainer extends ResultContainer {
    public CoffeeGrinderMenu menu;

    public CoffeeGrinderResultContainer(CoffeeGrinderMenu menu) {
        super();
        this.menu = menu;
    }

    public CoffeeGrinderResultContainer() {
        this(null);
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack retItem = super.removeItem(index, count);
        if(menu != null)
            menu.craftedOutput(count);
        return retItem;
    }
}
