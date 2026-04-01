package com.thewandererraven.ravencoffee.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.effect.breweffect.BrewEffect;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record BrewEffectData(
        ResourceLocation id,
        int duration,
        double mainValue,
        double secondaryValue
) {

    public static final Codec<BrewEffectData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(BrewEffectData::id),
                    Codec.INT.optionalFieldOf("duration", 0).forGetter(BrewEffectData::duration),
                    Codec.DOUBLE.fieldOf("main_value").forGetter(BrewEffectData::mainValue),
                    Codec.DOUBLE.optionalFieldOf("secondary_value", 0.0).forGetter(BrewEffectData::secondaryValue)
            ).apply(instance, BrewEffectData::new)
    );

    public static List<BrewEffectData.Builder> getListOfBasicEffects() {
        return List.of(
            new BrewEffectData.Builder(
                    ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "effect.haste"),
                    15 * 20,
                    5.0,
                    0.0
            ),
            new BrewEffectData.Builder(
                    ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "effect.slowness"),
                    7 * 20,
                    -0.2,
                    0.0
            )
        );
    }

    public static class Builder {
        public ResourceLocation id;
        int duration;
        double mainValue;
        double secondaryValue;

        public Builder(ResourceLocation id, int duration, double mainValue, double secondaryValue) {
            this.id = id;
            this.duration = duration;
            this.mainValue = mainValue;
            this.secondaryValue = secondaryValue;
        }

        public Builder(ResourceLocation id) {
            this(id, 0, 0.0, 0.0);
        }

        public Builder addDuration(int addedDuration) {
            this.duration += addedDuration;
            return this;
        }

        public Builder scaleDuration(double multiplier) {
            this.duration = (int) Math.ceil(this.duration * multiplier);
            return this;
        }

        public Builder setMainValue(double mainValue) {
            this.mainValue = mainValue;
            return this;
        }

        public Builder addMainValue(double addedMainValue) {
            this.mainValue += addedMainValue;
            return this;
        }

        public Builder scaleMainValue(double multiplier) {
            this.mainValue = this.mainValue * multiplier;
            return this;
        }

        public Builder setSecondaryValue(double secondaryValue) {
            this.secondaryValue = secondaryValue;
            return this;
        }

        public Builder addSecondaryValue(double addedSecondaryValue) {
            this.secondaryValue += addedSecondaryValue;
            return this;
        }

        public Builder scaleSecondaryValue(double multiplier) {
            this.secondaryValue = this.secondaryValue * multiplier;
            return this;
        }

        public BrewEffectData build() {
            return new BrewEffectData(this.id, this.duration, this.mainValue, this.secondaryValue);
        }
    }
}
