package com.torr.materia.events;

import com.torr.materia.ModBlocks;
import com.torr.materia.materia;
import com.torr.materia.block.CannonBlock;
import com.torr.materia.block.GunpowderTrailBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class GunpowderTrailPlacementHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCanceled()) {
            return;
        }
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        Level level = event.getWorld();
        if (level.isClientSide()) {
            return;
        }

        // Don't place gunpowder trails when interacting with cannons (gunpowder is used to charge them)
        BlockState clickedState = level.getBlockState(event.getPos());
        if (clickedState.getBlock() instanceof CannonBlock) {
            return;
        }

        ItemStack stack = event.getItemStack();
        if (stack.isEmpty() || stack.getItem() != Items.GUNPOWDER) {
            return;
        }

        Player player = event.getPlayer();
        if (player == null) {
            return;
        }

        if (event.getFace() == null || event.getFace() == net.minecraft.core.Direction.DOWN) {
            return;
        }

        BlockPos placePos = event.getPos().relative(event.getFace());

        // Phase 1: place into air/replaceable blocks, but not into fluids
        BlockState existing = level.getBlockState(placePos);
        if (!existing.getMaterial().isReplaceable() || !level.getFluidState(placePos).isEmpty()) {
            return;
        }

        Block block = ModBlocks.GUNPOWDER_TRAIL.get();
        if (!(block instanceof GunpowderTrailBlock trail)) {
            return;
        }

        BlockState placed = trail.updateConnections(level, placePos, block.defaultBlockState());
        if (!placed.canSurvive(level, placePos)) {
            return;
        }

        level.setBlock(placePos, placed, Block.UPDATE_ALL);

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }
}

