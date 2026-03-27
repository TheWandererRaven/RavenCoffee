package com.thewandererraven.ravencoffee.datacomponents;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.item.BrewItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public record CoffeeBrewData(
        ResourceLocation brewVariant,
        int caffeine,
        List<BrewEffectData> effects
) implements TooltipProvider {

    @Override
    public ResourceLocation brewVariant() {
        return brewVariant == null ? ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "broken") : brewVariant;
    }

    public static final Codec<CoffeeBrewData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.optionalFieldOf("brew_variant", ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "basic")).forGetter(CoffeeBrewData::brewVariant),
                    Codec.INT.fieldOf("caffeine").forGetter(CoffeeBrewData::caffeine),
                    BrewEffectData.CODEC.listOf().fieldOf("effects").forGetter(CoffeeBrewData::effects)
            ).apply(instance, CoffeeBrewData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, CoffeeBrewData> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC).cast();

    public static final CoffeeBrewData EMPTY = new CoffeeBrewData(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "basic"), 0, List.of());
    public static final CoffeeBrewData BROKEN = new CoffeeBrewData(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "broken"), 0, List.of());

    @Override
    public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
        tooltipAdder.accept(Component.literal("TEST"));
        for(BrewEffectData effectData : effects) {
//            MutableComponent mutablecomponent = getPotionDescription(holder, i);
            tooltipAdder.accept(Component.translatable(effectData.id().toLanguageKey()).append(" -> ").append(String.valueOf(effectData.duration())).withStyle(ChatFormatting.GRAY));
        }
    }
}
