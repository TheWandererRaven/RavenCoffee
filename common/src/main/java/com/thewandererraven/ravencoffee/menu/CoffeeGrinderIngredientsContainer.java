package com.thewandererraven.ravencoffee.menu;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeInput;

public class CoffeeGrinderIngredientsContainer extends SimpleContainer implements RecipeInput {
    public final AbstractContainerMenu menu;

    public CoffeeGrinderIngredientsContainer() {
        this(null);
    }

    public CoffeeGrinderIngredientsContainer(AbstractContainerMenu menu) {
        super(2);
        this.menu = menu;
    }

    @Override
    public int size() {
        return 2;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if(menu != null)
            menu.slotsChanged(this);
    }
}
