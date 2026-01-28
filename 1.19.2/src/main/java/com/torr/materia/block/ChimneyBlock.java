package com.torr.materia.block;

import com.torr.materia.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;

/**
 * Chimney block that can be placed on top of kilns to enable high-temperature smelting.
 * Detects if it's placed on a kiln and provides enhanced smelting capabilities.
 */
public class ChimneyBlock extends Block {

    public ChimneyBlock(Properties properties) {
        super(properties);
    }

    /**
     * Check if this chimney is placed on top of a kiln
     */
    public static boolean isOnKiln(Level level, BlockPos chimneyPos) {
        BlockPos kilnPos = chimneyPos.below();
        return level.getBlockState(kilnPos).is(ModBlocks.KILN.get());
    }

    /**
     * Get the kiln block below this chimney, or null if not on a kiln
     */
    public static BlockPos getKilnBelow(Level level, BlockPos chimneyPos) {
        BlockPos kilnPos = chimneyPos.below();
        if (level.getBlockState(kilnPos).is(ModBlocks.KILN.get())) {
            return kilnPos;
        }
        return null;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // Only spawn smoke on client side when the kiln below is lit
        BlockPos kilnPos = pos.below();
        BlockState kilnState = level.getBlockState(kilnPos);
        if (kilnState.is(ModBlocks.KILN.get()) && kilnState.getValue(com.torr.materia.KilnBlock.LIT)) {
            // Roughly one third of the campfire particle density
            if (random.nextInt(3) == 0) {
                double x = pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
                double y = pos.getY() + 1.0D; // Top of the chimney
                double z = pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
                double motionY = 0.07D; // Gentle upward drift
                level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 0.0D, motionY, 0.0D);
            }
        }
    }
} 