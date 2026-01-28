package com.torr.materia.events;

import com.torr.materia.ModBlocks;
import com.torr.materia.materia;
import com.torr.materia.blockentity.KilnBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;

/**
 * Converts vanilla furnaces to our furnace-kiln on first interaction
 * and opens the dual-input UI with advanced kiln behavior.
 */
@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class FurnaceOverrideHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCanceled()) return;
        if (event.getHand() != InteractionHand.MAIN_HAND) return;
        if (event.getPlayer() != null && event.getPlayer().isShiftKeyDown()) return;

        Level level = event.getWorld();
        if (level.isClientSide()) return;

        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        if (!state.is(net.minecraft.world.level.block.Blocks.FURNACE)) return;

        // Capture vanilla furnace contents, if any
        ItemStack input = ItemStack.EMPTY;
        ItemStack fuel = ItemStack.EMPTY;
        ItemStack output = ItemStack.EMPTY;
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof AbstractFurnaceBlockEntity furnaceBE) {
            input = furnaceBE.getItem(0).copy();
            fuel = furnaceBE.getItem(1).copy();
            output = furnaceBE.getItem(2).copy();
        }

        // Determine facing/lit
        Direction facing = state.getValue(FurnaceBlock.FACING);
        boolean lit = state.hasProperty(AbstractFurnaceBlock.LIT) && state.getValue(AbstractFurnaceBlock.LIT);

        // Replace with our furnace_kiln block
        BlockState newState = ModBlocks.FURNACE_KILN.get().defaultBlockState()
                .setValue(com.torr.materia.KilnBlock.FACING, facing)
                .setValue(com.torr.materia.KilnBlock.LIT, lit);
        level.setBlockAndUpdate(pos, newState);

        // Transfer items into new kiln entity
        BlockEntity newBe = level.getBlockEntity(pos);
        if (newBe instanceof KilnBlockEntity kilnBE) {
            if (!input.isEmpty()) kilnBE.getItemHandler().setStackInSlot(0, input);
            if (!fuel.isEmpty()) kilnBE.getItemHandler().setStackInSlot(1, fuel);
            if (!output.isEmpty()) kilnBE.getItemHandler().setStackInSlot(2, output);

            // Open our UI immediately
            NetworkHooks.openGui((ServerPlayer) event.getPlayer(), kilnBE, pos);
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }
}


