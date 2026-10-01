package com.thewandererraven.ravencoffee.brew;

import com.thewandererraven.ravenbrewslib.brew.data.BrewEffectDefinition;
import com.thewandererraven.ravenbrewslib.brew.effect.BrewEffectBehaviour;
import com.thewandererraven.ravenbrewslib.brew.effect.BrewEffectInstance;
import com.thewandererraven.ravenbrewslib.brew.effect.IBrewEffectsManager;
import com.thewandererraven.ravenbrewslib.utils.BrewEffectsUtils;
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
import java.util.Comparator;
import java.util.List;

public class CoffeeBrewEffectsManager implements IBrewEffectsManager, ICoffeeBrewEffectsManager {
    private final int maxEffectsStackSize = 5;
    private List<BrewEffectDefinition> inactiveEffects;
    private List<BrewEffectInstance> activeEffects;
    private List<ResourceLocation> finishedEffects;

    private final int maxCaffeine = 180 * 20;
    private int currentCaffeine = 0;
    private boolean isOverloaded = false;

    private final LivingEntity ownerEntity;

    public CoffeeBrewEffectsManager(LivingEntity owner) {
        this.ownerEntity = owner;
        this.inactiveEffects = new ArrayList<>();
        this.activeEffects = new ArrayList<>();
        this.finishedEffects = new ArrayList<>();
    }

    @Override
    public LivingEntity getOwnerEntity() {
        return ownerEntity;
    }

    @Override
    public void clearAllData() {
        IBrewEffectsManager.super.clearAllData();
        this.clearCaffeine();
        this.sendAllInfoToClient();
    }

    // ================================================== INACTIVE EFFECTS

    @Override
    public List<BrewEffectDefinition> getInactiveEffects() {
        return this.inactiveEffects;
    }

    @Override
    public void addInactiveEffects(List<BrewEffectDefinition> brewData) {
        if(this.inactiveEffects.size() >= this.maxEffectsStackSize)
            return;
        //boolean shouldUpdateActiveEffects = this.hasActiveEffects() || this.hasInactiveEffects();

        int newEffectsCountLimit = this.maxEffectsStackSize - this.inactiveEffects.size();
        List<BrewEffectDefinition> effDefsToAdd = new ArrayList<>(brewData.stream()
                .filter(effDef ->
                        this.inactiveEffects.stream().noneMatch(
                                actEff -> actEff.id().equals(effDef.id())
                        )
                ).limit(newEffectsCountLimit)
                .toList()
        );


        this.inactiveEffects.addAll(effDefsToAdd);
        this.inactiveEffects.sort(Comparator.comparingInt(BrewEffectDefinition::priority));

        //if(shouldUpdateActiveEffects)
        this.updateActiveEffects();
    }

    @Override
    public void removeInactiveEffects(List<ResourceLocation> effIds) {
        this.inactiveEffects.removeIf(eff -> effIds.stream().anyMatch(effId -> effId.equals(eff.id())));
    }

    public List<ResourceLocation> generateInactiveEffectIconLocations() {
        ArrayList<ResourceLocation> icons = new ArrayList<>(List.of());
        for(BrewEffectDefinition effect: this.inactiveEffects)
            icons.add(effect.generateIconLocation());
        return icons;
    }

    // ================================================== ACTIVE EFFECTS

    @Override
    public List<BrewEffectInstance> getActiveEffects() {
        return this.activeEffects;
    }

    @Override
    public void addActiveEffects(List<BrewEffectInstance> effInstances) {
        for(BrewEffectInstance effInstance : effInstances) {
            int foundActiveEffIndex = -1;
            for(int i = 0; i < this.activeEffects.size(); i++) {
                BrewEffectInstance activeEff = this.activeEffects.get(i);
                if(effInstance.effectBehaviour.id.equals(activeEff.effectBehaviour.id))
                    foundActiveEffIndex = i;
            }
            if(foundActiveEffIndex >= 0)
                this.activeEffects.set(foundActiveEffIndex, effInstance);
            else
                this.activeEffects.add(effInstance);
        }
    }

