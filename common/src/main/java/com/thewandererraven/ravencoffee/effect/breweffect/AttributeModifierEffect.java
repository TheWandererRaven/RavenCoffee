package com.thewandererraven.ravencoffee.effect.breweffect;


import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;

public class AttributeModifierEffect extends BrewEffect {

    public AttributeModifierEffect(ResourceLocation effectId, ResourceLocation attributeId, int effectTicksDuration, double mainValue, double secondaryValue, Holder<Attribute> attributeHolder) {
        // GET ATTRIBUTE HOLDER FROM THE LOOKUP METHOD
        super(effectId, effectTicksDuration, mainValue, secondaryValue,
                brewEffectContext -> AttributeModifierEffect.addAttributeModifierToPlayer(brewEffectContext.entity(), attributeHolder,
                        new AttributeModifierEffect.AttributeTemplate(
                                attributeId,
                                brewEffectContext.effectMainValue(),
                                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                        ).create(1)),
                brewEffectContext -> AttributeModifierEffect.removeAttributeModifierToPlayer(brewEffectContext.entity(), attributeHolder,
                        new AttributeModifierEffect.AttributeTemplate(
                                attributeId,
                                brewEffectContext.effectSecondaryValue() * -1,
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

    public record AttributeTemplate(ResourceLocation id, double amount, AttributeModifier.Operation operation) {
        public AttributeModifier create(int level) {
            return new AttributeModifier(this.id, this.amount * (double)(level + 1), this.operation);
        }
    }
}
