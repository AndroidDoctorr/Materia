package com.torr.materia.recipe;

import com.torr.materia.ModItems;
import com.torr.materia.ModBlocks;
import com.torr.materia.ModRecipes;
import com.torr.materia.materia;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

/**
 * Hewing recipe: 1 log + 1 basic axe (hand axe or stone axe) -> 4 matching rough planks.
 * Axe survives with 1 durability damage (breaks when reaches max damage).
 * Single JSON entry (type "materia:hewing") covers all wood types.
 * Note: Saws are excluded - they make posts from logs instead.
 */
public class HewingRecipe extends CustomRecipe {

    private static final TagKey<Item> ALL_SAWS_TAG = ItemTags.create(new ResourceLocation(materia.MOD_ID, "all_saws"));

    public HewingRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(@NotNull CraftingContainer inv, @NotNull Level level) {
        int logs = 0;
        int axes = 0;
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(ItemTags.LOGS)) {
                logs++;
            } else if (stack.getItem() instanceof net.minecraft.world.item.AxeItem) {
                // Exclude saws - they should make posts, not rough planks
                if (stack.is(ALL_SAWS_TAG)) {
                    return false;
                }
                axes++;
            } else {
                return false; // unexpected ingredient
            }
            if (logs > 1 || axes > 1) return false;
        }
        return logs == 1 && axes == 1;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingContainer inv) {
        ItemStack logStack = ItemStack.EMPTY;
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && stack.is(ItemTags.LOGS)) {
                logStack = stack;
                break;
            }
        }
        if (logStack.isEmpty()) return ItemStack.EMPTY;

        // Determine rough plank output based on log type
        ItemStack result = getPlankForLog(logStack);
        return result.copy();
    }

    private ItemStack getPlankForLog(ItemStack log) {
        // Vanilla logs
        if (log.is(Items.SPRUCE_LOG)) return new ItemStack(ModItems.ROUGH_SPRUCE_PLANK.get(), 4);
        if (log.is(Items.BIRCH_LOG)) return new ItemStack(ModItems.ROUGH_BIRCH_PLANK.get(), 4);
        if (log.is(Items.JUNGLE_LOG)) return new ItemStack(ModItems.ROUGH_JUNGLE_PLANK.get(), 4);
        if (log.is(Items.ACACIA_LOG)) return new ItemStack(ModItems.ROUGH_ACACIA_PLANK.get(), 4);
        if (log.is(Items.CRIMSON_STEM) || log.is(Items.CRIMSON_HYPHAE)) return new ItemStack(ModItems.ROUGH_CRIMSON_PLANK.get(), 4);
        if (log.is(Items.WARPED_STEM) || log.is(Items.WARPED_HYPHAE)) return new ItemStack(ModItems.ROUGH_WARPED_PLANK.get(), 4);
        if (log.is(Items.DARK_OAK_LOG)) return new ItemStack(ModItems.ROUGH_DARK_OAK_PLANK.get(), 4);
        
        // Custom mod logs
        if (log.is(ModBlocks.RUBBER_TREE_LOG.get().asItem())) return new ItemStack(ModItems.ROUGH_RUBBER_WOOD_PLANK.get(), 4);
        if (log.is(ModBlocks.OLIVE_TREE_LOG.get().asItem())) return new ItemStack(ModItems.ROUGH_OAK_PLANK.get(), 4);
        if (log.is(ModBlocks.SAPPED_SPRUCE_LOG.get().asItem())) return new ItemStack(ModItems.ROUGH_SPRUCE_PLANK.get(), 4);
        if (log.is(ModBlocks.TAPPED_RUBBER_TREE_LOG.get().asItem())) return new ItemStack(ModItems.ROUGH_RUBBER_WOOD_PLANK.get(), 4);
        
        // default to oak for any other log (includes OAK_LOG)
        return new ItemStack(ModItems.ROUGH_OAK_PLANK.get(), 4);
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof net.minecraft.world.item.AxeItem) {
                ItemStack copy = stack.copy();
                copy.setDamageValue(copy.getDamageValue() + 1);
                if (copy.getDamageValue() < copy.getMaxDamage()) {
                    remaining.set(i, copy);
                } // else leave EMPTY -> breaks
            }
        }
        return remaining;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.HEWING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }
}