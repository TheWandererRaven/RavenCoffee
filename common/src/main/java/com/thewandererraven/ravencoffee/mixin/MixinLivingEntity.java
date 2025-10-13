package com.thewandererraven.ravencoffee.mixin;

import com.thewandererraven.ravencoffee.effect.breweffect.DefaultBrewEffectsManager;
import com.thewandererraven.ravencoffee.platform.services.IBrewManagerHolder;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public class MixinLivingEntity implements IBrewManagerHolder {
    private final DefaultBrewEffectsManager brewEffectsManager = new DefaultBrewEffectsManager((LivingEntity)(Object)this);

    @Override
    public DefaultBrewEffectsManager ravencoffee$getBrewEffectManager() {
        return brewEffectsManager;
    }
}
