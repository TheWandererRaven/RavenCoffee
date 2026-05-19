package com.thewandererraven.ravencoffee.brew;

import com.thewandererraven.ravenbrewslib.brew.effect.AttributeModifierBrewEffectBehaviour;
import com.thewandererraven.ravenbrewslib.brew.effect.BrewEffectBehaviour;
import com.thewandererraven.ravenbrewslib.registry.RavenBrewsLibRegistryKeys;
import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.registry.RegistryObject;
import com.thewandererraven.ravencoffee.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

import java.util.List;

public class RavenBrewEffectsRegistry {
    public static final RegistryProvider<BrewEffectBehaviour> BREW_EFFECT_BEHAVIOURS = RegistryProvider.get(RavenBrewsLibRegistryKeys.BREW_EFFECT_BEHAVIOUR, Constants.MOD_ID);

    public static final String _jump_boost_id = "effect.jump_boost";
    public static final RegistryObject<BrewEffectBehaviour> JUMP_BOOST = RavenBrewEffectsRegistry.BREW_EFFECT_BEHAVIOURS.register(
            _jump_boost_id,
            () -> BrewEffectBehaviour.attributeModifier(List.of(
                    new AttributeModifierBrewEffectBehaviour.AttributeTemplate("jump_strength"),
                    //new AttributeModifierBrewEffectBehaviour.AttributeTemplate("fall_damage_multiplier", -1),
                    new AttributeModifierBrewEffectBehaviour.AttributeTemplate("safe_fall_distance")
            ))
    );

    public static final String _giantism_id = "effect.giantism";
    public static final RegistryObject<BrewEffectBehaviour> GIANTISM = RavenBrewEffectsRegistry.BREW_EFFECT_BEHAVIOURS.register(
            _giantism_id,
            () -> BrewEffectBehaviour.attributeModifier(List.of(
                    new AttributeModifierBrewEffectBehaviour.AttributeTemplate("scale"),
                    new AttributeModifierBrewEffectBehaviour.AttributeTemplate("jump_strength", 0.3),
                    new AttributeModifierBrewEffectBehaviour.AttributeTemplate("movement_speed"),
                    new AttributeModifierBrewEffectBehaviour.AttributeTemplate("step_height", 0.6, AttributeModifier.Operation.ADD_VALUE),
                    new AttributeModifierBrewEffectBehaviour.AttributeTemplate("block_interaction_range", AttributeModifier.Operation.ADD_VALUE),
                    new AttributeModifierBrewEffectBehaviour.AttributeTemplate("entity_interaction_range", AttributeModifier.Operation.ADD_VALUE),
                    new AttributeModifierBrewEffectBehaviour.AttributeTemplate("safe_fall_distance", AttributeModifier.Operation.ADD_VALUE)
            ))
    );
    public static void init() {

    }
}