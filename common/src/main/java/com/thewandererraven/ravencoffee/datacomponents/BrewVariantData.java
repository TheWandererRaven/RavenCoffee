package com.thewandererraven.ravencoffee.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.List;

public record BrewVariantData(
        List<Item> items,
        ResourceLocation variantId
) {
    public static final Codec<BrewVariantData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BuiltInRegistries.ITEM.byNameCodec().listOf().fieldOf("items").forGetter(BrewVariantData::items),
                    ResourceLocation.CODEC.fieldOf("variant_id").forGetter(BrewVariantData::variantId)
            ).apply(instance, BrewVariantData::new)
    );
}
