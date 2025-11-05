package com.thewandererraven.ravencoffee.item;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.registry.RegistryObject;
import com.thewandererraven.ravencoffee.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModTabs {
    public static final RegistryProvider<CreativeModeTab> CREATIVE_MODE_TABS = RegistryProvider.get(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);

    public static final String general_items_tab_id = "general_items_tab";
    public static final RegistryObject<CreativeModeTab> GENERAL_ITEMS_TAB =
            CREATIVE_MODE_TABS.register(general_items_tab_id, () -> CreativeModeTab.builder(null, -1)
                    .icon(() -> new ItemStack(GeneralItemsRegistry.ROASTED_COFFEE_BEANS.get()))
                    .title(Component.translatable("itemgroup." + Constants.MOD_ID + "." + general_items_tab_id))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(GeneralItemsRegistry.COFFEE_CHERRIES.get());
                        output.accept(GeneralItemsRegistry.ROASTED_COFFEE_BEANS.get());
                        output.accept(GeneralItemsRegistry.MAGMA_COFFEE_BEANS.get());
                        output.accept(GeneralItemsRegistry.GROUND_COFFEE.get());
                        output.accept(GeneralItemsRegistry.GROUND_MAGMA_COFFEE.get());
                        output.accept(GeneralItemsRegistry.POPCHORUS.get());
                        output.accept(GeneralItemsRegistry.MUFFIN.get());
                        output.accept(GeneralItemsRegistry.MELON_PAN.get());
                        output.accept(GeneralItemsRegistry.COFFEE_ECLAIR.get());
                        output.accept(GeneralItemsRegistry.BROWNIE.get());
                        output.accept(GeneralItemsRegistry.BROWNIE_BLOCK_ITEM.get());
                        output.accept(GeneralItemsRegistry.COFFEE_GRINDER_ITEM.get());
                        output.accept(GeneralItemsRegistry.COFFEE_MACHINE_ITEM.get());
                        output.accept(GeneralItemsRegistry.TIRAMISU_SLICE.get());
                        output.accept(GeneralItemsRegistry.HAM_SANDWICH.get());
                        output.accept(GeneralItemsRegistry.BEEF_SANDWICH.get());
                        output.accept(GeneralItemsRegistry.CHICKEN_SANDWICH.get());
                        output.accept(GeneralItemsRegistry.CROISSANT.get());
                        output.accept(GeneralItemsRegistry.HAM_CROISSANT.get());
                        output.accept(GeneralItemsRegistry.BEEF_CROISSANT.get());
                        output.accept(GeneralItemsRegistry.CHICKEN_CROISSANT.get());
                        output.accept(GeneralItemsRegistry.BAGEL.get());
                        output.accept(GeneralItemsRegistry.HAM_BAGEL.get());
                        output.accept(GeneralItemsRegistry.BEEF_BAGEL.get());
                        output.accept(GeneralItemsRegistry.CHICKEN_BAGEL.get());
                    })
                    .build()
            );

    public static final String mug_brews_items_tab_id = "mug_brews_items_tab";
    public static final RegistryObject<CreativeModeTab> MUG_BREWS_ITEMS_TAB =
            CREATIVE_MODE_TABS.register(mug_brews_items_tab_id, () -> CreativeModeTab.builder(null, -1)
                    .icon(() -> new ItemStack(BrewItemsRegistry.BASIC_BREW_MUG.get()))
                    .title(Component.translatable("itemgroup." + Constants.MOD_ID + "." + mug_brews_items_tab_id))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(BrewItemsRegistry.COFFEE_MUG.get());
                        output.accept(BrewItemsRegistry.BASIC_BREW_MUG.get());
                        output.accept(BrewItemsRegistry.MELON_BREW_MUG.get());
                        output.accept(BrewItemsRegistry.HONEY_BREW_MUG.get());
                    })
                    .build()
            );

    public static void init() {
    }

}
