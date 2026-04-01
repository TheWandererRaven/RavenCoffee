package com.thewandererraven.ravencoffee.menu;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.platform.Services;
import com.thewandererraven.ravencoffee.platform.services.IMenuFactory;
import com.thewandererraven.ravencoffee.platform.services.IScreenFactory;
import com.thewandererraven.ravencoffee.registry.RegistryObject;
import com.thewandererraven.ravencoffee.registry.RegistryProvider;
import com.thewandererraven.ravencoffee.screen.CoffeeBrewingStationScreen;
import com.thewandererraven.ravencoffee.screen.CoffeeGrinderScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class MenusRegistry {
    public static final RegistryProvider<MenuType<?>> MENUS = RegistryProvider.get(Registries.MENU, Constants.MOD_ID);

    public static final RegistryObject<MenuType<CoffeeGrinderMenu>> COFFEE_GRINDER = register(
            "coffee_grinder",
            () -> CoffeeGrinderMenu::new,
            () -> CoffeeGrinderScreen::new
    );

    public static final RegistryObject<MenuType<CoffeeBrewingStationMenu>> COFFEE_BREWING_STATION = register(
            "coffee_brewing_station",
            () -> CoffeeBrewingStationMenu::new,
            () -> CoffeeBrewingStationScreen::new
    );

    private static <T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> RegistryObject<MenuType<T>> register(String id, Supplier<IMenuFactory<T>> menuTypeFactory, Supplier<IScreenFactory<T, U>> screenFactory) {
        RegistryObject<MenuType<T>> registered_item = MENUS.register(id,
                () -> Services.REGISTRY_UTIL.buildMenuType(menuTypeFactory.get())
        );
        //TODO: Finish making this for ease of registering in the future, right now it doesn't matter since we have only 2 screens
        //MENUS_WITH_SCREENS.add(Map.entry(registered_item.get(), screenFactory));
        return registered_item;
    }

    public static void init() {
        //MENUS_WITH_SCREENS.add(Map.entry(COFFEE_GRINDER.get(), CoffeeGrinderScreen::new));
    }
}
