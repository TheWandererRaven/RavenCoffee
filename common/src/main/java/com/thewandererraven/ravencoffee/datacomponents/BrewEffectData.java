package com.thewandererraven.ravencoffee.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thewandererraven.ravencoffee.Constants;
import net.minecraft.resources.ResourceLocation;

public record BrewEffectData(
        ResourceLocation id,
        int duration,
        double mainValue,
        double secondaryValue
) {
    public static final Codec<BrewEffectData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(BrewEffectData::id),
                    Codec.INT.fieldOf("duration").forGetter(BrewEffectData::duration),
                    Codec.DOUBLE.fieldOf("main_value").forGetter(BrewEffectData::mainValue),
                    Codec.DOUBLE.fieldOf("secondary_value").forGetter(BrewEffectData::secondaryValue)
            ).apply(instance, BrewEffectData::new)
    );

    public ResourceLocation generateIconLocation() {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/effect/icons/" + id.getPath() + ".png");
    }
}
