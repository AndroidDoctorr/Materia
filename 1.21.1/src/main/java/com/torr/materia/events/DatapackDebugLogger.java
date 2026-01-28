package com.torr.materia.events;

import com.torr.materia.ModBlocks;
import com.torr.materia.materia;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

/**
 * Temporary diagnostics to confirm datapack-driven recipes/tags are actually loaded at runtime.
 *
 * Remove once recipes + vanilla overrides + log tags are confirmed working in-game.
 */
@Mod.EventBusSubscriber(modid = materia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class DatapackDebugLogger {
    private DatapackDebugLogger() {}

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        // Toggle via JVM property: -Dmateria.datapackDebug=true
        if (!Boolean.getBoolean("materia.datapackDebug")) return;

        var server = event.getServer();

        // 0) What datapacks are even selected?
        try {
            var repo = server.getPackRepository();
            var selected = repo.getSelectedPacks().stream().map(Pack::getId).toList();
            var available = repo.getAvailablePacks().stream().map(Pack::getId).toList();
            materia.LOGGER.info("[DatapackDebug] Selected packs: {}", selected);
            materia.LOGGER.info("[DatapackDebug] Available packs: {}", available);
        } catch (Throwable t) {
            materia.LOGGER.warn("[DatapackDebug] Failed to query PackRepository", t);
        }

        // 1) Are our crafting recipes present?
        List<RecipeHolder<CraftingRecipe>> crafting = server.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING);
        long materiaCrafting = crafting.stream().filter(h -> h.id().getNamespace().equals(materia.MOD_ID)).count();
        long minecraftCrafting = crafting.stream().filter(h -> h.id().getNamespace().equals("minecraft")).count();
        materia.LOGGER.info("[DatapackDebug] Crafting recipes loaded: total={}, materia={}, minecraft={}", crafting.size(), materiaCrafting, minecraftCrafting);

        // 1b) Are our custom recipe types loaded at all?
        try {
            long kiln = server.getRecipeManager().getAllRecipesFor(com.torr.materia.ModRecipes.KILN_TYPE.get()).size();
            long advKiln = server.getRecipeManager().getAllRecipesFor(com.torr.materia.ModRecipes.ADVANCED_KILN_TYPE.get()).size();
            long oven = server.getRecipeManager().getAllRecipesFor(com.torr.materia.ModRecipes.OVEN_TYPE.get()).size();
            long firePit = server.getRecipeManager().getAllRecipesFor(com.torr.materia.ModRecipes.FIRE_PIT_TYPE.get()).size();
            materia.LOGGER.info("[DatapackDebug] Custom recipe counts: kiln={}, advanced_kiln={}, oven={}, fire_pit={}", kiln, advKiln, oven, firePit);
        } catch (Throwable t) {
            materia.LOGGER.warn("[DatapackDebug] Failed to query custom recipe types", t);
        }

        // 1c) Do server resources even contain our JSONs? (bypasses RecipeManager parsing)
        try {
            ResourceManager rm = server.getResourceManager();
            int materiaRecipeJsons = rm.listResources("recipes", p -> p.getPath().endsWith(".json") && p.getNamespace().equals(materia.MOD_ID)).size();
            int minecraftRecipeJsons = rm.listResources("recipes", p -> p.getPath().endsWith(".json") && p.getNamespace().equals("minecraft")).size();
            int minecraftLogsTag = rm.listResources("tags/blocks", p -> p.getNamespace().equals("minecraft") && p.getPath().equals("tags/blocks/logs.json")).size();
            int minecraftLeavesTag = rm.listResources("tags/blocks", p -> p.getNamespace().equals("minecraft") && p.getPath().equals("tags/blocks/leaves.json")).size();
            materia.LOGGER.info("[DatapackDebug] ResourceManager JSON visibility: materia:recipes/*.json={}, minecraft:recipes/*.json={}", materiaRecipeJsons, minecraftRecipeJsons);
            materia.LOGGER.info("[DatapackDebug] ResourceManager tag visibility: minecraft:logs.json={}, minecraft:leaves.json={}", minecraftLogsTag, minecraftLeavesTag);
        } catch (Throwable t) {
            materia.LOGGER.warn("[DatapackDebug] Failed to query ResourceManager JSON visibility", t);
        }

        // 2) Are vanilla overrides active? (example: minecraft:oak_planks)
        ResourceLocation oakPlanksId = ResourceLocation.fromNamespaceAndPath("minecraft", "oak_planks");
        crafting.stream()
                .filter(h -> h.id().equals(oakPlanksId))
                .findFirst()
                .ifPresentOrElse(holder -> {
                    var ingredients = holder.value().getIngredients();
                    materia.LOGGER.info("[DatapackDebug] Recipe {} ingredient count={}", oakPlanksId, ingredients.size());
                    for (int i = 0; i < ingredients.size(); i++) {
                        var ing = ingredients.get(i);
                        materia.LOGGER.info("[DatapackDebug]  - ingredient[{}] items={}", i, ing.getItems().length);
                        // Only print a small sample to avoid huge logs.
                        for (int j = 0; j < Math.min(ing.getItems().length, 5); j++) {
                            var stack = ing.getItems()[j];
                            materia.LOGGER.info("[DatapackDebug]    * {}", BuiltInRegistries.ITEM.getKey(stack.getItem()));
                        }
                    }
                }, () -> materia.LOGGER.warn("[DatapackDebug] Recipe {} not found in RecipeType.CRAFTING list", oakPlanksId));

        // 3) Are our logs recognized as logs? (leaf decay depends on this tag)
        BlockState oliveLog = ModBlocks.OLIVE_TREE_LOG.get().defaultBlockState();
        materia.LOGGER.info("[DatapackDebug] olive_tree_log in minecraft:logs? {}", oliveLog.is(BlockTags.LOGS));
        materia.LOGGER.info("[DatapackDebug] olive_tree_log in minecraft:logs_that_burn? {}", oliveLog.is(BlockTags.LOGS_THAT_BURN));
    }
}

