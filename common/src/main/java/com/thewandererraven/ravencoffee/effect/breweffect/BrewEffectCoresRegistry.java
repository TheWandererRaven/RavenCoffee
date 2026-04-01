package com.thewandererraven.ravencoffee.effect.breweffect;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.registry.RegistryObject;
import com.thewandererraven.ravencoffee.registry.RegistryProvider;
import com.thewandererraven.ravencoffee.util.RavenCoffeeRegistryKeys;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Items;

public class BrewEffectCoresRegistry {
    public static final RegistryProvider<BrewEffectCore> BREW_EFFECT_CORES = RegistryProvider.create(RavenCoffeeRegistryKeys.BREW_EFFECT_CORES, Constants.MOD_ID, BrewEffectCore.class);

    public static final String _heal_id = "effect.heal";
    public static final RegistryObject<BrewEffectCore> HEAL = BREW_EFFECT_CORES.register(
            _heal_id,
            () -> BrewEffectCore.instant(context -> context.entity().heal(context.effectMainValueAsInt()))
    );
    public static final String _hurt_id = "effect.hurt";
    public static final RegistryObject<BrewEffectCore> HURT = BREW_EFFECT_CORES.register(
            _hurt_id,
            () -> BrewEffectCore.instant(context -> context.entity().hurt(context.entity().damageSources().generic(), context.effectMainValueAsFloat()))
    );
    public static final String _absorption_id = "effect.absorption";
    public static final RegistryObject<BrewEffectCore> ABSORPTION = BREW_EFFECT_CORES.register(
            _absorption_id,
            () -> BrewEffectCore.attributeModifier("max_absorption", AttributeModifier.Operation.ADD_VALUE, context -> {
                    context.entity().setAbsorptionAmount(context.entity().getAbsorptionAmount() + context.effectMainValueAsFloat());
            })
    );
    public static final String _speed_id = "effect.speed";
    public static final RegistryObject<BrewEffectCore> SPEED = BREW_EFFECT_CORES.register(
            _speed_id,
            () -> BrewEffectCore.attributeModifier("movement_speed")
    );
    // YES, it's basically the same, I just want the different id. I might add more functionality later so the difference is actually different
    public static final String _slowness_id = "effect.slowness";
    public static final RegistryObject<BrewEffectCore> SLOWNESS = BREW_EFFECT_CORES.register(
            _slowness_id,
            () -> BrewEffectCore.attributeModifier("movement_speed")
    );
    public static final String _attack_speed_id = "effect.attack_speed";
    public static final RegistryObject<BrewEffectCore> ATTACK_SPEED = BREW_EFFECT_CORES.register(
            _attack_speed_id,
            () -> BrewEffectCore.attributeModifier("attack_speed")
    );
    public static final String _haste_id = "effect.haste";
    public static final RegistryObject<BrewEffectCore> HASTE = BREW_EFFECT_CORES.register(
            _haste_id,
            () -> BrewEffectCore.attributeModifier("block_break_speed")
    );
    public static final String _strong_legs_id = "effect.strong_legs";
    public static final RegistryObject<BrewEffectCore> STRONG_LEGS = BREW_EFFECT_CORES.register(
            _strong_legs_id,
            () -> BrewEffectCore.attributeModifier("fall_damage_multiplier")
    );
    public static final String _weak_legs_id = "effect.weak_legs";
    public static final RegistryObject<BrewEffectCore> WEAK_LEGS = BREW_EFFECT_CORES.register(
            _weak_legs_id,
            () -> BrewEffectCore.attributeModifier("fall_damage_multiplier")
    );

    public static void init() {

    }
}
