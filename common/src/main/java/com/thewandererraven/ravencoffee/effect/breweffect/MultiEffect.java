package com.thewandererraven.ravencoffee.effect.breweffect;

import com.mojang.serialization.Codec;
import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.util.RavenCoffeeRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;

import java.util.ArrayList;
import java.util.List;

public class MultiEffect {
    //public static final Codec<Holder<MultiEffect>> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<MultiEffect>> STREAM_CODEC;
    private final ResourceLocation id;
    public int totalDuration;
    private List<TriggerableEffect> subEffects;

    public MultiEffect(ResourceLocation id, List<TriggerableEffect> effects) {
        this.id = id;
        this.subEffects = effects;
        this.subEffects.forEach(effect -> this.totalDuration += effect.effectDuration);
    }

    public MultiEffect addTriggerableEffect(TriggerableEffect effect) {
        this.subEffects.add(effect);
        this.totalDuration += effect.effectDuration;
        return this;
    }

    public TriggerableEffect getEffect(int index) {
        return this.subEffects.get(index);
    }

    public int getCount() {
        return this.subEffects.size();
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public Holder<MultiEffect> asHolder() {
        return Holder.direct(this);
    }

    public static MultiEffect of(String id, TriggerableEffect effect) {
        return MultiEffect.of(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, id), effect);
    }

    public static MultiEffect of(String id, List<TriggerableEffect> effects) {
        return MultiEffect.of(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, id), effects);
    }

    public static MultiEffect of(ResourceLocation id, TriggerableEffect effect) {
        return MultiEffect.of(id, List.of(effect));
    }

    public static MultiEffect of(ResourceLocation id, List<TriggerableEffect> effects) {
        return new MultiEffect(id, new ArrayList<>(effects));
    }
    static {
        //CODEC = BuiltInRegistries.MOB_EFFECT.holderByNameCodec();
        STREAM_CODEC = ByteBufCodecs.holderRegistry(RavenCoffeeRegistryKeys.BREW_EFFECTS);
    }
}
