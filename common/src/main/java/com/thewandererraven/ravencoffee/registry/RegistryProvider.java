package com.thewandererraven.ravencoffee.registry;

import com.thewandererraven.ravencoffee.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.function.Supplier;

public interface RegistryProvider<T> {

    static <T> RegistryProvider<T> get(ResourceKey<? extends Registry<T>> resourceKey, String modId) {
        return Services.REGISTRY_FACTORY.create(resourceKey, modId);
    }

    static <T> RegistryProvider<T> get(Registry<T> registry, String modId) {
        return Services.REGISTRY_FACTORY.create(registry, modId);
    }

    static <T> RegistryProvider<T> create(ResourceKey<Registry<T>> resourceKey, String modId, Class<T> type) {
        return Services.REGISTRY_FACTORY.createRegistry(resourceKey, modId, type);
    }

    <I extends T> RegistryObject<I> register(String name, Supplier<? extends I> supplier);

    Collection<RegistryObject<T>> getEntries();

    public Registry<T> makeRegistry();

    String getModId();
}
