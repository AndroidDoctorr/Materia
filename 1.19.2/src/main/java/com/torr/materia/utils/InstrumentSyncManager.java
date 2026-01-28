package com.torr.materia.utils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Manages synchronization of instrument playback to ensure all instruments
 * can start playing on aligned timing boundaries for better musical coordination.
 * 
 * This is a true metronome system that:
 * - Only allows instruments to start on sync points (every 10 ticks)
 * - Prevents multiple instances of the same instrument per player
 * - Queues instrument starts until the next sync point
 * 
 * Based on 120 BPM timing:
 * - 1 beat = 10 ticks (0.5 seconds)
 * - Sync points every 10 ticks allow instruments to align easily
 */
public class InstrumentSyncManager {
    
    /**
     * Sync interval in ticks. At 20 TPS, 10 ticks = 0.5 seconds = 1 beat at 120 BPM
     */
    public static final int SYNC_INTERVAL_TICKS = 10;
    
    /**
     * Tracks which players have queued instruments to start at the next sync point
     * Key: Player UUID, Value: Sync tick when they should start
     */
    private static final Map<UUID, Long> queuedStarts = new HashMap<>();
    
    /**
     * Tracks which players are currently playing instruments
     * Key: Player UUID, Value: Sync tick when they started playing
     */
    private static final Map<UUID, Long> activePlayers = new HashMap<>();
    
    /**
     * Gets the current global sync tick for the given level.
     * This represents how many sync intervals have passed since world creation.
     * 
     * @param level The level to get sync tick for
     * @return The current sync tick (increments every SYNC_INTERVAL_TICKS)
     */
    public static long getCurrentSyncTick(Level level) {
        return level.getGameTime() / SYNC_INTERVAL_TICKS;
    }
    
    /**
     * Gets the number of ticks until the next sync point.
     * 
     * @param level The level to check
     * @return Ticks remaining until next sync point (0 to SYNC_INTERVAL_TICKS-1)
     */
    public static int getTicksUntilNextSync(Level level) {
        return (int) (SYNC_INTERVAL_TICKS - (level.getGameTime() % SYNC_INTERVAL_TICKS));
    }
    
    /**
     * Checks if the current tick is a sync point.
     * 
     * @param level The level to check
     * @return true if this is a sync tick
     */
    public static boolean isOnSyncTick(Level level) {
        return level.getGameTime() % SYNC_INTERVAL_TICKS == 0;
    }
    
    /**
     * Gets the next sync point in absolute game time.
     * 
     * @param level The level to check
     * @return The absolute game time of the next sync point
     */
    public static long getNextSyncTime(Level level) {
        long currentTime = level.getGameTime();
        long remainder = currentTime % SYNC_INTERVAL_TICKS;
        if (remainder == 0) {
            return currentTime; // Already on sync point
        }
        return currentTime + (SYNC_INTERVAL_TICKS - remainder);
    }
    
    /**
     * Calculates when an instrument should play its next sample based on sync timing.
     * This ensures all instruments stay aligned to the global beat.
     * 
     * @param level The level
     * @param startSyncTick The sync tick when the instrument started playing
     * @param sampleIntervalTicks How often this instrument should play (40, 80, or 160 ticks)
     * @return The absolute game time when the next sample should play, or -1 if not time yet
     */
    public static long getNextSampleTime(Level level, long startSyncTick, int sampleIntervalTicks) {
        long currentSyncTick = getCurrentSyncTick(level);
        long syncTicksElapsed = currentSyncTick - startSyncTick;
        
        // Convert sample interval from game ticks to sync ticks
        int sampleIntervalSyncTicks = sampleIntervalTicks / SYNC_INTERVAL_TICKS;
        
        // Check if it's time for the next sample
        if (syncTicksElapsed > 0 && syncTicksElapsed % sampleIntervalSyncTicks == 0) {
            // It's time for a sample, return the current sync time
            return getCurrentSyncTick(level) * SYNC_INTERVAL_TICKS;
        }
        
        return -1; // Not time for a sample yet
    }
    
    /**
     * Checks if an instrument should play a sample on this tick, based on synchronized timing.
     * 
     * @param level The level
     * @param startSyncTick When the instrument started (in sync ticks)
     * @param sampleIntervalTicks Sample interval (40, 80, or 160 ticks)
     * @return true if the instrument should play a sample this tick
     */
    public static boolean shouldPlaySample(Level level, long startSyncTick, int sampleIntervalTicks) {
        // Only play on sync ticks
        if (!isOnSyncTick(level)) {
            return false;
        }
        
        long currentSyncTick = getCurrentSyncTick(level);
        long syncTicksElapsed = currentSyncTick - startSyncTick;
        
        // Convert sample interval from game ticks to sync ticks
        int sampleIntervalSyncTicks = sampleIntervalTicks / SYNC_INTERVAL_TICKS;
        
        // Play if we've elapsed a multiple of the sample interval
        return syncTicksElapsed > 0 && syncTicksElapsed % sampleIntervalSyncTicks == 0;
    }
    
