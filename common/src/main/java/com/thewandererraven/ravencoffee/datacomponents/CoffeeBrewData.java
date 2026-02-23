package com.thewandererraven.ravencoffee.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record CoffeeBrewData(
        int caffeine,
        int durationMultiplier,
        List<BrewEffectData> effects
) {
    public static final Codec<CoffeeBrewData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("caffeine").forGetter(CoffeeBrewData::caffeine),
                    Codec.INT.optionalFieldOf("duration_multiplier", 0).forGetter(CoffeeBrewData::durationMultiplier),
                    BrewEffectData.CODEC.listOf().fieldOf("effects").forGetter(CoffeeBrewData::effects)
            ).apply(instance, CoffeeBrewData::new)
    );
}
