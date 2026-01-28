package com.torr.materia.events;

import com.torr.materia.ModItems;
import com.torr.materia.materia;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Adds plant fiber drops when stripping logs with an axe.
 */
@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class PlantFiberStripHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        Level level = event.getLevel();
        if (level.isClientSide()) {
            return;
        }

        ItemStack heldItem = event.getItemStack();
        if (!(heldItem.getItem() instanceof AxeItem)) {
            return;
        }

        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        
        // Check if it's a log that can be stripped (has axis property)
        if (!(state.getBlock() instanceof RotatedPillarBlock) || 
            !state.hasProperty(RotatedPillarBlock.AXIS)) {
            return;
        }

        // Check if the block has a stripped variant (will be stripped)
        UseOnContext ctx = new UseOnContext(event.getEntity(), event.getHand(), event.getHitVec());
        BlockState strippedState = state.getToolModifiedState(ctx, net.minecraftforge.common.ToolActions.AXE_STRIP, false);
        
        if (strippedState != null && strippedState != state) {
            // Log will be stripped, add plant fiber drops
            if (level instanceof ServerLevel serverLevel) {
                Block.popResource(serverLevel, pos, new ItemStack(ModItems.PLANT_FIBER.get(), 2));
            }
        }
    }
}


