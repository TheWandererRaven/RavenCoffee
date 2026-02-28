package com.thewandererraven.ravencoffee.util;

import com.thewandererraven.ravencoffee.datacomponents.BrewEffectData;
import com.thewandererraven.ravencoffee.datacomponents.CoffeeBrewData;
import com.thewandererraven.ravencoffee.datacomponents.DataComponentTypes;
import com.thewandererraven.ravencoffee.effect.breweffect.BrewEffectCore;
import com.thewandererraven.ravencoffee.effect.breweffect.BrewEffectCoresRegistry;
import com.thewandererraven.ravencoffee.item.BrewItem;
import com.thewandererraven.ravencoffee.item.GeneralItemsRegistry;
import com.thewandererraven.ravencoffee.registry.RegistryObject;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class BrewEffectsUtils {
    public static BrewEffectCore findEffectInRegistry(ResourceLocation effectId) {
        RegistryObject<BrewEffectCore> foundEffect = BrewEffectCoresRegistry.BREW_EFFECT_CORES.getEntries().stream()
                .filter(param -> param.getId().equals(effectId)).findFirst()
                .orElse(null);
        if(foundEffect != null)
            return foundEffect.get();
        return null;
    }

    public static Holder<Attribute> findAttributeByItsId(Level level, ResourceLocation attributeId) {
        Registry<Attribute> reg = level.registryAccess().lookup(Registries.ATTRIBUTE).orElse(null);
        if(reg == null)
            return null;
        return reg.get(attributeId).orElse(null);
    }

    public static ItemStack createBrewItemStack(Item item, CoffeeBrewData data) {
        ItemStack retStack = new ItemStack(item);
        retStack.set(DataComponentTypes.COFFEE_BREW.get(), data);
        return retStack;
    }

    public static ItemStack createBrewItemStack(Item item, BrewItem.BrewVariant brewVariant, int caffeine, List<BrewEffectData> effects) {
        ItemStack retStack = new ItemStack(item);
        retStack.set(DataComponentTypes.COFFEE_BREW.get(), new CoffeeBrewData(brewVariant, caffeine, effects));
        return retStack;
    }

    public static ItemStack createBrewItemStack(Item item, BrewItem.BrewVariant brewVariant, int caffeine) {
        return createBrewItemStack(item, brewVariant,caffeine, new ArrayList<>());
    }

    public static ItemStack createBrewItemStack(CoffeeBrewData data) {
        return createBrewItemStack(GeneralItemsRegistry.COFFEE_BREW.get(), data);
    }

    public static ItemStack createBrewItemStack(BrewItem.BrewVariant brewVariant, int caffeine, List<BrewEffectData> effects) {
        return createBrewItemStack(GeneralItemsRegistry.COFFEE_BREW.get(), brewVariant, caffeine, effects);
    }

    public static ItemStack createBrewItemStack(BrewItem.BrewVariant brewVariant, int caffeine) {
        return createBrewItemStack(GeneralItemsRegistry.COFFEE_BREW.get(), brewVariant, caffeine);
    }

    public static ItemStack createEmptyBrewItemStack() {
        return createBrewItemStack(CoffeeBrewData.EMPTY);
    }

    public static CoffeeBrewData getItemBrewDataComponent(ItemStack stack) {
        CoffeeBrewData data = stack.get(DataComponentTypes.COFFEE_BREW.get());
        if(data == null)
            data = CoffeeBrewData.BROKEN;
        return data;
    }
}
