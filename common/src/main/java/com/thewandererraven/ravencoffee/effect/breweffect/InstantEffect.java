package com.thewandererraven.ravencoffee.effect.breweffect;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class InstantEffect extends BrewEffect {

    public InstantEffect(ResourceLocation effectId, double mainValue, double secondaryValue, Consumer<BrewEffectContext> primaryEffect) {
        super(effectId, 1, mainValue, secondaryValue, primaryEffect, brewEffectContext -> {});
    }
}