    @Override
    public void addActiveEffectsFromDefinitions(List<BrewEffectDefinition> effDefs) {
        this.addActiveEffects(effDefs.stream().map(effDef -> new BrewEffectInstance(this.ownerEntity.level(), effDef)).toList());
    }

    @Override
    public void removeActiveEffects(List<ResourceLocation> effIds) {
        this.activeEffects.removeIf(eff -> effIds.stream().anyMatch(effId -> effId.equals(eff.effectBehaviour.id)));
    }

    public void updateActiveEffects() {
        if(this.hasInactiveEffects() && !this.hasActiveEffects()) {
            int inactiveEffsLowestPriority = this.getInactiveEffect(0).priority();
            List<BrewEffectDefinition> effectsToAdd = this.getInactiveEffects().stream().filter(eff -> inactiveEffsLowestPriority == eff.priority()).toList();
            if(!effectsToAdd.isEmpty()) {
                this.addActiveEffectsFromDefinitions(effectsToAdd);
                this.removeInactiveEffects(effectsToAdd.stream().map(BrewEffectDefinition::id).toList());
                this.sendAllInfoToClient();
            }
        }
    }

    public int getActiveEffectsMaxRemainingTicks() {
        if(this.hasActiveEffects())
            return this.activeEffects.stream().mapToInt(eff -> eff.remainingTicks).max().orElse(0);
        return 0;
    }

    public List<ResourceLocation> generateActiveEffectIconLocations() {
        ArrayList<ResourceLocation> icons = new ArrayList<>(List.of());
        for(BrewEffectInstance effect: this.activeEffects)
            icons.add(effect.effectBehaviour.generateIconLocation());
        return icons;
    }

    // ================================================== GENERAL EFFECTS

    public int calculateTotalRemainingTicks() {
        int totalTicksDuration = this.getActiveEffectsMaxRemainingTicks();
        for(BrewEffectDefinition effDef : this.inactiveEffects) {
            totalTicksDuration += effDef.duration();
        }
        return totalTicksDuration;
    }

    public boolean add(CoffeeBrewData brewData) {
        if(!this.isOverloaded) {
            if(this.inactiveEffects.size() < this.maxEffectsStackSize)
                this.addInactiveEffects(brewData.effects());

            this.generateInactiveEffectIconLocations();
            this.addCaffeine(brewData.caffeine());

//            if(this.currentCaffeine >= this.maxCaffeine) {
//                // TODO: Here goes te drawback of
//                Constants.LOG.info("CAFFEINE OVERLOAD!");
//                this.setOverloaded(true);
//            }
            this.sendAllInfoToClient();
            return true;
        }
        return false;
    }

    public static List<BrewEffectDefinition.Builder> getListOfDefaultEffects() {
        return List.of(
                new BrewEffectDefinition.Builder(
                        ResourceLocation.fromNamespaceAndPath(com.thewandererraven.ravenbrewslib.Constants.MOD_ID, "effect.mining_efficiency"),
                        10,
                        15 * 20,
                        0,
                        5.0,
                        0.0
                ),
                new BrewEffectDefinition.Builder(
                        ResourceLocation.fromNamespaceAndPath(com.thewandererraven.ravenbrewslib.Constants.MOD_ID, "effect.mining_fatigue"),
                        15,
                        7 * 20,
                        0,
                        0.2,
                        0.0
                )
        );
    }

    // ================================================== TICK
    @Override
    public void tick() {
        if (ownerEntity instanceof ServerPlayer serverPlayer) {
            this.tickCaffeine(serverPlayer);
            if(this.hasActiveEffects()) {
                for(BrewEffectInstance effInstance : this.activeEffects)
                    this.tickActiveEffect(effInstance);
            }
            if(!this.finishedEffects.isEmpty()) {
                this.removeActiveEffects(this.finishedEffects);
                this.finishedEffects.clear();
                this.updateActiveEffects();
            }
        }
    }

