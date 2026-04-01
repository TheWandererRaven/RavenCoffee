package com.thewandererraven.ravencoffee.item;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.datacomponents.BrewEffectData;
import com.thewandererraven.ravencoffee.datacomponents.CoffeeBrewData;
import com.thewandererraven.ravencoffee.registry.RegistryObject;
import com.thewandererraven.ravencoffee.registry.RegistryProvider;
import com.thewandererraven.ravencoffee.util.BrewEffectsUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ModCreativeModTabs {
    public static final RegistryProvider<CreativeModeTab> CREATIVE_MODE_TABS = RegistryProvider.get(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);

    public static final String general_items_tab_id = "general_items_tab";
    public static final RegistryObject<CreativeModeTab> GENERAL_ITEMS_TAB =
            CREATIVE_MODE_TABS.register(general_items_tab_id, () -> CreativeModeTab.builder(null, -1)
                    .icon(() -> new ItemStack(GeneralItemsRegistry.ROASTED_COFFEE_BEANS.get()))
                    .title(Component.translatable("itemgroup." + Constants.MOD_ID + "." + general_items_tab_id))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(GeneralItemsRegistry.DEBUG_MUG.get());
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
                        output.accept(GeneralItemsRegistry.COFFEE_BREWING_STATION.get());
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
                    .icon(BrewEffectsUtils::createEmptyBrewItemStack)
                    .title(Component.translatable("itemgroup." + Constants.MOD_ID + "." + mug_brews_items_tab_id))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(BrewEffectsUtils.createBrewItemStack(
                                new CoffeeBrewData(
                                        ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "basic"),
                                        10 * 20,
                                        BrewEffectData.getListOfBasicEffects().stream().map(BrewEffectData.Builder::build).toList()
                                )
                        ));
                        output.accept(BrewEffectsUtils.createBrewItemStack(
                                new CoffeeBrewData(
                                        ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "apple"),
                                        23 * 20,
                                        List.of(
                                                new BrewEffectData(
                                                        ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "effect.speed"),
                                                        5 * 20,
                                                        1,
                                                        0
                                                ),
                                                new BrewEffectData(
                                                        ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "effect.slowness"),
                                                        5 * 20,
                                                        -0.2,
                                                        0
                                                )
                                        )
                                )
                        ));
                        output.accept(BrewEffectsUtils.createBrewItemStack(
                                new CoffeeBrewData(
                                        ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "melon"),
                                        15 * 20,
                                        List.of(
                                                new BrewEffectData(
                                                        ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "effect.heal"),
                                                        0,
                                                        5,
                                                        0
                                                )
                                        )
                                )
                        ));
                        output.accept(BrewEffectsUtils.createBrewItemStack(
                                new CoffeeBrewData(
                                        ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "honey"),
                                        30 * 20,
                                        List.of(
                                                new BrewEffectData(
                                                        ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "effect.absorption"),
                                                        10 * 20,
                                                        8.0,
                                                        0
                                                ),
                                                new BrewEffectData(
                                                        ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "effect.hurt"),
                                                        0,
                                                        8.0,
                                                        0
                                                )
                                        )
                                )
                        ));
                        output.accept(BrewEffectsUtils.createBrewItemStack(
                                new CoffeeBrewData(
                                        ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "cookies_and_cream"),
                                        30 * 20,
                                        List.of(
                                                new BrewEffectData(
                                                        ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "effect.attack_speed"),
                                                        20 * 20,
                                                        3.0,
                                                        0
                                                ),
                                                new BrewEffectData(
                                                        ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "effect.slowness"),
                                                        5 * 20,
                                                        -0.2,
                                                        0
                                                ),
                                                new BrewEffectData(
                                                        ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "effect.strong_legs"),
                                                        15 * 20,
                                                        -1.0,
                                                        0
                                                ),
                                                new BrewEffectData(
                                                        ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "effect.weak_legs"),
                                                        20 * 20,
                                                        1.5,
                                                        0
                                                )
                                        )
                                )
                        ));
                    }).build()
            );

    public static void init() {
    }

}
