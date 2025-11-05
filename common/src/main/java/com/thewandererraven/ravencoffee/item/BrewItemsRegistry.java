package com.thewandererraven.ravencoffee.item;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.effect.breweffect.MultiEffectsRegistry;
import com.thewandererraven.ravencoffee.registry.RegistryObject;
import com.thewandererraven.ravencoffee.registry.RegistryProvider;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumables;

public class BrewItemsRegistry {
    public static final RegistryProvider<Item> ITEMS = RegistryProvider.get(Registries.ITEM, Constants.MOD_ID);

    // ############################################### BASIC COFFEE STUFF ##############################################

    public static final String _coffee_mug_id = "coffee_mug";
    public static final RegistryObject<Item> COFFEE_MUG =
            ITEMS.register(_coffee_mug_id, () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _coffee_mug_id)
                    ))
            ));

    public static final String _basic_brew_mug_id = "basic_brew_mug";
    public static final RegistryObject<Item> BASIC_BREW_MUG =
            ITEMS.register(_basic_brew_mug_id, () -> new BrewItem(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _basic_brew_mug_id)
                    )).component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK)
                    .usingConvertsTo(COFFEE_MUG.get()),
                    MultiEffectsRegistry.BASIC
            ));

    public static final String _melon_brew_mug_id = "melon_brew_mug";
    public static final RegistryObject<Item> MELON_BREW_MUG =
            ITEMS.register(_melon_brew_mug_id, () -> new BrewItem(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _melon_brew_mug_id)
                    )).component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK)
                    .usingConvertsTo(COFFEE_MUG.get()),
                    MultiEffectsRegistry.SPEED
            ));


    public static final String _honey_brew_mug_id = "honey_brew_mug";
    public static final RegistryObject<Item> HONEY_BREW_MUG =
            ITEMS.register(_honey_brew_mug_id, () -> new BrewItem(new Item.Properties()
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _honey_brew_mug_id)
                    )).component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK)
                    .usingConvertsTo(COFFEE_MUG.get()),
                    MultiEffectsRegistry.HEAL
            ));

    public static void init() {

    }
}
