package com.thewandererraven.ravencoffee.item.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thewandererraven.ravencoffee.effect.breweffect.TriggerableEffect;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

public record CoffeeBrewEffectData(
        ResourceLocation id,
        ResourceLocation attributeId,
        String type,
        int duration,
        double mainValue,
        double secondaryValue
) {
    public static final Codec<CoffeeBrewEffectData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(CoffeeBrewEffectData::id),
                    ResourceLocation.CODEC.fieldOf("attribute_id").forGetter(CoffeeBrewEffectData::attributeId),
                    Codec.STRING.fieldOf("type").forGetter(CoffeeBrewEffectData::type),
                    Codec.INT.fieldOf("duration").forGetter(CoffeeBrewEffectData::duration),
                    Codec.DOUBLE.fieldOf("main_value").forGetter(CoffeeBrewEffectData::mainValue),
                    Codec.DOUBLE.fieldOf("secondary_value").forGetter(CoffeeBrewEffectData::secondaryValue)
            ).apply(instance, CoffeeBrewEffectData::new)
    );
}
