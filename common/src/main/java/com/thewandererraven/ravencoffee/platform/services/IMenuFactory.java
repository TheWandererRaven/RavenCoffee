package com.thewandererraven.ravencoffee.platform.services;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public interface IMenuFactory<T extends AbstractContainerMenu> {
    T create(int syncId, Inventory inv);
}
