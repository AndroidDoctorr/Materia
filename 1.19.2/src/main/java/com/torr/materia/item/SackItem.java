package com.torr.materia.item;

import com.torr.materia.menu.SackMenu;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class SackItem extends Item {
    private static final int SLOT_COUNT = 4;

    public SackItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            MenuConstructor constructor = (containerId, inv, p) -> new SackMenu(containerId, inv, hand);
            MenuProvider provider = new SimpleMenuProvider(constructor, Component.translatable("container.materia.sack"));
            NetworkHooks.openScreen(serverPlayer, provider, buf -> buf.writeEnum(hand));
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            int count = 0;
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stored = handler.getStackInSlot(i);
                if (!stored.isEmpty()) {
                    count += stored.getCount();
                }
            }
            if (count > 0) {
                tooltip.add(Component.literal("§6Contains: " + count + " items"));
            }
        });
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new SackCapabilityProvider();
    }

    private static class SackCapabilityProvider implements ICapabilityProvider, net.minecraftforge.common.util.INBTSerializable<CompoundTag> {
        private final SackItemHandler handler = new SackItemHandler(SLOT_COUNT);
        private final LazyOptional<IItemHandler> optional = LazyOptional.of(() -> handler);

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
            return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? optional.cast() : LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            return handler.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            handler.deserializeNBT(nbt);
        }
    }

    public static class SackItemHandler extends ItemStackHandler {
        public SackItemHandler(int size) {
            super(size);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return !(stack.getItem() instanceof SackItem);
        }
    }
}

