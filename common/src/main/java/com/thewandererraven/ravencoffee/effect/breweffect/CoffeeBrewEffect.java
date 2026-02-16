package com.thewandererraven.ravencoffee.effect.breweffect;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.util.RavenCoffeeRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class CoffeeBrewEffect extends MultiEffect {
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<CoffeeBrewEffect>> STREAM_CODEC;
    public final int caffeineContent;

    public CoffeeBrewEffect(ResourceLocation id, ResourceLocation iconLocation, int caffeineContent, List<TriggerableEffect> effects) {
        super(id, iconLocation, effects);
        this.caffeineContent = caffeineContent;
    }

    public static CoffeeBrewEffect asEmpty() {
        return new CoffeeBrewEffect(null, null, 0, new ArrayList<>());
    }

    public static CoffeeBrewEffect of(String id, TriggerableEffect effect) {
        return CoffeeBrewEffect.of(id, 10, List.of(effect));
    }

    public static CoffeeBrewEffect of(String id, int caffeineContent, TriggerableEffect effect) {
        return CoffeeBrewEffect.of(id, caffeineContent, List.of(effect));
    }

    public static CoffeeBrewEffect of(String id, int caffeineContent, List<TriggerableEffect> effects) {
        return CoffeeBrewEffect.of(id, id, caffeineContent, effects);
    }

    public static CoffeeBrewEffect of(String id, String iconLocation, int caffeineContent, List<TriggerableEffect> effects) {
        return new CoffeeBrewEffect(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, id),
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/item/coffee_mug/" + iconLocation + ".png"),
                caffeineContent,
                new ArrayList<>(effects)
        );
    }

    public CoffeeBrewEffect addAttributeModifierEffect(TriggerableEffect effect) {
        this.addEffect(effect);
        this.totalDuration += effect.effectDuration;
        return this;
    }

    @Override
    public Holder<CoffeeBrewEffect> asHolder() {
        return Holder.direct(this);
    }

    static {
        STREAM_CODEC = ByteBufCodecs.holderRegistry(RavenCoffeeRegistryKeys.BREW_EFFECTS);
    }
}
