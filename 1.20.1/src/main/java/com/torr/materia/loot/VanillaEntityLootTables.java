package com.torr.materia.loot;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.common.loot.LootTableIdCondition;

/**
 * Helpers for gating global loot modifiers to vanilla entity loot tables only.
 */
public final class VanillaEntityLootTables {

    private VanillaEntityLootTables() {}

    /**
     * True when the loot roll uses a vanilla table for the given entity path
     * (e.g. {@code cow} matches {@code minecraft:entities/cow} and
     * {@code sheep} matches {@code minecraft:entities/sheep/white}).
     */
    public static boolean matchesVanillaEntityLoot(LootContext context, String entityPath) {
        ResourceLocation id = context.getQueriedLootTableId();
        if (id == null || LootTableIdCondition.UNKNOWN_LOOT_TABLE.equals(id)) {
            return false;
        }
        if (!"minecraft".equals(id.getNamespace())) {
            return false;
        }
        String prefix = "entities/" + entityPath;
        return id.getPath().equals(prefix) || id.getPath().startsWith(prefix + "/");
    }
}
