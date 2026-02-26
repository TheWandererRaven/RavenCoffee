package com.thewandererraven.ravencoffee.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thewandererraven.ravencoffee.item.BrewItem;

import java.util.ArrayList;
import java.util.List;

public record CoffeeBrewData(
        BrewItem.BrewVariant brewVariant,
        int caffeine,
        int durationMultiplier,
        List<BrewEffectData> effects
) {
    @Override
    public BrewItem.BrewVariant brewVariant() {
        return brewVariant == null ? BrewItem.BrewVariant.BROKEN : brewVariant;
    }

    public static final Codec<CoffeeBrewData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BrewItem.BrewVariant.CODEC.optionalFieldOf("brew_variant", BrewItem.BrewVariant.BASIC).forGetter(CoffeeBrewData::brewVariant),
                    Codec.INT.fieldOf("caffeine").forGetter(CoffeeBrewData::caffeine),
                    Codec.INT.optionalFieldOf("duration_multiplier", 0).forGetter(CoffeeBrewData::durationMultiplier),
                    BrewEffectData.CODEC.listOf().fieldOf("effects").forGetter(CoffeeBrewData::effects)
            ).apply(instance, CoffeeBrewData::new)
    );

    public static final CoffeeBrewData EMPTY = new CoffeeBrewData(BrewItem.BrewVariant.BASIC, 0, 0, new ArrayList<>());
    public static final CoffeeBrewData BROKEN = new CoffeeBrewData(BrewItem.BrewVariant.BROKEN, 0, 0, new ArrayList<>());
}
