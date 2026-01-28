package com.torr.materia.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class materiaCommonConfig {
    public static final ForgeConfigSpec SPEC;

    // Cannonballs
    public static final ForgeConfigSpec.BooleanValue CANNONBALL_BLOCK_EFFECTS_ENABLED;
    public static final ForgeConfigSpec.BooleanValue CANNONBALL_EFFECT_CRACK_VARIANTS;
    public static final ForgeConfigSpec.BooleanValue CANNONBALL_EFFECT_STONE_TO_COBBLE;
    public static final ForgeConfigSpec.BooleanValue CANNONBALL_EFFECT_COBBLE_TO_PEBBLES;
    public static final ForgeConfigSpec.BooleanValue CANNONBALL_EFFECT_SAND_BREAKS;
    public static final ForgeConfigSpec.BooleanValue IRON_CANNONBALL_EXTRA_STONE_SMASHING;

    // Vines
    public static final ForgeConfigSpec.BooleanValue VINES_PREVENT_GRAPE_WISTERIA_OVERLAP;
    public static final ForgeConfigSpec.BooleanValue VINES_SELF_HEAL_SUPPORT_OVERLAP;
    public static final ForgeConfigSpec.IntValue VINE_PLANT_SPREAD_CHANCE_PERCENT;
    public static final ForgeConfigSpec.IntValue VINE_SUPPORT_CHAIN_SPREAD_CHANCE_PERCENT;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        b.push("cannonballs");
        CANNONBALL_BLOCK_EFFECTS_ENABLED = b
                .comment("If false, cannonballs will not transform/break blocks (entity damage still applies).")
                .define("blockEffectsEnabled", true);
        CANNONBALL_EFFECT_CRACK_VARIANTS = b
                .comment("If true, cannonballs will try to swap blocks to a cracked_ variant when available.")
                .define("crackVariants", true);
        CANNONBALL_EFFECT_STONE_TO_COBBLE = b
                .comment("If true, cannonballs can turn stone (and some stones for iron cannonballs) into cobblestone.")
                .define("stoneToCobblestone", true);
        CANNONBALL_EFFECT_COBBLE_TO_PEBBLES = b
                .comment("If true, cannonballs can turn cobblestone into pebbles.")
                .define("cobblestoneToPebbles", true);
        CANNONBALL_EFFECT_SAND_BREAKS = b
                .comment("If true, cannonballs can break sand/red sand.")
                .define("breakSand", true);
        IRON_CANNONBALL_EXTRA_STONE_SMASHING = b
                .comment("If true, iron cannonballs can smash a wider set of base stones (granite/etc.) into cobblestone.")
                .define("ironExtraStoneSmashing", true);
        b.pop();

        b.push("vines");
        VINES_PREVENT_GRAPE_WISTERIA_OVERLAP = b
                .comment("If true, vine types will avoid occupying the same support block state.")
                .define("preventVineOverlapOnSupports", true);
        VINES_SELF_HEAL_SUPPORT_OVERLAP = b
                .comment("If true, supports (trellis/joists/posts) will attempt to self-heal if overlapping vine states somehow occur.")
                .define("selfHealSupportOverlap", true);
        VINE_PLANT_SPREAD_CHANCE_PERCENT = b
                .comment("Chance (0-100) per random tick for a mature grape/wisteria crop block to attempt spreading onto nearby supports.")
                .defineInRange("plantSpreadChancePercent", 10, 0, 100);
        VINE_SUPPORT_CHAIN_SPREAD_CHANCE_PERCENT = b
                .comment("Chance (0-100) per random tick for a support block (trellis/joists/posts) to attempt spreading vine state to nearby supports.")
                .defineInRange("supportChainSpreadChancePercent", 2, 0, 100);
        b.pop();

        SPEC = b.build();
    }

    private materiaCommonConfig() {}
}

