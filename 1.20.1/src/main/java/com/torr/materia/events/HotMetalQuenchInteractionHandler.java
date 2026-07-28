package com.torr.materia.events;

import com.torr.materia.blockentity.WaterPotBlockEntity;
import com.torr.materia.materia;
import com.torr.materia.utils.HotMetalStackingUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Quenches heated metal when the player right-clicks a water source, filled cauldron, or water pot.
 */
@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public final class HotMetalQuenchInteractionHandler {

    private HotMetalQuenchInteractionHandler() {}

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        Level level = event.getLevel();
        if (level.isClientSide()) {
            return;
        }

        Player player = event.getEntity();
        ItemStack held = event.getItemStack();
        if (!HotMetalStackingUtils.isHeatedHeatableMetal(held)) {
            return;
        }

        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        if (!isQuenchTarget(level, pos, state)) {
            return;
        }

        HotMetalStackingUtils.quenchHeatableIfHeated(held).ifPresent(cooled -> {
            player.setItemInHand(event.getHand(), cooled);
            level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 0.6F,
                    1.9F + level.random.nextFloat() * 0.12F);
            consumeQuenchWater(level, pos, state);
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        });
    }

    private static boolean isQuenchTarget(Level level, BlockPos pos, BlockState state) {
        if ((state.is(Blocks.WATER) || state.getFluidState().getType() == Fluids.WATER)
                && state.getFluidState().isSource()) {
            return true;
        }
        if (state.is(Blocks.WATER_CAULDRON)
                && state.getValue(LayeredCauldronBlock.LEVEL) > 0) {
            return true;
        }
        return level.getBlockEntity(pos) instanceof WaterPotBlockEntity pot && pot.hasWater();
    }

    private static void consumeQuenchWater(Level level, BlockPos pos, BlockState state) {
        if (!state.is(Blocks.WATER_CAULDRON)) {
            return;
        }
        int current = state.getValue(LayeredCauldronBlock.LEVEL);
        if (current <= 0) {
            return;
        }
        int newLevel = current - 1;
        BlockState newState = newLevel == 0 ? Blocks.CAULDRON.defaultBlockState()
                : state.setValue(LayeredCauldronBlock.LEVEL, newLevel);
        level.setBlock(pos, newState, 3);
    }
}
