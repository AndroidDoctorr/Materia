package com.torr.materia;

import com.torr.materia.entity.RockEntity;
import com.torr.materia.entity.FallingAmphoraEntity;
import com.torr.materia.entity.DynamiteEntity;
import com.torr.materia.entity.MetalArrowEntity;
import com.torr.materia.entity.CannonballEntity;
import com.torr.materia.entity.CannonTntEntity;
import com.torr.materia.entity.CanisterShotEntity;
import com.torr.materia.entity.ShrapnelEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, materia.MOD_ID);

    public static final RegistryObject<EntityType<RockEntity>> ROCK_PROJECTILE = ENTITIES.register(
            "rock_projectile",
            () -> EntityType.Builder.<RockEntity>of(RockEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("rock_projectile"));

    public static final RegistryObject<EntityType<com.torr.materia.entity.FlintSpearEntity>> FLINT_SPEAR_PROJECTILE = ENTITIES.register(
            "flint_spear_projectile",
            () -> EntityType.Builder.<com.torr.materia.entity.FlintSpearEntity>of(com.torr.materia.entity.FlintSpearEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("flint_spear_projectile"));

    public static final RegistryObject<EntityType<FallingAmphoraEntity>> FALLING_AMPHORA = ENTITIES.register(
            "falling_amphora",
            () -> EntityType.Builder.<FallingAmphoraEntity>of(FallingAmphoraEntity::new, MobCategory.MISC)
                    .sized(0.98F, 0.98F)
                    .clientTrackingRange(10)
                    .updateInterval(20)
                    .build("falling_amphora"));

    public static final RegistryObject<EntityType<DynamiteEntity>> DYNAMITE_PROJECTILE = ENTITIES.register(
            "dynamite_projectile",
            () -> EntityType.Builder.<DynamiteEntity>of(DynamiteEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("dynamite_projectile"));

    public static final RegistryObject<EntityType<CannonballEntity>> CANNONBALL_PROJECTILE = ENTITIES.register(
            "cannonball_projectile",
            () -> EntityType.Builder.<CannonballEntity>of(CannonballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("cannonball_projectile"));

    public static final RegistryObject<EntityType<CannonTntEntity>> CANNON_TNT_PROJECTILE = ENTITIES.register(
            "cannon_tnt_projectile",
            () -> EntityType.Builder.<CannonTntEntity>of(CannonTntEntity::new, MobCategory.MISC)
                    .sized(0.98F, 0.98F)
                    .clientTrackingRange(6)
                    .updateInterval(10)
                    .build("cannon_tnt_projectile"));

    public static final RegistryObject<EntityType<CanisterShotEntity>> CANISTER_SHOT_PROJECTILE = ENTITIES.register(
            "canister_shot_projectile",
            () -> EntityType.Builder.<CanisterShotEntity>of(CanisterShotEntity::new, MobCategory.MISC)
                    .sized(0.35F, 0.35F)
                    .clientTrackingRange(6)
                    .updateInterval(10)
                    .build("canister_shot_projectile"));

    public static final RegistryObject<EntityType<ShrapnelEntity>> SHRAPNEL_PROJECTILE = ENTITIES.register(
            "shrapnel_projectile",
            () -> EntityType.Builder.<ShrapnelEntity>of(ShrapnelEntity::new, MobCategory.MISC)
                    .sized(0.15F, 0.15F)
                    .clientTrackingRange(6)
                    .updateInterval(10)
                    .build("shrapnel_projectile"));

    // One shared entity type for all metal arrows (pickup stack determines the exact item)
    public static final RegistryObject<EntityType<MetalArrowEntity>> METAL_ARROW = ENTITIES.register(
            "metal_arrow",
            () -> EntityType.Builder.<MetalArrowEntity>of(MetalArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("metal_arrow"));
}


