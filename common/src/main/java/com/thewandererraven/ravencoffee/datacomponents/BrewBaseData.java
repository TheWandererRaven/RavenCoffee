package com.thewandererraven.ravencoffee.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public record BrewBaseData(
        Item item,
        int caffeineBase,
        double caffeineMultiplier,
        double durationMultiplier,
        double effectValuesMultiplier
) {
    public static final Codec<BrewBaseData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(BrewBaseData::item),
                    Codec.INT.optionalFieldOf("caffeine_base", 5).forGetter(BrewBaseData::caffeineBase),
                    Codec.DOUBLE.optionalFieldOf("caffeine_multiplier", 1.0).forGetter(BrewBaseData::caffeineMultiplier),
                    Codec.DOUBLE.optionalFieldOf("duration_multiplier", 1.0).forGetter(BrewBaseData::durationMultiplier),
                    Codec.DOUBLE.optionalFieldOf("effect_values_multiplier", 1.0).forGetter(BrewBaseData::effectValuesMultiplier)
            ).apply(instance, BrewBaseData::new)
    );
}
