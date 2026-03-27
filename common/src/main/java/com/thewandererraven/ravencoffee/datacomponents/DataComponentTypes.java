package com.thewandererraven.ravencoffee.datacomponents;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.registry.RegistryObject;
import com.thewandererraven.ravencoffee.registry.RegistryProvider;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;

public class DataComponentTypes {
    public static final RegistryProvider<DataComponentType<?>> DATA_COMPONENT_TYPES = RegistryProvider.get(Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID);
    public static final RegistryObject<DataComponentType<CoffeeBrewData>> COFFEE_BREW = DATA_COMPONENT_TYPES.register(
            "coffee_brew",
            () -> DataComponentType.<CoffeeBrewData>builder()
                    .persistent(CoffeeBrewData.CODEC)
                    .networkSynchronized(CoffeeBrewData.STREAM_CODEC)
            .build()
            );

    public static void init() {

    }
}
