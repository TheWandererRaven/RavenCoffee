package com.thewandererraven.ravencoffee.effect.breweffect;

import com.thewandererraven.ravencoffee.Constants;

import java.util.function.Consumer;

public class InstantEffect extends BrewEffectCore {

    public InstantEffect(double mainValue, double secondaryValue, Consumer<BrewEffectContext> primaryEffect) {
        super(0, mainValue, secondaryValue, primaryEffect, brewEffectContext -> {});
    }
}
