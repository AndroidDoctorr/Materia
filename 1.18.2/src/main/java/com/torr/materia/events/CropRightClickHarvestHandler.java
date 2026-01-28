package com.torr.materia.events;

import com.torr.materia.ThreeSistersBlock;
import com.torr.materia.ThreeSistersCornUpperBlock;
import com.torr.materia.materia;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

/**
 * Right-click harvest (with replant) for vanilla crops and the Three Sisters crop.
 * Behaves similarly to RightClickHarvest: harvests when mature, resets age, and leaves farmland intact.
 */
@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class CropRightClickHarvestHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        Level level = event.getWorld();
        if (level.isClientSide()) {
            return;
        }

        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        // If we clicked the tall corn upper block, redirect to the base Three Sisters block below
        if (state.getBlock() instanceof ThreeSistersCornUpperBlock) {
            pos = pos.below();
            state = level.getBlockState(pos);
        }

        boolean harvested = false;

        if (state.getBlock() instanceof ThreeSistersBlock threeSisters) {
            harvested = harvestThreeSisters((ServerLevel) level, pos, state);
        } else if (state.getBlock() instanceof CropBlock crop) {
            harvested = harvestStandardCrop(crop, (ServerLevel) level, pos, state);
        }

        if (harvested) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }

    private static boolean harvestStandardCrop(CropBlock crop, ServerLevel level, BlockPos pos, BlockState state) {
        if (!crop.isMaxAge(state)) {
            return false;
        }

        List<ItemStack> drops = Block.getDrops(state, level, pos, level.getBlockEntity(pos));
        drops.forEach(stack -> Block.popResource(level, pos, stack));

        // Replant at age 1 to keep the crop in-place
        BlockState replanted = crop.getStateForAge(1);
        level.setBlock(pos, replanted, Block.UPDATE_ALL);

        level.levelEvent(2001, pos, Block.getId(state)); // Block break particles/sound
        return true;
    }

    private static boolean harvestThreeSisters(ServerLevel level, BlockPos pos, BlockState state) {
        int cornAge = state.getValue(ThreeSistersBlock.CORN_AGE);
        int beansAge = state.getValue(ThreeSistersBlock.BEANS_AGE);
        int squashAge = state.getValue(ThreeSistersBlock.SQUASH_AGE);

        boolean didHarvest = false;
        List<ItemStack> drops = new ArrayList<>();

        // Harvest each crop independently if mature (age 7)
        if (cornAge >= 7) {
            drops.addAll(getDropsForSingleCrop(level, pos, state, ThreeSistersBlock.CORN_AGE));
            state = state.setValue(ThreeSistersBlock.CORN_AGE, 1);
            didHarvest = true;
        }

        if (beansAge >= 7) {
            drops.addAll(getDropsForSingleCrop(level, pos, state, ThreeSistersBlock.BEANS_AGE));
            state = state.setValue(ThreeSistersBlock.BEANS_AGE, 1);
            didHarvest = true;
        }

        if (squashAge >= 7) {
            drops.addAll(getDropsForSingleCrop(level, pos, state, ThreeSistersBlock.SQUASH_AGE));
            state = state.setValue(ThreeSistersBlock.SQUASH_AGE, 1);
            didHarvest = true;
        }

        if (!didHarvest) {
            return false;
        }

        drops.forEach(stack -> Block.popResource(level, pos, stack));
        level.setBlock(pos, state, Block.UPDATE_ALL);
        level.levelEvent(2001, pos, Block.getId(state)); // particles/sound
        return true;
    }

    /**
     * Build a synthetic blockstate where only the targeted crop age remains,
     * letting the loot table generate the correct drops for that crop alone.
     */
    private static List<ItemStack> getDropsForSingleCrop(ServerLevel level, BlockPos pos, BlockState state, IntegerProperty targetAgeProp) {
        BlockState harvestState = state
                .setValue(ThreeSistersBlock.CORN_AGE, targetAgeProp == ThreeSistersBlock.CORN_AGE ? state.getValue(targetAgeProp) : 0)
                .setValue(ThreeSistersBlock.BEANS_AGE, targetAgeProp == ThreeSistersBlock.BEANS_AGE ? state.getValue(targetAgeProp) : 0)
                .setValue(ThreeSistersBlock.SQUASH_AGE, targetAgeProp == ThreeSistersBlock.SQUASH_AGE ? state.getValue(targetAgeProp) : 0);

        return Block.getDrops(harvestState, level, pos, level.getBlockEntity(pos));
    }
}

