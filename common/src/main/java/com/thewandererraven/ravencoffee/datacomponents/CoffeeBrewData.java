package com.thewandererraven.ravencoffee.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thewandererraven.ravenbrewslib.brew.data.BrewEffectDefinition;
import com.thewandererraven.ravencoffee.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.List;
import java.util.function.Consumer;

public record CoffeeBrewData(
        ResourceLocation brewVariant,
        int caffeine,
        List<BrewEffectDefinition> effects
) implements TooltipProvider {

    @Override
    public ResourceLocation brewVariant() {
        return brewVariant == null ? ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "broken") : brewVariant;
    }

    public static final Codec<CoffeeBrewData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.optionalFieldOf("brew_variant", ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "default")).forGetter(CoffeeBrewData::brewVariant),
                    Codec.INT.fieldOf("caffeine").forGetter(CoffeeBrewData::caffeine),
                    BrewEffectDefinition.CODEC.listOf().fieldOf("effects").forGetter(CoffeeBrewData::effects)
            ).apply(instance, CoffeeBrewData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, CoffeeBrewData> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC).cast();

    public static final CoffeeBrewData DEFAULT = new CoffeeBrewData(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "default"), 0, List.of());
    public static final CoffeeBrewData BROKEN = new CoffeeBrewData(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "broken"), 0, List.of());

    @Override
    public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
        for(BrewEffectDefinition effectData : effects) {
//            MutableComponent mutablecomponent = getPotionDescription(holder, i);
            String duration = effectData.duration() > 0 ? String.valueOf(effectData.duration()) : "instant";
            tooltipAdder.accept(Component.translatable(effectData.id().toLanguageKey()).append(" -> ").append(duration).withStyle(ChatFormatting.GRAY));
        }
    }
}
