package com.thewandererraven.ravencoffee.effect.breweffect;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.registry.RegistryObject;
import com.thewandererraven.ravencoffee.registry.RegistryProvider;
import com.thewandererraven.ravencoffee.util.RavenCoffeeRegistryKeys;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class MultiEffectsRegistry {
    public static final RegistryProvider<CoffeeBrewEffect> BREW_EFFECTS = RegistryProvider.create(RavenCoffeeRegistryKeys.BREW_EFFECTS, Constants.MOD_ID, CoffeeBrewEffect.class);

    public static final String _basic_id = "basic";
    public static final RegistryObject<CoffeeBrewEffect> BASIC = BREW_EFFECTS.register(
            _basic_id,
            () -> CoffeeBrewEffect.of(
                    _basic_id,
                    10 * 20,
                    AttributeModifierEffect.of(0, 5,
                            Attributes.MINING_EFFICIENCY,
                            new AttributeModifierEffect.AttributeTemplate(
                                    ResourceLocation.withDefaultNamespace("effect.basic_brew_haste"),
                                    (double)2.0F,
                                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                            ).create(1)
                    )
            ).addAttributeModifierEffect(
                    AttributeModifierEffect.of(5, 5,
                            Attributes.MOVEMENT_SPEED,
                            new AttributeModifierEffect.AttributeTemplate(
                                    ResourceLocation.withDefaultNamespace("effect.basic_brew_slowness"),
                                    (double)-0.2F,
                                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                            ).create(1)
                    )
            )
    );

    public static final String _speed_id = "speed";
    public static final RegistryObject<CoffeeBrewEffect> SPEED = BREW_EFFECTS.register(
            _speed_id,
            () -> CoffeeBrewEffect.of(
                    _speed_id,
                    23 * 20,
                    AttributeModifierEffect.of(0, 15,
                            Attributes.MOVEMENT_SPEED,
                            new AttributeModifierEffect.AttributeTemplate(
                                    ResourceLocation.withDefaultNamespace("effect.speed_brew_speed"),
                                    (double)0.5F,
                                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                            ).create(1)
                    )
            ).addAttributeModifierEffect(
                    AttributeModifierEffect.of(15, 8,
                            Attributes.MOVEMENT_SPEED,
                            new AttributeModifierEffect.AttributeTemplate(
                                    ResourceLocation.withDefaultNamespace("effect.speed_brew_slowness"),
                                    (double)-0.4F,
                                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                            ).create(1)
                    )
            )
    );

    public static final String _heal_id = "heal";
    public static final RegistryObject<CoffeeBrewEffect> HEAL = BREW_EFFECTS.register(
            _heal_id,
            () -> CoffeeBrewEffect.of(
                    _heal_id,
                    10 * 20,
                    new SingleEffect(0, 1,
                            entity -> entity.heal(5.0f)
                    )
            )
    );

    public static void init() {

    }
}
