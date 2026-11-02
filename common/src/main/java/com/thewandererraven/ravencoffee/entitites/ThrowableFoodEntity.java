package com.thewandererraven.ravencoffee.entitites;

import com.thewandererraven.ravencoffee.item.GeneralItemsRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ThrowableFoodEntity extends ThrowableItemProjectile {
    public ThrowableFoodEntity(EntityType<? extends ThrowableFoodEntity> entityType, Level level) {
        super((EntityType<? extends ThrowableItemProjectile>) entityType, level);
    }

    public ThrowableFoodEntity(Level level, LivingEntity owner, ItemStack stack) {
        super(EntityTypesRegistry.MUFFIN.get(), owner, level, stack);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Entity hitEntity = hitResult.getEntity();
        if(hitEntity instanceof Animal) ((Animal)hitEntity).ate();

        if (hitEntity instanceof Player) {
            Player hitPlayer = (Player)hitEntity;
            FoodProperties foodProps = this.getItem().getComponents().get(DataComponents.FOOD);
            if (hitPlayer.canEat(false) && foodProps != null) {
                hitPlayer.getFoodData().eat(foodProps);
                hitPlayer.level().playSound(null, hitPlayer.blockPosition(), SoundEvents.GENERIC_EAT.value(), SoundSource.AMBIENT, 1.0f, 1.0f);
                hitPlayer.level().playSound(null, hitPlayer.blockPosition(), SoundEvents.PLAYER_BURP, SoundSource.AMBIENT, 1.0f, 1.0f);
            } else
                hitPlayer.hurt(damageSources().thrown(this, this.getOwner()), foodProps.nutrition());
        }
        else if (hitEntity instanceof Villager) {
            Villager hitVillager = (Villager)hitEntity;
            if (hitVillager.wantsMoreFood()) hitVillager.eatAndDigestFood();
            hitVillager.knockback(0.5f, this.position().x - hitEntity.getX(), this.position().z - hitEntity.getZ());
        }
        else if(hitEntity instanceof Animal) {
            Animal hitAnimal = (Animal)hitEntity;
            hitAnimal.setInLoveTime(600);
            this.level().broadcastEntityEvent(hitAnimal, (byte)18);
            hitAnimal.knockback(0.5f, this.position().x - hitEntity.getX(), this.position().z - hitEntity.getZ());
            hitAnimal.level().playSound(null, hitAnimal.blockPosition(), SoundEvents.GENERIC_EAT.value(), SoundSource.AMBIENT, 1.0f, 1.15f);
            hitAnimal.level().playSound(null, hitAnimal.blockPosition(), SoundEvents.PLAYER_BURP, SoundSource.AMBIENT, 1.0f, 1.1f);
        }
        else {
            hitEntity.hurt(damageSources().thrown(this, this.getOwner()), (float) 1.0);
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (this.level().isClientSide) {
            this.addParticles();
            this.discard();
        }

    }

    private void addParticles() {
        for (int i = 0; i < 8; i++) {
            this.level()
                    .addParticle(
                            new ItemParticleOption(ParticleTypes.ITEM, this.getItem()),
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            (this.random.nextFloat() - 0.5) * 0.08,
                            (this.random.nextFloat() - 0.5) * 0.08,
                            (this.random.nextFloat() - 0.5) * 0.08
                    );
        }
    }

    @Override
    protected Item getDefaultItem() {
        return GeneralItemsRegistry.MUFFIN.get();
    }
}
