package com.torr.materia.item;

import com.torr.materia.materia;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Wood variants for cart hull textures, crafting, and physical stats. */
public enum CartWoodType {
    OAK("oak", "Oak", "smooth_oak_planks", 1.0F, 1.0F, 1.0F, 1.0F),
    SPRUCE("spruce", "Spruce", "smooth_spruce_planks", 0.9F, 0.85F, 1.1F, 1.0F),
    BIRCH("birch", "Birch", "smooth_birch_planks", 0.85F, 0.75F, 1.25F, 1.0F),
    JUNGLE("jungle", "Jungle", "smooth_jungle_planks", 1.15F, 1.15F, 0.9F, 1.0F),
    ACACIA("acacia", "Acacia", "smooth_acacia_planks", 1.1F, 1.2F, 0.85F, 1.0F),
    DARK_OAK("dark_oak", "Dark Oak", "smooth_dark_oak_planks", 1.2F, 1.25F, 0.8F, 1.0F),
    CHERRY("cherry", "Cherry", "smooth_cherry_planks", 0.9F, 0.8F, 1.15F, 1.0F),
    MANGROVE("mangrove", "Mangrove", "smooth_mangrove_planks", 1.15F, 1.1F, 0.95F, 1.0F),
    CRIMSON("crimson", "Crimson", "smooth_crimson_planks", 0.95F, 0.85F, 1.0F, 0.35F),
    WARPED("warped", "Warped", "smooth_warped_planks", 0.95F, 0.85F, 1.0F, 0.35F),
    RUBBER_WOOD("rubber_wood", "Rubber Wood", "smooth_rubber_wood_planks", 1.05F, 0.95F, 1.05F, 1.0F),
    FIG("fig", "Fig", "smooth_fig_plank", 0.82F, 0.7F, 1.3F, 1.0F),
    CEDAR("cedar", "Cedar", "smooth_cedar_plank", 0.85F, 0.85F, 1.1F, 1.0F),
    EUCALYPTUS("eucalyptus", "Eucalyptus", "smooth_eucalyptus_plank", 0.88F, 0.8F, 1.15F, 1.0F),
    PALE_OAK("pale_oak", "Pale Oak", null, "minecraft:pale_oak_planks", 0.92F, 0.9F, 1.05F, 1.0F);

    public static final float BASE_MAX_HEALTH = 60.0F;
    public static final float REPAIR_AMOUNT = 10.0F;

    private static final Map<String, CartWoodType> BY_ID = Arrays.stream(values())
            .collect(Collectors.toMap(CartWoodType::getId, Function.identity()));

    private final String id;
    private final String displayName;
    @Nullable
    private final String smoothPlankItemId;
    @Nullable
    private final String repairItemId;
    private final float massFactor;
    private final float toughness;
    private final float damageMultiplier;
    private final float fireDamageMultiplier;

    CartWoodType(String id, String displayName, @Nullable String smoothPlankItemId,
            float massFactor, float toughness, float damageMultiplier, float fireDamageMultiplier) {
        this(id, displayName, smoothPlankItemId, smoothPlankItemId, massFactor, toughness, damageMultiplier,
                fireDamageMultiplier);
    }

    CartWoodType(String id, String displayName, @Nullable String smoothPlankItemId, @Nullable String repairItemId,
            float massFactor, float toughness, float damageMultiplier, float fireDamageMultiplier) {
        this.id = id;
        this.displayName = displayName;
        this.smoothPlankItemId = smoothPlankItemId;
        this.repairItemId = repairItemId;
        this.massFactor = massFactor;
        this.toughness = toughness;
        this.damageMultiplier = damageMultiplier;
        this.fireDamageMultiplier = fireDamageMultiplier;
    }

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getCartId() {
        return this.id + "_cart";
    }

    public String getCartBaseId() {
        return this.id + "_cart_base";
    }

    @Nullable
    public String getSmoothPlankItemId() {
        return this.smoothPlankItemId;
    }

    public float getMaxHealth() {
        return BASE_MAX_HEALTH * this.toughness;
    }

    public float getMassFactor() {
        return this.massFactor;
    }

    public float applyDamageAmount(DamageSource source, float amount) {
        float scaled = amount * this.damageMultiplier;
        if (this.fireDamageMultiplier < 1.0F && source.is(DamageTypeTags.IS_FIRE)) {
            scaled *= this.fireDamageMultiplier;
        }
        return scaled;
    }

    public boolean isRepairItem(ItemStack stack) {
        if (stack.isEmpty() || this.repairItemId == null) {
            return false;
        }
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        ResourceLocation repairId = parseItemId(this.repairItemId);
        return itemId != null && repairId != null && itemId.equals(repairId);
    }

    public ResourceLocation getEntityTexture() {
        return ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "textures/entity/" + this.id + "_cart.png");
    }

    public int networkId() {
        return this.ordinal();
    }

    public static CartWoodType fromNetworkId(int id) {
        if (id < 0 || id >= values().length) {
            return OAK;
        }
        return values()[id];
    }

    public static Optional<CartWoodType> fromId(String id) {
        if (id == null || id.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(BY_ID.get(id.toLowerCase(Locale.ROOT)));
    }

    public static CartWoodType fromItem(Item item) {
        if (item instanceof CartItem cartItem) {
            return cartItem.getWoodType();
        }
        return OAK;
    }

    @Nullable
    private static ResourceLocation parseItemId(String itemId) {
        if (itemId.contains(":")) {
            return ResourceLocation.tryParse(itemId);
        }
        return ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, itemId);
    }
}
