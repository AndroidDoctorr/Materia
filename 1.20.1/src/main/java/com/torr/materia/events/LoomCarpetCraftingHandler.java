package com.torr.materia.events;

import com.torr.materia.materia;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class LoomCarpetCraftingHandler {
    private static final String STRING_SUFFIX = "_string";
    private static final int STRINGS_REQUIRED = 8;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCanceled()) {
            return;
        }
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        Level level = event.getLevel();
        if (level.isClientSide()) {
            return;
        }

        BlockState clicked = level.getBlockState(event.getPos());
        if (clicked.getBlock() != Blocks.LOOM) {
            return;
        }

        ItemStack held = event.getItemStack();
        if (held.isEmpty()) {
            return;
        }

        ResourceLocation heldId = ForgeRegistries.ITEMS.getKey(held.getItem());
        if (heldId == null || !materia.MOD_ID.equals(heldId.getNamespace())) {
            return;
        }

        String path = heldId.getPath();
        if (!path.endsWith(STRING_SUFFIX)) {
            return;
        }

        String color = path.substring(0, path.length() - STRING_SUFFIX.length());
        Item outputItem = resolveCarpetItem(color);
        if (outputItem == null) {
            return;
        }

        Player player = event.getEntity();
        if (player == null) {
            return;
        }

        if (!player.getAbilities().instabuild) {
            int available = countInInventory(player, held.getItem());
            if (available < STRINGS_REQUIRED) {
                return;
            }
            removeFromInventory(player, held.getItem(), STRINGS_REQUIRED);
        }

        ItemStack output = new ItemStack(outputItem, 1);
        if (!player.addItem(output)) {
            player.drop(output, false);
        }

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    private static Item resolveCarpetItem(String color) {
        Item vanilla = ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft", color + "_carpet"));
        if (vanilla != null && vanilla != Items.AIR) {
            return vanilla;
        }

        Item mod = ForgeRegistries.ITEMS.getValue(new ResourceLocation(materia.MOD_ID, color + "_carpet"));
        if (mod != null && mod != Items.AIR) {
            return mod;
        }

        return null;
    }

    private static int countInInventory(Player player, Item item) {
        int total = 0;
        var inv = player.getInventory();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && stack.getItem() == item) {
                total += stack.getCount();
            }
        }
        return total;
    }

    private static void removeFromInventory(Player player, Item item, int count) {
        int remaining = count;
        var inv = player.getInventory();

        for (int i = 0; i < inv.getContainerSize(); i++) {
            if (remaining <= 0) {
                break;
            }

            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty() || stack.getItem() != item) {
                continue;
            }

            int take = Math.min(remaining, stack.getCount());
            stack.shrink(take);
            remaining -= take;
            inv.setItem(i, stack.isEmpty() ? ItemStack.EMPTY : stack);
        }

        inv.setChanged();
    }
}

