package com.thewandererraven.ravencoffee.effect.breweffect;

import net.minecraft.world.entity.LivingEntity;

public class DefaultBrewEffectsManager implements IBrewEffectsManager {
    private MultiEffectInstance currentEffect = null;
    private LivingEntity player;

    public DefaultBrewEffectsManager(LivingEntity owner) {
        this.player = owner;
    }

    @Override
    public void add(MultiEffectInstance instance) {
        if(currentEffect == null)
            this.currentEffect = instance;
        if(currentEffect.multiEffect == instance.multiEffect)
            this.currentEffect.reset(player);
    }

    @Override
    public void tick() {
//        Iterator<BrewEffectInstance> it = active.iterator();
//        while(it.hasNext()) {
//            BrewEffectInstance inst = it.next();
//            if(inst.tick(player))
//                it.remove();
//        }
        if(currentEffect != null)
            if(currentEffect.tick(player))
                currentEffect = null;
    }

    @Override
    public MultiEffectInstance getCurrentEffect() {
//        return Collections.unmodifiableList(currentEffect);
        return currentEffect;
    }

    public DefaultBrewEffectsManager copy() {
        DefaultBrewEffectsManager copy = new DefaultBrewEffectsManager(this.player);
        copy.currentEffect = this.currentEffect;
        return copy;
    }
}
