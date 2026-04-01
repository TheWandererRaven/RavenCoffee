package com.thewandererraven.ravencoffee.item.properties;


import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.item.BrewItem;
import com.thewandererraven.ravencoffee.util.BrewEffectsUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BrewVariantProperty implements SelectItemModelProperty<ResourceLocation> {

    // The object to register that contains the relevant codecs
    public static final SelectItemModelProperty.Type<BrewVariantProperty, ResourceLocation> TYPE = SelectItemModelProperty.Type.create(
            // The map codec for this property
            MapCodec.unit(new BrewVariantProperty()),
            // The codec for the object being selected
            // Used to serialize the case entries ("when": <property value>)
            ResourceLocation.CODEC
    );

    @Override
    public ResourceLocation get(ItemStack stack, ClientLevel level, LivingEntity entity, int seed, ItemDisplayContext displayContext) {
        // When null, uses the fallback model
        ResourceLocation variant = BrewEffectsUtils.getItemBrewDataComponent(stack).brewVariant();
        return variant != null ? variant : ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "default");
    }

    @Override
    public Codec<ResourceLocation> valueCodec() {
        return ResourceLocation.CODEC;
    }

    @Override
    public SelectItemModelProperty.Type<BrewVariantProperty, ResourceLocation> type() {
        return TYPE;
    }
}