    // ========== METRONOME SYSTEM METHODS ==========
    
    /**
     * Attempts to queue an instrument start for a player. If they're already playing
     * or already queued, this does nothing. Otherwise, queues them to start at the next sync point.
     * 
     * @param player The player trying to start an instrument
     * @param level The level
     * @return true if successfully queued, false if already playing/queued
     */
    public static boolean queueInstrumentStart(Player player, Level level) {
        UUID playerId = player.getUUID();
        
        // Perform periodic cleanup to prevent memory leaks
        if (getCurrentSyncTick(level) % 200 == 0) { // Every 100 seconds
            cleanupStaleEntries(level);
        }
        
        // Don't queue if already playing or queued
        if (activePlayers.containsKey(playerId) || queuedStarts.containsKey(playerId)) {
            return false;
        }
        
        // Queue to start at the next sync point
        long nextSyncTick = getCurrentSyncTick(level) + 1;
        queuedStarts.put(playerId, nextSyncTick);
        return true;
    }
    
    /**
     * Checks if a player should start playing their instrument on this tick.
     * This should be called every tick for all players.
     * 
     * @param player The player to check
     * @param level The level
     * @return true if the player should start their instrument now
     */
    public static boolean shouldStartInstrument(Player player, Level level) {
        UUID playerId = player.getUUID();
        
        // Only start on sync ticks
        if (!isOnSyncTick(level)) {
            return false;
        }
        
        // Check if this player is queued to start now
        Long queuedSyncTick = queuedStarts.get(playerId);
        if (queuedSyncTick != null && queuedSyncTick <= getCurrentSyncTick(level)) {
            // Move from queued to active
            queuedStarts.remove(playerId);
            long currentSyncTick = getCurrentSyncTick(level);
            activePlayers.put(playerId, currentSyncTick);
            
            // Safety check: if the queued time is way in the past, something went wrong
            if (currentSyncTick - queuedSyncTick > 10) {
                // This suggests the queue got stuck - let's clean up and continue
                cleanupStaleEntries(level);
            }
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Gets the start sync tick for a player, if they're currently playing.
     * 
     * @param player The player
     * @return The sync tick when they started, or null if not playing
     */
    public static Long getPlayerStartSyncTick(Player player) {
        return activePlayers.get(player.getUUID());
    }
    
    /**
     * Marks a player as no longer playing an instrument.
     * 
     * @param player The player who stopped playing
     */
    public static void stopInstrument(Player player) {
        UUID playerId = player.getUUID();
        activePlayers.remove(playerId);
        queuedStarts.remove(playerId); // Also remove any queued start
    }
    
    /**
     * Checks if a player is currently playing an instrument.
     * 
     * @param player The player to check
     * @return true if the player is currently playing
     */
    public static boolean isPlayerPlaying(Player player) {
        return activePlayers.containsKey(player.getUUID());
    }
    
    /**
     * Checks if a player has an instrument start queued.
     * 
     * @param player The player to check
     * @return true if the player has a start queued
     */
    public static boolean isPlayerQueued(Player player) {
        return queuedStarts.containsKey(player.getUUID());
    }
    
    // ========== CLEANUP AND MAINTENANCE METHODS ==========
    
    /**
     * Cleans up stale entries from the maps. Should be called periodically
     * to prevent memory leaks from disconnected players.
     * 
     * @param level The current level to get current time
     */
    public static void cleanupStaleEntries(Level level) {
        long currentSyncTick = getCurrentSyncTick(level);
        
        // Remove very old queued starts (older than 100 sync ticks = 50 seconds)
        Iterator<Map.Entry<UUID, Long>> queueIterator = queuedStarts.entrySet().iterator();
        while (queueIterator.hasNext()) {
            Map.Entry<UUID, Long> entry = queueIterator.next();
            if (currentSyncTick - entry.getValue() > 100) {
                queueIterator.remove();
            }
        }
        
        // Remove very old active players (older than 1000 sync ticks = ~8 minutes)
        Iterator<Map.Entry<UUID, Long>> activeIterator = activePlayers.entrySet().iterator();
        while (activeIterator.hasNext()) {
            Map.Entry<UUID, Long> entry = activeIterator.next();
            if (currentSyncTick - entry.getValue() > 1000) {
                activeIterator.remove();
            }
        }
    }
    
    /**
     * Clears all state. Use carefully - this will stop all instruments immediately.
     * Useful for debugging or world reloads.
     */
    public static void clearAllState() {
        queuedStarts.clear();
        activePlayers.clear();
    }
    
    /**
     * Gets debug information about the current state of the sync manager.
     * 
     * @param level The current level
     * @return A string with debug info
     */
    public static String getDebugInfo(Level level) {
        long currentSyncTick = getCurrentSyncTick(level);
        return String.format("SyncManager Debug - Current sync tick: %d, Queued: %d, Active: %d", 
                currentSyncTick, queuedStarts.size(), activePlayers.size());
    }
}