package com.thewandererraven.ravencoffee.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.List;

public record BrewIngredientData(
        Item item,
        int caffeineIncrement,
        List<BrewEffectData> effects
) {
    public static final Codec<BrewIngredientData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(BrewIngredientData::item),
                    Codec.INT.fieldOf("caffeine_increment").forGetter(BrewIngredientData::caffeineIncrement),
                    BrewEffectData.CODEC.listOf().fieldOf("effects").forGetter(BrewIngredientData::effects)
            ).apply(instance, BrewIngredientData::new)
    );
}
