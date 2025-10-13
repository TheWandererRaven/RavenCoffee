package com.thewandererraven.ravencoffee.effect.breweffect;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.registry.RegistryObject;
import com.thewandererraven.ravencoffee.registry.RegistryProvider;
import com.thewandererraven.ravencoffee.util.RavenCoffeeRegistryKeys;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class MultiEffectsRegistry {
    public static final RegistryProvider<MultiEffect> BREW_EFFECTS = RegistryProvider.create(RavenCoffeeRegistryKeys.BREW_EFFECTS, Constants.MOD_ID, MultiEffect.class);

    public static final String _basic_id = "basic";
    public static final RegistryObject<MultiEffect> BASIC = BREW_EFFECTS.register(
            _basic_id,
            () -> MultiEffect.of(
                    _basic_id,
                    new TriggerableEffect(0, 5,
                            Attributes.MINING_EFFICIENCY,
                            new TriggerableEffect.AttributeTemplate(
                                    ResourceLocation.withDefaultNamespace("effect.haste"),
                                    (double)2.0F,
                                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                            ).create(1)
                    )
            ).addTriggerableEffect(
                    new TriggerableEffect(5, 5,
                            Attributes.MOVEMENT_SPEED,
                            new TriggerableEffect.AttributeTemplate(
                                    ResourceLocation.withDefaultNamespace("effect.slowness"),
                                    (double)-0.2F,
                                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                            ).create(1)
                    )
            )
    );

    public static final String _speed_id = "speed";
    public static final RegistryObject<MultiEffect> SPEED = BREW_EFFECTS.register(
            _speed_id,
            () -> MultiEffect.of(
                    _speed_id,
                    new TriggerableEffect(0, 5,
                            Attributes.MOVEMENT_SPEED,
                            new TriggerableEffect.AttributeTemplate(
                                    ResourceLocation.withDefaultNamespace("effect.slowness"),
                                    (double)0.5F,
                                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                            ).create(1)
                    )
            ).addTriggerableEffect(
                    new TriggerableEffect(5, 5,
                            Attributes.MOVEMENT_SPEED,
                            new TriggerableEffect.AttributeTemplate(
                                    ResourceLocation.withDefaultNamespace("effect.slowness"),
                                    (double)-0.4F,
                                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                            ).create(1)
                    )
            )
    );

    public static void init() {

    }
}
