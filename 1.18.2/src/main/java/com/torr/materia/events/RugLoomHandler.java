package com.torr.materia.events;

import com.torr.materia.ModItems;
import com.torr.materia.materia;
import com.torr.materia.rug.RugWeaving;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.IdentityHashMap;
import java.util.Set;

/**
 * Extends the vanilla loom for Materia rug weaving: rug base in the template slot, one field dye, rug pattern item.
 * Inputs are only consumed when the player takes the finished rug from the output slot.
 */
@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public final class RugLoomHandler {
    private static final Set<LoomMenu> HOOKED = java.util.Collections.newSetFromMap(new IdentityHashMap<>());

    private RugLoomHandler() {
    }

    @SubscribeEvent
    public static void onContainerOpen(PlayerContainerEvent.Open event) {
        AbstractContainerMenu menu = event.getContainer();
        if (!(menu instanceof LoomMenu loom) || HOOKED.contains(loom)) {
            return;
        }
        hookLoomSlots(loom);
        loom.addSlotListener(new RugPreviewSyncListener(loom));
        HOOKED.add(loom);
    }

    @SubscribeEvent
    public static void onContainerClose(PlayerContainerEvent.Close event) {
        if (event.getContainer() instanceof LoomMenu loom) {
            HOOKED.remove(loom);
        }
    }

    private static void hookLoomSlots(LoomMenu loom) {
        replaceSlot(loom, loom.getDyeSlot().index, new RugLoomDyeSlot(loom));
        replaceSlot(loom, loom.getResultSlot().index, new RugLoomResultSlot(loom));
    }

    private static void replaceSlot(LoomMenu loom, int index, Slot replacement) {
        loom.slots.set(index, replacement);
    }

    private static boolean isRugMode(LoomMenu loom) {
        return loom.getBannerSlot().getItem().is(ModItems.RUG_BASE.get());
    }

    private static final class RugPreviewSyncListener implements ContainerListener {
        private final LoomMenu loom;

        private RugPreviewSyncListener(LoomMenu loom) {
            this.loom = loom;
        }

        @Override
        public void slotChanged(AbstractContainerMenu container, int slotIndex, ItemStack stack) {
            if (slotIndex <= 2 && isRugMode(loom)) {
                loom.broadcastChanges();
            }
        }

        @Override
        public void dataChanged(AbstractContainerMenu container, int dataId, int dataValue) {
        }
    }

    private static final class RugLoomDyeSlot extends Slot {
        RugLoomDyeSlot(LoomMenu loom) {
            super(loom.getDyeSlot().container, loom.getDyeSlot().getContainerSlot(), loom.getDyeSlot().x, loom.getDyeSlot().y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.isEmpty() || RugWeaving.isLoomDye(stack);
        }
    }

    private static final class RugLoomResultSlot extends Slot {
        private final LoomMenu loom;

        RugLoomResultSlot(LoomMenu loom) {
            super(loom.getResultSlot().container, loom.getResultSlot().getContainerSlot(), loom.getResultSlot().x, loom.getResultSlot().y);
            this.loom = loom;
        }

        @Override
        public ItemStack getItem() {
            if (isRugMode(loom)) {
                return RugWeaving.resolve(
                        loom.getBannerSlot().getItem(),
                        loom.getDyeSlot().getItem(),
                        loom.getPatternSlot().getItem()
                ).orElse(ItemStack.EMPTY);
            }
            return super.getItem();
        }

        @Override
        public ItemStack remove(int amount) {
            if (isRugMode(loom)) {
                ItemStack preview = getItem();
                if (preview.isEmpty()) {
                    return ItemStack.EMPTY;
                }
                ItemStack taken = preview.copy();
                taken.setCount(Math.min(amount, taken.getCount()));
                loom.getBannerSlot().remove(1);
                loom.getDyeSlot().remove(1);
                return taken;
            }
            return super.remove(amount);
        }

        @Override
        public void set(ItemStack stack) {
            if (!isRugMode(loom)) {
                super.set(stack);
            }
        }

        @Override
        public boolean mayPickup(Player player) {
            return !getItem().isEmpty();
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}
