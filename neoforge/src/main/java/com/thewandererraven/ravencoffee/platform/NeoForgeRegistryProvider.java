package com.thewandererraven.ravencoffee.platform;

import com.thewandererraven.ravencoffee.platform.services.IRegistryFactory;
import com.thewandererraven.ravencoffee.registry.RegistryObject;
import com.thewandererraven.ravencoffee.registry.RegistryProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public class NeoForgeRegistryProvider implements IRegistryFactory {

    @Override
    public <T> RegistryProvider<T> create(ResourceKey<? extends Registry<T>> resourceKey,
                                          String modId) {
        final var containerOpt = ModList.get().getModContainerById(modId);
        if (containerOpt.isEmpty()) {
            throw new NullPointerException("Cannot find mod container for id " + modId);
        }
        final var cont = containerOpt.get();
        if (cont instanceof FMLModContainer fmlModContainer) {
            final var register = DeferredRegister.create(resourceKey, modId);
            register.register(Objects.requireNonNull(fmlModContainer.getEventBus()));
            return new Provider<>(modId, register);
        } else {
            throw new ClassCastException("The container of the mod " + modId + " is not a FML one!");
        }
    }

    @Override
    public <T> RegistryProvider<T> createRegistry(ResourceKey<? extends Registry<T>> resourceKey, String modId, Class<T> type) {
        RegistryProvider<T> provider = create(resourceKey, modId);
        provider.makeRegistry();
        return provider;
    }

    public static class Provider<T> implements RegistryProvider<T> {
        private final String modId;
        private final DeferredRegister<T> registry;

        private final Set<RegistryObject<T>> entries = new HashSet<>();
        private final Set<RegistryObject<T>> entriesView = Collections.unmodifiableSet(entries);

        private Provider(String modId, DeferredRegister<T> registry) {
            this.modId = modId;
            this.registry = registry;
        }

        @Override
        public String getModId() {
            return this.modId;
        }

        @Override
        public Registry<T> makeRegistry() {
            return this.registry.makeRegistry(builder -> {});
        }

        @Override
        @SuppressWarnings("unchecked")
        public <I extends T> RegistryObject<I> register(String name, Supplier<? extends I> supplier) {
            final DeferredHolder<T, I> obj = this.registry.register(name, supplier);
            final RegistryObject<I> ro = new RegistryObject<>() {

                @Override
                public ResourceKey<I> getResourceKey() {
                    return (ResourceKey<I>) obj.getKey();
                }

                @Override
                public ResourceLocation getId() {
                    return obj.getId();
                }

                @Override
                public I get() {
                    return obj.get();
                }

                @Override
                public Holder<I> asHolder() {
                    return (Holder<I>) obj;
                }
            };
            this.entries.add((RegistryObject<T>) ro);
            return ro;
        }

        @Override
        public Set<RegistryObject<T>> getEntries() {
            return this.entriesView;
        }
    }
}
