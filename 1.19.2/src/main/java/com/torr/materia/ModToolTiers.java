package com.torr.materia;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.util.LazyLoadedValue;

import java.util.function.Supplier;

public enum ModToolTiers implements Tier {
    // Hand axe tier - similar to wood but slightly less durable
    HAND_AXE(0, 45, 2.0F, 0.0F, 15, () -> {
        return Ingredient.of(ModItems.KNAPPED_FLINT.get());
    }),
    // Bronze tier - between stone and iron
    BRONZE(2, 300, 6.0F, 2.0F, 14, () -> {
        return Ingredient.of(ModItems.BRONZE_INGOT.get());
    }),
    // Wrought iron tier - sturdier than bronze, repairs with wrought iron
    WROUGHT_IRON(2, 400, 6.2F, 2.0F, 12, () -> {
        return Ingredient.of(ModItems.WROUGHT_IRON_INGOT.get());
    });

    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    private ModToolTiers(int level, int uses, float speed, float damage, int enchantmentValue, Supplier<Ingredient> repairIngredient) {
        this.level = level;
        this.uses = uses;
        this.speed = speed;
        this.damage = damage;
        this.enchantmentValue = enchantmentValue;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
    }

    public int getUses() {
        return this.uses;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAttackDamageBonus() {
        return this.damage;
    }

    public int getLevel() {
        return this.level;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
} 