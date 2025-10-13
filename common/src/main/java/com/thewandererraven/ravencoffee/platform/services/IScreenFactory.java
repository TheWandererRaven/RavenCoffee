package com.thewandererraven.ravencoffee.platform.services;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public interface IScreenFactory<T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> {
    default void fromPacket(Component title, MenuType<T> type, Minecraft mc, int windowId) {
        U u = this.create(type.create(windowId, mc.player.getInventory()), mc.player.getInventory(), title);
        mc.player.containerMenu = ((MenuAccess)u).getMenu();
        mc.setScreen(u);
    }

    U create(T menu, Inventory playerInventory, Component title);
}
