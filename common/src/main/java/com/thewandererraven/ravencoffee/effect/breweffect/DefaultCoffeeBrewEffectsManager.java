package com.thewandererraven.ravencoffee.effect.breweffect;

import com.thewandererraven.ravenbrewslib.brew.data.BrewEffectDefinition;
import com.thewandererraven.ravenbrewslib.brew.effect.AttributeModifierBrewEffectBehaviour;
import com.thewandererraven.ravenbrewslib.brew.effect.BrewEffectInstance;
import com.thewandererraven.ravenbrewslib.brew.effect.IBrewEffectsManager;
import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.datacomponents.CoffeeBrewData;
import com.thewandererraven.ravencoffee.networking.SyncBrewGuiDisplayCaffeinePayload;
import com.thewandererraven.ravencoffee.networking.SyncBrewGuiDisplayDurationsPayload;
import com.thewandererraven.ravencoffee.networking.SyncBrewGuiDisplayIconsPayload;
import com.thewandererraven.ravencoffee.platform.Services;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class DefaultCoffeeBrewEffectsManager implements ICoffeeBrewEffectsManager, IBrewEffectsManager {
    private List<BrewEffectDefinition> effectsStack = null;
    private BrewEffectInstance currentEffect = null;
    public int totalRemainingTicks = 0;
    private int currentCaffeine = 0;
    private boolean isOverloaded = false;

    private final int maxEffectsStackSize = 5;
    private final int maxCaffeine = 30 * 20;

    private final LivingEntity ownerEntity;

    public DefaultCoffeeBrewEffectsManager(LivingEntity owner) {
        this.ownerEntity = owner;
        this.effectsStack = new ArrayList<>();
    }

    public List<ResourceLocation> generateEffectIconLocations() {
        ArrayList<ResourceLocation> icons = new ArrayList<>(List.of());
        for(BrewEffectDefinition effect: this.effectsStack)
            icons.add(effect.generateIconLocation());
        return icons;
    }

    public boolean add(CoffeeBrewData brewData) {
        if(!this.isOverloaded) {
            if(this.effectsStack.size() < this.maxEffectsStackSize)
                this.add(brewData.effects());

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
    public void add(List<BrewEffectDefinition> brewData) {
        boolean shouldUpdateCurrEff = this.effectsStack.isEmpty();
        for(BrewEffectDefinition effectData: brewData) {
            this.effectsStack.add(effectData);
            if(this.effectsStack.size() >= this.maxEffectsStackSize)
                break;
        }
        if(shouldUpdateCurrEff)
            this.updateCurrentEffect();
    }

    @Override
    public boolean isEmpty() {
        return this.effectsStack.isEmpty();
    }

    public void setClientEffects(List<BrewEffectDefinition> list) {
        this.effectsStack = list;
    }

    public boolean setClientEffect(BrewEffectDefinition effectData, int index) {
        if(index < this.effectsStack.size()) {
            this.effectsStack.set(index, effectData);
            return true;
        }
        return false;
    }

//    public boolean setClientEffect(BrewEffectDefinition effectData) {
//        for(int i = 0; i < this.effectsStack.size(); i++)
//        {
//            if(this.effectsStack.get(i)..equals(effectData.id()))
//                return this.setClientEffect(effectData, i);totalRemainingTicks
//        }
//        return false;
//    }

    public BrewEffectDefinition getEffect(int index)
    {
        return this.effectsStack.get(index);
    }

    public BrewEffectInstance getCurrentEffect()
    {
        return this.currentEffect;
    }

    public void setCurrentEffectRemainingTicks(int remainingTicks) {
        if(this.getCurrentEffect() != null)
            this.getCurrentEffect().remainingTicks = remainingTicks;
    }

    public int getCurrentEffectRemainingTicks() {
        if(this.getCurrentEffect() != null)
            return this.getCurrentEffect().remainingTicks;
        return 0;
    }

    public void updateCurrentEffect() {
        if(!this.effectsStack.isEmpty())
            this.currentEffect = new BrewEffectInstance(this.getEffect(0));
        else
            this.currentEffect = null;
    }

    public void calculateTotalRemainingTicks() {
        int totalTicksDuration = this.getCurrentEffectRemainingTicks();
        for(int i = 1; i < this.effectsStack.size(); i++) {
            totalTicksDuration += this.effectsStack.get(i).duration();
        }
        setTotalRemainingTicks(totalTicksDuration);
    }

    public void setTotalRemainingTicks(int totalRemainingTicks) {
        this.totalRemainingTicks = totalRemainingTicks;
    }

    public int getBrewTotalRemainingSeconds() {
        return (int) Math.ceil(this.totalRemainingTicks / 20.0);
    }

    public boolean areCurrentEffectTicksInAFullSecond() {
        return this.getCurrentEffectRemainingTicks() % 20 == 0;
    }

    public int getCurrentEffectRemainingSeconds() {
        return (int) Math.ceil(this.getCurrentEffectRemainingTicks() / 20.0);
    }

    public void tickCaffeine(ServerPlayer player) {
        if(this.currentCaffeine > 0) {
            this.currentCaffeine -= 1;
        } else if(this.isOverloaded) {
            this.isOverloaded = false;
        } else
            return;
        this.sendCaffeineToClient();
    }

    @Override
    public void tick() {
        if (ownerEntity instanceof ServerPlayer serverPlayer) {
            this.tickCaffeine(serverPlayer);
            if(!this.effectsStack.isEmpty()) {
                this.tickCurrentEffect(serverPlayer);
                this.totalRemainingTicks--;
            }
        }
        //Constants.LOG.info("TOTAL TICKS: {}", this.totalRemainingTicks);
    }

    public void tickCurrentEffect(ServerPlayer serverPlayer) {
        BrewEffectInstance currentEffect = getCurrentEffect();
        if(currentEffect == null)
            return;
        if(currentEffect.isEffectEnding()) {
            if(currentEffect.effectBehaviour instanceof AttributeModifierBrewEffectBehaviour)
                currentEffect.applyAdditionalEffect(ownerEntity); // AKA: remove attr modifier
            this.effectsStack.removeFirst();
            this.updateCurrentEffect();
            this.sendDurationsToClient();
            this.sendEffectIconsToClient();
            return;
        }

        if(currentEffect.effectBehaviour instanceof AttributeModifierBrewEffectBehaviour) {
            if(currentEffect.isEffectStarting())
                currentEffect.applyPrimaryEffect(ownerEntity);
        } else {
            currentEffect.applyPrimaryEffect(ownerEntity);
            currentEffect.applyAdditionalEffect(ownerEntity);
        }
        currentEffect.remainingTicks--;
        this.sendDurationsToClient();
        //Constants.LOG.info("CURR EFF REMAINING TICKS: {}", this.getCurrentEffectRemainingTicks());
    }

    @Override
    public List<BrewEffectDefinition> getEffectsStack() {
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
    public void clearEffects() {
        // Remove att modifier
        if(!this.effectsStack.isEmpty()) {
            BrewEffectInstance currEff = this.getCurrentEffect();
            if(currEff != null)
                if (currEff.effectBehaviour instanceof AttributeModifierBrewEffectBehaviour)
                    currEff.applyAdditionalEffect(this.ownerEntity);
        }

        this.effectsStack.clear();
        this.updateCurrentEffect();
        this.totalRemainingTicks = 0;
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
            Services.PLATFORM.sendCustomPacket(serverPlayer, new SyncBrewGuiDisplayIconsPayload(this.generateEffectIconLocations()));
        }
    }

    @Override
    public void sendDurationsToClient() {
        if (this.ownerEntity instanceof ServerPlayer serverPlayer) {
            Services.PLATFORM.sendCustomPacket(serverPlayer, new SyncBrewGuiDisplayDurationsPayload(this.getCurrentEffectRemainingSeconds(), this.getBrewTotalRemainingSeconds()));
        }
    }

    @Override
    public void sendCaffeineToClient() {
        if (this.ownerEntity instanceof ServerPlayer serverPlayer) {
            Services.PLATFORM.sendCustomPacket(serverPlayer, new SyncBrewGuiDisplayCaffeinePayload(this.getCurrentCaffeinePercentage(), this.isOverloaded));
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
        for (BrewEffectDefinition effect : this.effectsStack) {
            CompoundTag effectTag = new CompoundTag();
            effectTag.putString("Id", effect.id().toString());
            effectTag.putInt("Duration", effect.duration());
            effectTag.putDouble("MainValue", effect.mainValue());
            effectTag.putDouble("SecondaryValue", effect.secondaryValue());
            list.add(effectTag);
        }

        tag.put("EffectsStack", list);
        tag.putInt("CurrentEffectRemainingTicks", this.getCurrentEffectRemainingTicks());
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

            effectsStack.add(new BrewEffectDefinition(
                    ResourceLocation.parse(effectTag.getString("Id").orElse(Constants.MOD_ID + ":effect.empty")),
                    effectTag.getInt("Duration").orElse(0),
                    effectTag.getDouble("MainValue").orElse(0.0),
                    effectTag.getDouble("SecondaryValue").orElse(0.0)
            ));
        }
        this.updateCurrentEffect();
        this.setCurrentEffectRemainingTicks(tag.getInt("CurrentEffectRemainingTicks").orElse(0));
        if(this.getCurrentEffectRemainingTicks() > 0 && this.getCurrentEffect() != null)
            this.getCurrentEffect().applyPrimaryEffect(this.ownerEntity);
        this.currentCaffeine = tag.getInt("CurrentCaffeine").orElse(0);
        this.isOverloaded = tag.getBoolean("IsOverloaded").orElse(false);
        this.generateEffectIconLocations();
        this.calculateTotalRemainingTicks();
    }
}
