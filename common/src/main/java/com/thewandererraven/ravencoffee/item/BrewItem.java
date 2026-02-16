package com.thewandererraven.ravencoffee.item;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.effect.breweffect.*;
import com.thewandererraven.ravencoffee.item.data.CoffeeBrewData;
import com.thewandererraven.ravencoffee.item.data.CoffeeBrewEffectData;
import com.thewandererraven.ravencoffee.item.data.DataComponentTypes;
import com.thewandererraven.ravencoffee.platform.services.IBrewManagerHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class BrewItem extends Item {
    private Supplier<CoffeeBrewEffect> effectHolder;

    public BrewItem(Properties properties, Supplier<CoffeeBrewEffect> holder) {
        super(properties);
        this.effectHolder = holder;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if(livingEntity instanceof Player player) {
            if(!level.isClientSide) {
                CoffeeBrewData data = stack.get(DataComponentTypes.COFFEE_BREW.get());
                if(data != null) {
                    ((IBrewManagerHolder) livingEntity).ravencoffee$getBrewEffectManager().add(
                            //new CoffeeBrewEffectInstance(effectHolder.get().asHolder())
                            buildEffectInstance(data)
                    );
                }
            }
            if(!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

        }
        return stack.isEmpty() ? new ItemStack(BrewItemsRegistry.COFFEE_MUG.get()) : stack;
    }

    public static CoffeeBrewEffectInstance buildEffectInstance(CoffeeBrewData data)
    {
        CoffeeBrewEffect coffeeEffect = CoffeeBrewEffect.asEmpty();

        for(CoffeeBrewEffectData effectData: data.effects())
        {
            // TODO: Add code to get the attribute name based on the data id for that effect
            if(effectData.effect_type().equals("attribute_modifier")) {
                coffeeEffect.addAttributeModifierEffect(
                        AttributeModifierEffect.of(coffeeEffect.totalDuration, effectData.duration() * data.durationMultiplier(),
                                Attributes.MOVEMENT_SPEED,
                                new AttributeModifierEffect.AttributeTemplate(
                                        ResourceLocation.withDefaultNamespace("effect.speed_brew_speed"),
                                        (double) 0.5F,
                                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                                ).create(1)
                        )
                );
            }
        }
        return new CoffeeBrewEffectInstance(
            CoffeeBrewEffect.of(
                    "speed",
                    data.caffeine() * 20,
                    AttributeModifierEffect.of(0, 5,
                            Attributes.MOVEMENT_SPEED,
                            new AttributeModifierEffect.AttributeTemplate(
                                    ResourceLocation.withDefaultNamespace("effect.speed_brew_speed"),
                                    (double)0.5F + data.effects().getFirst().value(),
                                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                            ).create(1)
                    )
            ).addAttributeModifierEffect(
                    AttributeModifierEffect.of(5, 8,
                            Attributes.MOVEMENT_SPEED,
                            new AttributeModifierEffect.AttributeTemplate(
                                    ResourceLocation.withDefaultNamespace("effect.speed_brew_slowness"),
                                    (double)-0.4F + data.effects().get(1).value(),
                                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                            ).create(1)
                    )
            ).asHolder()
        );
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.DRINK;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if(player instanceof IBrewManagerHolder holder) {
            if(!holder.ravencoffee$getBrewEffectManager().getOverloadStatus())
                return ItemUtils.startUsingInstantly(level, player, hand);
        }
        return InteractionResult.FAIL;
    }
}
