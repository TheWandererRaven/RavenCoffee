package com.thewandererraven.ravencoffee.effect.breweffect;


import com.thewandererraven.ravencoffee.Constants;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.util.BiConsumer;

import java.util.function.Consumer;

public class AttributeModifierEffect extends BrewEffectCore {

    public AttributeModifierEffect(ResourceLocation effectId, int effectDuration, double mainValue, double secondaryValue, Holder<Attribute> attributeHolder) {
        // GET ATTRIBUTE HOLDER FROM THE LOOKUP METHOD
        super(effectDuration, mainValue, secondaryValue,
                brewEffectContext -> AttributeModifierEffect.addAttributeModifierToPlayer(brewEffectContext.entity(), attributeHolder,
                        new AttributeModifierEffect.AttributeTemplate(
                                effectId,
                                mainValue,
                                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                        ).create(1)),
                brewEffectContext -> AttributeModifierEffect.removeAttributeModifierToPlayer(brewEffectContext.entity(), attributeHolder,
                        new AttributeModifierEffect.AttributeTemplate(
                                effectId,
                                mainValue * -1,
                                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                        ).create(1)
                )
        );
    }

    private static void addAttributeModifierToPlayer(LivingEntity entity, Holder<Attribute> attribute, AttributeModifier modifier) {
        if(entity instanceof Player player) {
            AttributeInstance instance = player.getAttributes().getInstance(attribute);
            if (instance != null)
                if(!instance.hasModifier(modifier.id()))
                    instance.addTransientModifier(modifier);
        }
    }

    private static void removeAttributeModifierToPlayer(LivingEntity entity, Holder<Attribute> attribute, AttributeModifier modifier) {
        if(entity instanceof Player player) {
            AttributeInstance instance = player.getAttributes().getInstance(attribute);
            if (instance != null)
                instance.removeModifier(modifier);
        }
    }

}
