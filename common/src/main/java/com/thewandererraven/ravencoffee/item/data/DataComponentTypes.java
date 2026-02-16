package com.thewandererraven.ravencoffee.item.data;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.registry.RegistryObject;
import com.thewandererraven.ravencoffee.registry.RegistryProvider;
import com.thewandererraven.ravencoffee.util.RavenCoffeeRegistryKeys;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

import java.util.function.Consumer;

public class DataComponentTypes {
    public static final RegistryProvider<DataComponentType<?>> DATA_COMPONENT_TYPES = RegistryProvider.get(Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID);
    public static final RegistryObject<DataComponentType<CoffeeBrewData>> COFFEE_BREW = DATA_COMPONENT_TYPES.register(
            "coffee_brew",
            () -> DataComponentType.<CoffeeBrewData>builder()
            .persistent(CoffeeBrewData.CODEC)
            .build()
            );

    public static void init() {

    }
}
