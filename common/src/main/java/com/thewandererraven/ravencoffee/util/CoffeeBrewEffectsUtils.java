package com.thewandererraven.ravencoffee.util;

import com.thewandererraven.ravenbrewslib.brew.data.BrewEffectDefinition;
import com.thewandererraven.ravenbrewslib.utils.BrewEffectsUtils;
import com.thewandererraven.ravencoffee.datacomponents.CoffeeBrewData;
import com.thewandererraven.ravencoffee.datacomponents.DataComponentTypes;
import com.thewandererraven.ravencoffee.item.GeneralItemsRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CoffeeBrewEffectsUtils extends BrewEffectsUtils {

    public static ItemStack createBrewItemStack(Item item, CoffeeBrewData data) {
        ItemStack retStack = new ItemStack(item);
        retStack.set(DataComponentTypes.COFFEE_BREW.get(), data);
        return retStack;
    }

    public static ItemStack createBrewItemStack(Item item, ResourceLocation brewVariant, int caffeine, List<BrewEffectDefinition> effects) {
        ItemStack retStack = new ItemStack(item);
        retStack.set(DataComponentTypes.COFFEE_BREW.get(), new CoffeeBrewData(brewVariant, caffeine, effects));
        return retStack;
    }

    public static ItemStack createBrewItemStack(Item item, ResourceLocation brewVariant, int caffeine) {
        return createBrewItemStack(item, brewVariant,caffeine, new ArrayList<>());
    }

    public static ItemStack createBrewItemStack(CoffeeBrewData data) {
        return createBrewItemStack(GeneralItemsRegistry.COFFEE_BREW.get(), data);
    }

    public static ItemStack createBrewItemStack(ResourceLocation brewVariant, int caffeine, List<BrewEffectDefinition> effects) {
        return createBrewItemStack(GeneralItemsRegistry.COFFEE_BREW.get(), brewVariant, caffeine, effects);
    }

    public static ItemStack createBrewItemStack(ResourceLocation brewVariant, int caffeine) {
        return createBrewItemStack(GeneralItemsRegistry.COFFEE_BREW.get(), brewVariant, caffeine);
    }

    public static ItemStack createEmptyBrewItemStack() {
        return createBrewItemStack(CoffeeBrewData.DEFAULT);
    }

    public static CoffeeBrewData getItemBrewDataComponent(ItemStack stack) {
        CoffeeBrewData data = stack.get(DataComponentTypes.COFFEE_BREW.get());
        if(data == null)
            data = CoffeeBrewData.BROKEN;
        return data;
    }
}
