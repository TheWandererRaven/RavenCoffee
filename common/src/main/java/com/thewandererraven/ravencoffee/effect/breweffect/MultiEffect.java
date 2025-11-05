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
    private final ResourceLocation id;
    private final ResourceLocation iconLocation;
    public int totalDuration;
    private List<TriggerableEffect> subEffects;

    public MultiEffect(ResourceLocation id, ResourceLocation iconLocation, List<TriggerableEffect> effects) {
        this.id = id;
        this.iconLocation = iconLocation;
        this.subEffects = effects;
        this.subEffects.forEach(effect -> this.totalDuration += effect.effectDuration);
    }

    public MultiEffect addAttributeModifierEffect(TriggerableEffect effect) {
        this.addEffect(effect);
        this.totalDuration += effect.effectDuration;
        return this;
    }

    public TriggerableEffect getEffect(int index) {
        return this.subEffects.get(index);
    }

    public boolean addEffect(TriggerableEffect effect) {
        return this.subEffects.add(effect);
    }

    public int getCount() {
        return this.subEffects.size();
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public Holder<?> asHolder() {
        return Holder.direct(this);
    }

    public ResourceLocation getIconLocation() {
        return this.iconLocation;
    }

    public static MultiEffect of(String id, TriggerableEffect effect) {
        return MultiEffect.of(id, List.of(effect));
    }

    public static MultiEffect of(String id, List<TriggerableEffect> effects) {
        return MultiEffect.of(id, id, effects);
    }

    public static MultiEffect of(String id, String iconLocation, List<TriggerableEffect> effects) {
        return new MultiEffect(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, id), ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/item/coffee_mug/" + iconLocation + ".png"), new ArrayList<>(effects));
    }
}
