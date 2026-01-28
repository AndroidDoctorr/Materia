package com.torr.materia.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import java.util.function.Supplier;

public class InstrumentItem extends Item {
    public enum SampleLength {
        SHORT,
        MEDIUM,
        LONG;
    }

    private final Supplier<SoundEvent> soundSupplier;
    private final SampleLength sampleLength;
    
    // NBT keys for storing instrument state
    private static final String QUEUED_START_TIME_KEY = "materia:queued_start_time";
    private static final String ACTUAL_START_TIME_KEY = "materia:actual_start_time";
    private static final String LAST_USE_TIME_KEY = "materia:last_use_time";
    
    // Sync interval in ticks (10 ticks = 0.5 seconds = 1 beat at 120 BPM)
    private static final int SYNC_INTERVAL_TICKS = 10;
    
    public InstrumentItem(Properties properties, Supplier<SoundEvent> soundSupplier, SampleLength sampleLength) {
        super(properties);
        this.soundSupplier = soundSupplier;
        this.sampleLength = sampleLength;
    }
    
    public InstrumentItem(Properties properties, Supplier<SoundEvent> soundSupplier) {
        super(properties);
        this.soundSupplier = soundSupplier;
        this.sampleLength = SampleLength.SHORT;
    }
    
    // ========== HELPER METHODS ==========
    
    /**
     * Gets the current sync tick for the given level
     */
    private static long getCurrentSyncTick(Level level) {
        return level.getGameTime() / SYNC_INTERVAL_TICKS;
    }
    
    /**
     * Checks if the current tick is a sync point
     */
    private static boolean isOnSyncTick(Level level) {
        return level.getGameTime() % SYNC_INTERVAL_TICKS == 0;
    }
    
    /**
     * Gets the next sync time in absolute game ticks
     */
    private static long getNextSyncTime(Level level) {
        long currentTime = level.getGameTime();
        long remainder = currentTime % SYNC_INTERVAL_TICKS;
        if (remainder == 0) {
            return currentTime; // Already on sync point
        }
        return currentTime + (SYNC_INTERVAL_TICKS - remainder);
    }
    
    /**
     * Checks if this instrument should start playing now based on its queued time
     */
    private boolean shouldStartNow(ItemStack stack, Level level) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(QUEUED_START_TIME_KEY)) {
            return false;
        }
        
        long queuedTime = tag.getLong(QUEUED_START_TIME_KEY);
        return level.getGameTime() >= queuedTime && isOnSyncTick(level);
    }
    
    /**
     * Checks if this instrument should play a sample now
     */
    private boolean shouldPlaySample(ItemStack stack, Level level) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(ACTUAL_START_TIME_KEY)) {
            return false;
        }
        
        if (!isOnSyncTick(level)) {
            return false;
        }
        
        long startTime = tag.getLong(ACTUAL_START_TIME_KEY);
        long elapsed = level.getGameTime() - startTime;
        
        int sampleTicks = sampleLength == SampleLength.SHORT ? 40 : sampleLength == SampleLength.MEDIUM ? 80 : 160;
        
        return elapsed > 0 && elapsed % sampleTicks == 0;
    }
    
    /**
     * Checks if the instrument is currently active (has started playing)
     */
    private boolean isActive(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(ACTUAL_START_TIME_KEY);
    }
    
    /**
     * Checks if the instrument is queued to start
     */
    private boolean isQueued(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(QUEUED_START_TIME_KEY) && !tag.contains(ACTUAL_START_TIME_KEY);
    }
    
    /**
     * Clears all instrument state from the item
     */
    private void clearState(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            tag.remove(QUEUED_START_TIME_KEY);
            tag.remove(ACTUAL_START_TIME_KEY);
            tag.remove(LAST_USE_TIME_KEY);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        
        if (!level.isClientSide) {
            // Check if already active or queued - ignore rapid clicks
            if (isActive(stack) || isQueued(stack)) {
                return InteractionResultHolder.pass(stack);
            }
            
            // Queue to start at the next sync point
            long nextSyncTime = getNextSyncTime(level);
            CompoundTag tag = stack.getOrCreateTag();
            tag.putLong(QUEUED_START_TIME_KEY, nextSyncTime);
            tag.putLong(LAST_USE_TIME_KEY, level.getGameTime());
        }
        
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseTicks) {
        if (!(livingEntity instanceof Player player)) {
            return;
        }
        
        if (level.isClientSide) {
            return; // Only handle on server side
        }
        
        // Safety check: clear old state if item has been used too long
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(LAST_USE_TIME_KEY)) {
            long lastUse = tag.getLong(LAST_USE_TIME_KEY);
            if (level.getGameTime() - lastUse > 1200) { // 1 minute
                clearState(stack);
                player.stopUsingItem();
                return;
            }
        }
        
        // Check if this instrument should start playing now
        if (shouldStartNow(stack, level)) {
            // Move from queued to active
            CompoundTag nbtTag = stack.getOrCreateTag();
            nbtTag.remove(QUEUED_START_TIME_KEY);
            nbtTag.putLong(ACTUAL_START_TIME_KEY, level.getGameTime());
            
            // Play the initial sound
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    soundSupplier.get(), SoundSource.PLAYERS, 0.8F, 1.0F);
            return;
        }
        
        // Check if we should play a sample
        if (shouldPlaySample(stack, level)) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    soundSupplier.get(), SoundSource.PLAYERS, 0.8F, 1.0F);
        }
    }
    
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeLeft) {
        if (livingEntity instanceof Player player && !level.isClientSide) {
            // Clean up when player stops using the instrument
            clearState(stack);
        }
    }
}