    public void tickActiveEffect(BrewEffectInstance effInstance) {
        Constants.LOG.info("=====================================");
        Constants.LOG.info("CURR EFF R: {}", effInstance.effectBehaviour.id);
        Constants.LOG.info("CURR EFF REMAINING TICKS: {}", effInstance.remainingTicks);

        if (effInstance.effectBehaviour.tickMode != BrewEffectBehaviour.TickMode.IGNORE) {
            if (effInstance.effectBehaviour.tickMode == BrewEffectBehaviour.TickMode.START_AND_END) {
                if (effInstance.isEffectStarting())
                    effInstance.applyPrimaryEffect(ownerEntity);
            } else if (effInstance.effectBehaviour.tickMode != BrewEffectBehaviour.TickMode.INTERVAL || effInstance.isEffectAtInterval()) {
                effInstance.applyPrimaryEffect(ownerEntity);
                effInstance.applyAdditionalEffect(ownerEntity);
            }
        }

        if (effInstance.isEffectEnding()) {
            if (effInstance.effectBehaviour.tickMode == BrewEffectBehaviour.TickMode.START_AND_END)
                effInstance.applyAdditionalEffect(ownerEntity); // AKA: remove attr modifier

            this.finishedEffects.add(effInstance.effectBehaviour.id);
            this.sendDurationsToClient();
            this.sendEffectIconsToClient();
            return;
        }

        effInstance.remainingTicks--;
        this.sendDurationsToClient();
    }

    // ================================================== CAFFEINE

    @Override
    public int getMaxCaffeine() {
        return this.maxCaffeine;
    }

    @Override
    public int getCurrentCaffeine() {
        return this.currentCaffeine;
    }

    @Override
    public void setCurrentCaffeine(int newValue) {
        this.currentCaffeine = newValue;
    }

    @Override
    public void addCaffeine(int addedCaffeine) {
        if(this.ownerEntity instanceof Player player)
            if(!player.getAbilities().instabuild) {
                this.setCurrentCaffeine(Math.min(this.currentCaffeine + addedCaffeine, this.maxCaffeine));
                if(isOverloaded())
                    this.setOverloadedStatus(true);
            }
    }

    @Override
    public boolean getOverloadStatus() {
        return this.isOverloaded;
    }

    @Override
    public void setOverloadedStatus(boolean newValue) {
        this.isOverloaded = newValue;
    }

    @Override
    public boolean isOverloaded() {
        return this.getCurrentCaffeine() > this.maxCaffeine;
    }

    @Override
    public void tickCaffeine(ServerPlayer player) {
        if(this.currentCaffeine > 0)
            this.currentCaffeine -= 1;
        else if(this.getOverloadStatus())
            this.setOverloadedStatus(false);
        else
            return;
        this.sendCaffeineToClient();
    }

    // ================================================== NETWORKING

    public void sendEffectIconsToClient() {
        if (this.ownerEntity instanceof ServerPlayer serverPlayer) {
            Services.PLATFORM.sendCustomPacket(serverPlayer, new SyncBrewGuiDisplayIconsPayload(this.generateInactiveEffectIconLocations(), this.generateActiveEffectIconLocations()));
        }
    }

    public void sendDurationsToClient() {
        if (this.ownerEntity instanceof ServerPlayer serverPlayer) {
            Services.PLATFORM.sendCustomPacket(serverPlayer, new SyncBrewGuiDisplayDurationsPayload(
                    (int) BrewEffectsUtils.getSecondsFromTicks(this.calculateTotalRemainingTicks()),
                    this.activeEffects.stream().map(eff -> (int) BrewEffectsUtils.getSecondsFromTicks(eff.remainingTicks)).toList()
            ));
        }
    }

    public void sendCaffeineToClient() {
        if (this.ownerEntity instanceof ServerPlayer serverPlayer) {
            Services.PLATFORM.sendCustomPacket(serverPlayer, new SyncBrewGuiDisplayCaffeinePayload(this.getCurrentCaffeinePercentage(), this.isOverloaded));
        }
    }

