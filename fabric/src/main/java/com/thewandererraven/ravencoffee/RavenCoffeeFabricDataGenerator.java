package com.thewandererraven.ravencoffee;

import com.thewandererraven.ravencoffee.datagen.*;
import com.thewandererraven.ravencoffee.worldgen.RavenCoffeeWorldConfiguredFeatures;
import com.thewandererraven.ravencoffee.worldgen.RavenCoffeeWorldPlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class RavenCoffeeFabricDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModModelProvider::new);
        pack.addProvider(ModLootTableProvider::new);
        pack.addProvider(ModBlockTagProvider::new);
        pack.addProvider(ModItemTagProvider::new);
        pack.addProvider(ModRecipeProvider::new);
        pack.addProvider(ModWorldGenProvider::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        DataGeneratorEntrypoint.super.buildRegistry(registryBuilder);
        registryBuilder.add(Registries.CONFIGURED_FEATURE, RavenCoffeeWorldConfiguredFeatures::configure);
        registryBuilder.add(Registries.PLACED_FEATURE, RavenCoffeeWorldPlacedFeatures::configure);
    }
}
