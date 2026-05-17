package com.torr.materia.events;

import com.torr.materia.ModItems;
import com.torr.materia.materia;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Materia replaces several diamond-tier crafts with upgrades from iron (steel-tier) bases.
 * Shaped crafting JSON always yields a pristine result; mirror {@link com.torr.materia.recipe.FlintKnifeRecipe}
 * by transferring the same fractional wear from the ingredient base onto the crafted item.
 */
@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public final class DiamondTierUpgradeCraftHandler {

    private DiamondTierUpgradeCraftHandler() {}

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack crafted = event.getCrafting();
        Item baseItem = baseIngredientFor(crafted.getItem());
        if (baseItem == null) {
            return;
        }
        Container inv = event.getInventory();
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack slot = inv.getItem(i);
            if (slot.isEmpty()) {
                continue;
            }
            if (slot.getItem() == baseItem) {
                transferProportionalWear(slot, crafted);
                return;
            }
        }
    }

    /**
     * @return the single steel-/iron-tier item whose wear applies to {@code crafted}, or null when this handler should not apply
     */
    private static Item baseIngredientFor(Item crafted) {
        if (crafted == Items.DIAMOND_PICKAXE) {
            return Items.IRON_PICKAXE;
        }
        if (crafted == Items.DIAMOND_AXE) {
            return Items.IRON_AXE;
        }
        if (crafted == Items.DIAMOND_SHOVEL) {
            return Items.IRON_SHOVEL;
        }
        if (crafted == Items.DIAMOND_HOE) {
            return Items.IRON_HOE;
        }
        if (crafted == Items.DIAMOND_SWORD) {
            return Items.IRON_SWORD;
        }
        if (crafted == Items.DIAMOND_HELMET) {
            return Items.IRON_HELMET;
        }
        if (crafted == Items.DIAMOND_CHESTPLATE) {
            return Items.IRON_CHESTPLATE;
        }
        if (crafted == Items.DIAMOND_LEGGINGS) {
            return Items.IRON_LEGGINGS;
        }
        if (crafted == Items.DIAMOND_BOOTS) {
            return Items.IRON_BOOTS;
        }
        if (crafted == ModItems.DIAMOND_HAMMER.get()) {
            return ModItems.STEEL_HAMMER.get();
        }
        if (crafted == ModItems.DIAMOND_SPEAR.get()) {
            return ModItems.STEEL_SPEAR.get();
        }
        if (crafted == ModItems.DIAMOND_SHOVEL_HEAD.get()) {
            return ModItems.STEEL_SHOVEL_HEAD.get();
        }
        return null;
    }

    private static void transferProportionalWear(ItemStack from, ItemStack to) {
        int fromMax = from.getMaxDamage();
        int toMax = to.getMaxDamage();
        if (fromMax <= 0 || toMax <= 0) {
            return;
        }
        int fromDamage = from.getDamageValue();
        float wearRatio = (float) fromDamage / (float) fromMax;
        to.setDamageValue((int) (wearRatio * toMax));
    }
}