    public void sendAllInfoToClient() {
        this.sendEffectIconsToClient();
        this.sendDurationsToClient();
        this.sendCaffeineToClient();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        ListTag inactiveEffs = new ListTag();
        for (BrewEffectDefinition effect : this.inactiveEffects) {
            CompoundTag effectTag = new CompoundTag();
            effectTag.putString("Id", effect.id().toString());
            effectTag.putInt("Priority", effect.priority());
            effectTag.putInt("Duration", effect.duration());
            effectTag.putInt("IntervalDuration", effect.duration());
            effectTag.putDouble("MainValue", effect.mainValue());
            effectTag.putDouble("SecondaryValue", effect.secondaryValue());
            inactiveEffs.add(effectTag);
        }
        ListTag activeEffs = new ListTag();
        for (BrewEffectInstance effect : this.activeEffects) {
            CompoundTag effectTag = new CompoundTag();
            effectTag.putString("Id", effect.effectBehaviour.id.toString());
            effectTag.putInt("RemainingTicks", effect.remainingTicks);
            effectTag.putInt("Duration", effect.duration);
            effectTag.putInt("IntervalDuration", effect.duration);
            effectTag.putDouble("MainValue", effect.mainValue);
            effectTag.putDouble("SecondaryValue", effect.secondaryValue);
            activeEffs.add(effectTag);
        }

        tag.putInt("CurrentCaffeine", this.currentCaffeine);
        tag.putBoolean("IsOverloaded", this.isOverloaded);
        tag.put("InactiveEffects", inactiveEffs);
        tag.put("ActiveEffects", activeEffs);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        IBrewEffectsManager.super.clearAllData();
        this.clearCaffeine();

        ListTag inactiveEffs = tag.getListOrEmpty("InactiveEffects");
        for (Tag _effectTag : inactiveEffs) {
            CompoundTag effectTag = (CompoundTag) _effectTag;
            this.inactiveEffects.add(new BrewEffectDefinition(
                    ResourceLocation.parse(effectTag.getString("Id").orElse(Constants.MOD_ID + ":effect.empty")),
                    effectTag.getInt("Priority").orElse(10),
                    effectTag.getInt("Duration").orElse(0),
                    effectTag.getInt("IntervalDuration").orElse(0),
                    effectTag.getDouble("MainValue").orElse(0.0),
                    effectTag.getDouble("SecondaryValue").orElse(0.0)
            ));
        }
        ListTag activeEffs = tag.getListOrEmpty("ActiveEffects");
        for (Tag _effectTag : activeEffs) {
            CompoundTag effectTag = (CompoundTag) _effectTag;
            BrewEffectInstance newEffInstance = new BrewEffectInstance(
                    BrewEffectsUtils.findEffectBehaviour(this.ownerEntity.level(), ResourceLocation.parse(effectTag.getString("Id").orElse(Constants.MOD_ID + ":effect.empty"))),
                    effectTag.getInt("Duration").orElse(0),
                    effectTag.getInt("IntervalDuration").orElse(0),
                    effectTag.getDouble("MainValue").orElse(0.0),
                    effectTag.getDouble("SecondaryValue").orElse(0.0)
            );
            this.activeEffects.add(newEffInstance);
            if((newEffInstance.remainingTicks > 0 && newEffInstance.remainingTicks < newEffInstance.duration) && newEffInstance.effectBehaviour.tickMode == BrewEffectBehaviour.TickMode.START_AND_END)
                newEffInstance.applyPrimaryEffect(this.ownerEntity);
        }

        this.updateActiveEffects();
        this.currentCaffeine = tag.getInt("CurrentCaffeine").orElse(0);
        this.isOverloaded = tag.getBoolean("IsOverloaded").orElse(false);
        //this.generateActiveEffectIconLocations();
//        this.calculateTotalRemainingTicks();
    }
}
