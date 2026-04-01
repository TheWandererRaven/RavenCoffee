package com.thewandererraven.ravencoffee.effect.breweffect;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.datacomponents.CoffeeBrewData;
import com.thewandererraven.ravencoffee.datacomponents.BrewEffectData;
import com.thewandererraven.ravencoffee.networking.SyncBrewManagerCaffeinePayload;
import com.thewandererraven.ravencoffee.networking.SyncBrewManagerDurationPayload;
import com.thewandererraven.ravencoffee.networking.SyncBrewManagerIconsPayload;
import com.thewandererraven.ravencoffee.platform.Services;
import com.thewandererraven.ravencoffee.util.BrewEffectsUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

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

    public void generateEffectIconLocations() {
        this.effectsIcons.clear();
        for(BrewEffect effect: this.effectsStack) {
            this.effectsIcons.add(effect.generateIconLocation());
        }
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
                    if(this.effectsStack.size() >= this.maxEffectsStackSize)
                        break;
                }

            this.generateEffectIconLocations();
            this.addCaffeine(brewData.caffeine());
            this.calculateTotalRemainingTicks();

            if(this.currentCaffeine >= this.maxCaffeine) {
                // TODO: Here goes te drawback of
                Constants.LOG.info("CAFFEINE OVERLOAD!");
                this.isOverloaded = true;
            }
            this.sendAllInfoToClient();
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.effectsStack.isEmpty();
    }

    public BrewEffect createBrewEffect(BrewEffectData data) {
        return createBrewEffect(data.id(), data.duration(), data.mainValue(), data.secondaryValue());
    }

    public BrewEffect createBrewEffect(ResourceLocation effectCoreId, int ticksDuration, double mainValue, double secondaryValue) {
        BrewEffect newEffect = null;
        BrewEffectCore effectCore = BrewEffectsUtils.findEffectInRegistry(effectCoreId);
        if(effectCore == null) {
            Constants.LOG.error("Unable to find brew effect for: {}", effectCoreId.toString());
            return BrewEffect.EMPTY;
        }

        if(effectCore.type.equals("attribute_modifier")) {
            newEffect = new AttributeModifierEffect(effectCoreId, effectCore.attributeId, effectCore.attributeOperation, ticksDuration, mainValue, secondaryValue,
                    BrewEffectsUtils.findAttributeByItsId(this.ownerEntity.level(), effectCore.attributeId),
                    effectCore.primaryEffect,
                    effectCore.additionalEffect
            );
        } else if(effectCore.type.equals("instant")) {
            newEffect = new InstantEffect(effectCoreId, mainValue, secondaryValue, effectCore.primaryEffect);
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
            Services.PLATFORM.sendCustomPacket(player, new SyncBrewManagerCaffeinePayload(this.currentCaffeine, this.isOverloaded));
        } else if(this.isOverloaded) {
            this.isOverloaded = false;
            Services.PLATFORM.sendCustomPacket(player, new SyncBrewManagerCaffeinePayload(this.currentCaffeine, this.isOverloaded));
        }
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
                    if(!this.effectsStack.isEmpty()) {
                        this.currentEffectRemainingTicks = this.effectsStack.getFirst().effectTicksDuration;
                        this.calculateTotalRemainingTicks();
                    }
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

    @Override
    public void clearEffects() {
        // Remove att modifier
        if(!this.effectsStack.isEmpty())
            if(this.effectsStack.getFirst() instanceof AttributeModifierEffect attModEffect)
                attModEffect.applyAdditionalEffect(this.ownerEntity);

        this.effectsStack.clear();
        this.currentEffectRemainingTicks = 0;
        this.totalTicksDuration = 0;
        this.effectsIcons.clear();
        this.sendAllInfoToClient();
    }

    @Override
    public void clearCaffeine() {
        this.currentCaffeine = 0;
        this.isOverloaded = false;
    }

    @Override
    public void clearAll() {
        this.clearEffects();
        this.clearCaffeine();
    }

    @Override
    public void sendEffectIconsToClient() {
        if (this.ownerEntity instanceof ServerPlayer serverPlayer) {
            Services.PLATFORM.sendCustomPacket(serverPlayer, new SyncBrewManagerIconsPayload(this.effectsIcons));
        }
    }

    @Override
    public void sendDurationsToClient() {
        if (this.ownerEntity instanceof ServerPlayer serverPlayer) {
            Services.PLATFORM.sendCustomPacket(serverPlayer, new SyncBrewManagerDurationPayload(this.currentEffectRemainingTicks, this.totalTicksDuration));
        }
    }

    @Override
    public void sendCaffeineToClient() {
        if (this.ownerEntity instanceof ServerPlayer serverPlayer) {
            Services.PLATFORM.sendCustomPacket(serverPlayer, new SyncBrewManagerCaffeinePayload(this.currentCaffeine, this.isOverloaded));
        }
    }

    @Override
    public void sendAllInfoToClient() {
        this.sendEffectIconsToClient();
        this.sendDurationsToClient();
        this.sendCaffeineToClient();
    }


    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        ListTag list = new ListTag();
        for (BrewEffect effect : this.effectsStack) {
            CompoundTag effectTag = new CompoundTag();
            effectTag.putString("Id", effect.effectId.toString());
            effectTag.putInt("TicksDuration", effect.effectTicksDuration);
            effectTag.putDouble("MainValue", effect.mainValue);
            effectTag.putDouble("SecondaryValue", effect.secondaryValue);
            list.add(effectTag);
        }

        tag.put("EffectsStack", list);
        tag.putInt("CurrentEffectRemainingTicks", this.currentEffectRemainingTicks);
        tag.putInt("CurrentCaffeine", this.currentCaffeine);
        tag.putBoolean("IsOverloaded", this.isOverloaded);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.effectsStack.clear();

        ListTag list = tag.getListOrEmpty("EffectsStack");
        for (Tag _effectTag : list) {
            CompoundTag effectTag = (CompoundTag) _effectTag;

            effectsStack.add(createBrewEffect(
                    ResourceLocation.parse(effectTag.getString("Id").orElse(Constants.MOD_ID + ":effect.empty")),
                    effectTag.getInt("TicksDuration").orElse(0),
                    effectTag.getDouble("MainValue").orElse(0.0),
                    effectTag.getDouble("SecondaryValue").orElse(0.0)
            ));
        }
        this.currentEffectRemainingTicks = tag.getInt("CurrentEffectRemainingTicks").orElse(0);
        if(this.currentEffectRemainingTicks > 0)
            this.effectsStack.getFirst().applyPrimaryEffect(this.ownerEntity);
        this.currentCaffeine = tag.getInt("CurrentCaffeine").orElse(0);
        this.isOverloaded = tag.getBoolean("IsOverloaded").orElse(false);
        this.generateEffectIconLocations();
        this.calculateTotalRemainingTicks();
    }
}
