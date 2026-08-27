package com.thewandererraven.ravencoffee.brew;

import com.thewandererraven.ravenbrewslib.brew.effect.AttributeModifierBrewEffectBehaviour;
import com.thewandererraven.ravenbrewslib.brew.effect.BrewEffectBehaviour;
import com.thewandererraven.ravenbrewslib.registry.RavenBrewsLibRegistryKeys;
import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.registry.RegistryObject;
import com.thewandererraven.ravencoffee.registry.RegistryProvider;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.animal.sheep.Sheep;

import java.util.List;

public class RavenBrewEffectsRegistry {
    public static final RegistryProvider<BrewEffectBehaviour> BREW_EFFECT_BEHAVIOURS = RegistryProvider.get(RavenBrewsLibRegistryKeys.BREW_EFFECT_BEHAVIOUR, Constants.MOD_ID);

    public static final String _giantism_id = "effect.giantism";
    public static final RegistryObject<BrewEffectBehaviour> GIANTISM = RavenBrewEffectsRegistry.BREW_EFFECT_BEHAVIOURS.register(
            _giantism_id,
            () -> (new BrewEffectBehaviour.Builder(com.thewandererraven.ravenbrewslib.Constants.MOD_ID, _giantism_id))
                    .buildAttributeModifier(List.of(
                    new AttributeModifierBrewEffectBehaviour.AttributeTemplate("scale"),
                    new AttributeModifierBrewEffectBehaviour.AttributeTemplate("jump_strength", 0.3),
                    new AttributeModifierBrewEffectBehaviour.AttributeTemplate("movement_speed"),
                    new AttributeModifierBrewEffectBehaviour.AttributeTemplate("step_height", 0.6, AttributeModifier.Operation.ADD_VALUE),
                    new AttributeModifierBrewEffectBehaviour.AttributeTemplate("block_interaction_range", AttributeModifier.Operation.ADD_VALUE),
                    new AttributeModifierBrewEffectBehaviour.AttributeTemplate("entity_interaction_range", AttributeModifier.Operation.ADD_VALUE),
                    new AttributeModifierBrewEffectBehaviour.AttributeTemplate("safe_fall_distance", AttributeModifier.Operation.ADD_VALUE)
            ))
    );
    public static final String _sheep_spawner_id = "effect.sheep_spawner";
    public static final RegistryObject<BrewEffectBehaviour> SHEEP_SPAWNER = RavenBrewEffectsRegistry.BREW_EFFECT_BEHAVIOURS.register(
            _sheep_spawner_id,
            () -> (new BrewEffectBehaviour.Builder(com.thewandererraven.ravenbrewslib.Constants.MOD_ID, _giantism_id))
                    .withPrimaryEffect(context -> {
                        LivingEntity entity = context.entity();
                        if(entity.level() instanceof ServerLevel serverLevel) {
                            Sheep sheep = EntityType.SHEEP.create(serverLevel, EntitySpawnReason.MOB_SUMMONED);
                            sheep.moveOrInterpolateTo(entity.getPosition(20).add(1.0, 0.0, 0.0), 0.0f, 0.0f);
                            serverLevel.addFreshEntity(sheep);
                        }
                    })
                    .withTickMode(BrewEffectBehaviour.TickMode.INTERVAL)
                    .build()
    );

    public static void init() {

    }
}