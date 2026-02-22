package com.thewandererraven.ravencoffee.effect.breweffect;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.item.data.CoffeeBrewData;
import com.thewandererraven.ravencoffee.item.data.BrewEffectData;
import com.thewandererraven.ravencoffee.networking.SyncBrewManagerCaffeinePayload;
import com.thewandererraven.ravencoffee.networking.SyncBrewManagerDurationPayload;
import com.thewandererraven.ravencoffee.networking.SyncBrewManagerIconsPayload;
import com.thewandererraven.ravencoffee.platform.Services;
import com.thewandererraven.ravencoffee.util.BrewEffectsUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DefaultBrewEffectsManager implements IBrewEffectsManager {
    private List<BrewEffect> effectsStack = null;
    private List<ResourceLocation> effectsIcons = null;
    public int totalTicksDuration = 0;
    private int currentEffectRemainingTicks = 0;
    private int currentCaffeine = 0;
    private boolean isOverloaded = false;

    private final int maxEffectsStackSize = 5;
    private final int maxCaffeine = 30 * 20;

    private final LivingEntity ownerEntity;

    public DefaultBrewEffectsManager(LivingEntity owner) {
        this.ownerEntity = owner;
        this.effectsStack = new ArrayList<>();
        this.effectsIcons = new ArrayList<>();
    }

    @Override
    public boolean add(CoffeeBrewData brewData) {
        if(!this.isOverloaded) {
            if(this.effectsStack.size() < this.maxEffectsStackSize)
                for(BrewEffectData effectData: brewData.effects()) {
                    BrewEffect newEffect = createBrewEffect(effectData);
                    if(this.effectsStack.isEmpty())
                        this.currentEffectRemainingTicks = newEffect.effectTicksDuration;
                    this.effectsStack.add(newEffect);
                    this.effectsIcons.add(effectData.id());
                    if(this.effectsStack.size() >= this.maxEffectsStackSize)
                        break;
                }

            this.addCaffeine(brewData.caffeine());
            this.calculateTotalRemainingTicks();

            if(this.currentCaffeine >= this.maxCaffeine) {
                // TODO: Here goes te drawback of
                Constants.LOG.info("CAFFEINE OVERLOAD!");
                this.isOverloaded = true;
            }
            if (ownerEntity instanceof ServerPlayer serverPlayer) {
                Services.PLATFORM.sendCustomPacket(serverPlayer, new SyncBrewManagerIconsPayload(this.effectsIcons));
                Services.PLATFORM.sendCustomPacket(serverPlayer, new SyncBrewManagerDurationPayload(this.currentEffectRemainingTicks, this.totalTicksDuration));
                Services.PLATFORM.sendCustomPacket(serverPlayer, new SyncBrewManagerCaffeinePayload(this.currentCaffeine, this.isOverloaded));
            }
            return true;
        }
        return false;
    }

    public BrewEffect createBrewEffect(BrewEffectData data)
    {
        BrewEffect newEffect = null;
        BrewEffectCore effectCore = BrewEffectsUtils.findEffectInRegistry(data.id());
        if(effectCore == null) {
            Constants.LOG.error("Unable to find brew effect for: {}", data.id().toString());
            return BrewEffect.EMPTY;
        }

        if(effectCore.type.equals("attribute_modifier")) {
            newEffect = new AttributeModifierEffect(data.id(), effectCore.attributeId, data.duration(), data.mainValue(), data.secondaryValue(),
                    BrewEffectsUtils.findAttributeByItsId(this.ownerEntity.level(), effectCore.attributeId)
            );
        } else if(effectCore.type.equals("instant")) {
            newEffect = new InstantEffect(data.id(), data.mainValue(), data.secondaryValue(), effectCore.primaryEffect);
        }

        return newEffect;
    }

    public void setClientEffects(List<BrewEffect> list) {
        this.effectsStack = list;
    }

    public boolean setClientEffect(BrewEffectData effectData, int index) {
        if(index < this.effectsStack.size()) {
            this.effectsStack.set(index, createBrewEffect(effectData));
            return true;
        }
        return false;
    }

    public boolean setClientEffect(BrewEffectData effectData) {
        for(int i = 0; i < this.effectsStack.size(); i++)
        {
            if(this.effectsStack.get(i).effectId.equals(effectData.id()))
                return this.setClientEffect(effectData, i);
        }
        return false;
    }

    public BrewEffect getEffect(int index)
    {
        return this.effectsStack.get(index);
    }

    public BrewEffect getCurrentEffect()
    {
        return getEffect(0);
    }

    public boolean areCurrentEffectTicksInAFullSecond() {
        return this.currentEffectRemainingTicks % 20 == 0;
    }

    public void setCurrentEffectRemainingTicks(int currEffRemainingTicks) {
        this.currentEffectRemainingTicks = currEffRemainingTicks;
    }
    public int getCurrentEffectRemainingSeconds() {
        return (int) Math.ceil(this.currentEffectRemainingTicks / 20.0);
    }

    public void calculateTotalRemainingTicks() {
        int totalTicksDuration = this.currentEffectRemainingTicks;
        for(int i = 1; i < this.effectsStack.size(); i++) {
            totalTicksDuration += this.effectsStack.get(i).effectTicksDuration;
        }
        this.totalTicksDuration = totalTicksDuration;
    }

    public void setTotalRemainingTicks(int totalTicksDuration) {
        this.totalTicksDuration = totalTicksDuration;
    }

    public int getTotalRemainingSeconds() {
        return (int) Math.ceil(this.totalTicksDuration / 20.0);
    }

    public void tickCaffeine(ServerPlayer player) {
        if(this.currentCaffeine > 0) {
            this.currentCaffeine -= 1;
        } else if(this.isOverloaded) {
            this.isOverloaded = false;
        }
        if(this.currentCaffeine <= 0 && !this.isOverloaded)
            return;
        Services.PLATFORM.sendCustomPacket(player, new SyncBrewManagerCaffeinePayload(this.currentCaffeine, this.isOverloaded));
    }

    @Override
    public void tick() {
        if (ownerEntity instanceof ServerPlayer serverPlayer) {

            this.tickCaffeine(serverPlayer);

            if(!this.effectsStack.isEmpty()) {
                BrewEffect currentEffect = getCurrentEffect();
                if(currentEffect instanceof AttributeModifierEffect attrEff) {
                    // ADD MODIFIER
                    if(this.currentEffectRemainingTicks == attrEff.effectTicksDuration)
                        attrEff.applyPrimaryEffect(ownerEntity);
                    //REMOVE MODIFIER
                    else if(this.currentEffectRemainingTicks <= 1)
                        attrEff.applyAdditionalEffect(ownerEntity);
                }
                else if(currentEffect instanceof InstantEffect instantEff) {
                    instantEff.applyPrimaryEffect(ownerEntity);
                    instantEff.applyAdditionalEffect(ownerEntity);
                }
                this.currentEffectRemainingTicks--;
                this.totalTicksDuration--;
                // CHANGE TO THE NEXT EFFECT
                if(this.currentEffectRemainingTicks <= 0) {
                    this.effectsStack.removeFirst();
                    this.effectsIcons.removeFirst();
                    if(!this.effectsStack.isEmpty())
                        this.currentEffectRemainingTicks = this.effectsStack.getFirst().effectTicksDuration;
                    Services.PLATFORM.sendCustomPacket(serverPlayer, new SyncBrewManagerIconsPayload(this.effectsIcons));
                }
                Services.PLATFORM.sendCustomPacket(serverPlayer, new SyncBrewManagerDurationPayload(this.currentEffectRemainingTicks, this.totalTicksDuration));
            }
        }
    }

    @Override
    public List<BrewEffect> getEffectsStack() {
        return this.effectsStack;
    }

    @Override
    public int getCurrentCaffeine() {
        return this.currentCaffeine;
    }

    @Override
    public void setCurrentCaffeine(int newValue) {
        this.currentCaffeine = newValue;
    }

    public void addCaffeine(int addedCaffeine) {
        if(this.ownerEntity instanceof Player player)
            if(!player.getAbilities().instabuild)
                this.setCurrentCaffeine(Math.min(this.currentCaffeine + addedCaffeine, this.maxCaffeine));
    }

    @Override
    public int getCurrentCaffeinePercentage() {
        return Math.min(((this.currentCaffeine * 100) / this.maxCaffeine), 100);
    }

    @Override
    public boolean getOverloadStatus() {
        return this.isOverloaded;
    }

    @Override
    public void setOverloaded(boolean newValue) {
        this.isOverloaded = newValue;
    }

    @Override
    public void setEffectIcons(List<ResourceLocation> iconLocations) {
        this.effectsIcons = iconLocations;
    }

    @Override
    public List<ResourceLocation> getEffectIcons() {
        return this.effectsIcons;
    }
}
