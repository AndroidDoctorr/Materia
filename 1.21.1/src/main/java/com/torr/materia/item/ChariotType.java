package com.torr.materia.item;

import com.torr.materia.materia;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Bronze and iron chariot variants (single entity type, synced on the chariot). */
public enum ChariotType {
    /** ~80 HP, modest damage reduction — sturdier than a typical wood cart. */
    BRONZE("bronze", 1.05F, 1.33F, 0.85F, 0.75F),
    /** ~120 HP, heavy armor — built to shrug off arrow fire in combat. */
    IRON("iron", 1.25F, 2.0F, 0.50F, 0.35F);

    public static final float BASE_MAX_HEALTH = 60.0F;

    private static final Map<String, ChariotType> BY_ID = Arrays.stream(values())
            .collect(Collectors.toMap(ChariotType::getId, Function.identity()));

    private final String id;
    private final float massFactor;
    private final float toughness;
    private final float damageMultiplier;
    private final float fireDamageMultiplier;

    ChariotType(String id, float massFactor, float toughness, float damageMultiplier, float fireDamageMultiplier) {
        this.id = id;
        this.massFactor = massFactor;
        this.toughness = toughness;
        this.damageMultiplier = damageMultiplier;
        this.fireDamageMultiplier = fireDamageMultiplier;
    }

    public String getId() {
        return this.id;
    }

    public String getItemId() {
        return this.id + "_chariot";
    }

    public float getMassFactor() {
        return this.massFactor;
    }

    public float getMaxHealth() {
        return BASE_MAX_HEALTH * this.toughness;
    }

    public float applyDamageAmount(DamageSource source, float amount) {
        float scaled = amount * this.damageMultiplier;
        if (this.fireDamageMultiplier < 1.0F && source.is(DamageTypeTags.IS_FIRE)) {
            scaled *= this.fireDamageMultiplier;
        }
        return scaled;
    }

    public int networkId() {
        return this.ordinal();
    }

    public ResourceLocation getEntityTexture() {
        return ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "textures/entity/" + this.id + "_chariot.png");
    }

    public static ChariotType fromNetworkId(int id) {
        ChariotType[] values = values();
        if (id < 0 || id >= values.length) {
            return BRONZE;
        }
        return values[id];
    }

    public static Optional<ChariotType> fromId(String id) {
        if (id == null || id.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(BY_ID.get(id.toLowerCase(Locale.ROOT)));
    }
}
