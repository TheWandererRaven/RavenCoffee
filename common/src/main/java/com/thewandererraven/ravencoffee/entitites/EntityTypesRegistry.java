package com.thewandererraven.ravencoffee.entitites;

import com.thewandererraven.ravencoffee.Constants;
import com.thewandererraven.ravencoffee.registry.RegistryObject;
import com.thewandererraven.ravencoffee.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.block.Block;

public class EntityTypesRegistry {
    public static final RegistryProvider<EntityType<?>> ENTITY_TYPES = RegistryProvider.get(Registries.ENTITY_TYPE, Constants.MOD_ID);

    public static final String _muffin_id = "muffin";
    public static final RegistryObject<EntityType<ThrowableFoodEntity>> MUFFIN = ENTITY_TYPES.register(_muffin_id,
            () -> EntityType.Builder.<ThrowableFoodEntity>of(ThrowableFoodEntity::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, _muffin_id)))
    );

    public static void init() {

    }
}
