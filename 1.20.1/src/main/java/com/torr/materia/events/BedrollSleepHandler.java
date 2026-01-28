package com.torr.materia.events;

import com.torr.materia.ModBlocks;
import com.torr.materia.materia;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class BedrollSleepHandler {

    // Track bedroll head positions for players currently sleeping on a bedroll
    private static final Map<UUID, BlockPos> playerToBedrollHead = new ConcurrentHashMap<>();
    // Delay the time advance to create a longer fade/risks (in ticks)
    private static final int BEDROLL_SLEEP_DELAY_TICKS = 100; // 5 seconds at 20 TPS
    private static final Map<UUID, Integer> playerSleepDelay = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void onPlayerSleepInBed(PlayerSleepInBedEvent event) {
        Player player = event.getEntity();
        Level level = player.level();
        BlockPos pos = event.getPos();

        if (level.isClientSide()) {
            return; // Only handle on server side
        }

        // If player is sleeping on our bedroll, start a delayed time advance
        if (level.getBlockState(pos).is(ModBlocks.BEDROLL.get())) {
            playerToBedrollHead.put(player.getUUID(), pos.immutable());
            playerSleepDelay.put(player.getUUID(), BEDROLL_SLEEP_DELAY_TICKS);
        }
    }

    // Called from placement to ensure we always know where the bedroll is
    public static void recordBedrollPlacement(UUID playerId, BlockPos headPos) {
        playerToBedrollHead.put(playerId, headPos.immutable());
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if (player == null || player.level().isClientSide()) return;
        UUID uuid = player.getUUID();
        Integer ticks = playerSleepDelay.get(uuid);
        if (ticks == null) return;

        ticks -= 1;
        if (ticks <= 0) {
            Level level = player.level();
            if (level instanceof ServerLevel serverLevel) {
                long currentTime = serverLevel.getDayTime();
                long timeToMorning = 24000 - (currentTime % 24000);
                if (timeToMorning > 12000) timeToMorning = 24000 - timeToMorning;
                serverLevel.setDayTime(currentTime + timeToMorning);
                // Proactively remove the bedroll now to avoid lingering blocks
                BlockPos headPos = playerToBedrollHead.remove(uuid);
                if (headPos != null) {
                    removeBedrollAt(serverLevel, headPos);
                }
            }
            playerSleepDelay.remove(uuid);
        } else {
            playerSleepDelay.put(uuid, ticks);
        }
    }

    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
        Player player = event.getEntity();
        Level level = player.level();

        if (level.isClientSide()) {
            return; // Only handle on server side
        }

        // Prefer stored bedroll head position
        BlockPos headPos = playerToBedrollHead.remove(player.getUUID());
        playerSleepDelay.remove(player.getUUID());
        if (headPos != null) {
            removeBedrollAt(level, headPos);
            // Give bedroll back to player
            player.getInventory().add(new net.minecraft.world.item.ItemStack(com.torr.materia.ModItems.BEDROLL.get()));
            // Also try removing adjacent in case saved part changed
            tryRemoveAdjacent(level, headPos);
            return;
        }

        // Fallback: try player sleeping position
        BlockPos sleepingPos = player.getSleepingPos().orElse(null);
        if (sleepingPos != null && level.getBlockState(sleepingPos).is(ModBlocks.BEDROLL.get())) {
            removeBedrollAt(level, sleepingPos);
            tryRemoveAdjacent(level, sleepingPos);
            // Give bedroll back to player
            player.getInventory().add(new net.minecraft.world.item.ItemStack(com.torr.materia.ModItems.BEDROLL.get()));
        }
    }

    private static void removeBedrollAt(Level level, BlockPos headOrFoot) {
        BlockState state = level.getBlockState(headOrFoot);
        if (!state.is(ModBlocks.BEDROLL.get())) {
            return;
        }

        BedPart part = state.getValue(BedBlock.PART);
        if (part == BedPart.HEAD) {
            BlockPos footPos = headOrFoot.relative(state.getValue(BedBlock.FACING).getOpposite());
            level.setBlock(headOrFoot, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 11);
            level.setBlock(footPos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 11);
        } else {
            BlockPos headPos = headOrFoot.relative(state.getValue(BedBlock.FACING));
            level.setBlock(headOrFoot, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 11);
            level.setBlock(headPos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 11);
        }
    }

    private static void tryRemoveAdjacent(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!state.is(ModBlocks.BEDROLL.get())) return;
        BlockPos other = state.getValue(BedBlock.PART) == BedPart.HEAD
                ? pos.relative(state.getValue(BedBlock.FACING).getOpposite())
                : pos.relative(state.getValue(BedBlock.FACING));
        level.setBlock(other, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 11);
    }
}