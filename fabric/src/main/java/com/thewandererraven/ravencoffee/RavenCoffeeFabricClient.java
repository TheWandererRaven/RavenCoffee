package com.thewandererraven.ravencoffee;

import com.thewandererraven.ravencoffee.menu.MenusRegistry;
import com.thewandererraven.ravencoffee.platform.Services;
import com.thewandererraven.ravencoffee.platform.services.IMenuFactory;
import com.thewandererraven.ravencoffee.platform.services.IScreenFactory;
import com.thewandererraven.ravencoffee.screen.CoffeeGrinderScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.Map;

public class RavenCoffeeFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(MenusRegistry.COFFEE_GRINDER.get(), CoffeeGrinderScreen::new);
    }

}
