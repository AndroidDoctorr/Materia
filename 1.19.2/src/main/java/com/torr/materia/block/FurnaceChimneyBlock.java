package com.torr.materia.block;

import com.torr.materia.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;

/**
 * Chimney block that can be placed on top of furnaces to enable stone smelting.
 * Only works with furnace blocks, not kilns.
 */
public class FurnaceChimneyBlock extends Block {

    public FurnaceChimneyBlock(Properties properties) {
        super(properties);
    }

    /**
     * Check if this chimney is placed on top of a furnace
     */
    public static boolean isOnFurnace(Level level, BlockPos chimneyPos) {
        BlockPos furnacePos = chimneyPos.below();
        return level.getBlockState(furnacePos).is(ModBlocks.FURNACE_KILN.get());
    }

    /**
     * Get the furnace block below this chimney, or null if not on a furnace
     */
    public static BlockPos getFurnaceBelow(Level level, BlockPos chimneyPos) {
        BlockPos furnacePos = chimneyPos.below();
        if (level.getBlockState(furnacePos).is(ModBlocks.FURNACE_KILN.get())) {
            return furnacePos;
        }
        return null;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (isOnFurnace(level, pos)) {
            // Add smoke particles when placed on an active furnace
            BlockPos furnacePos = pos.below();
            BlockState furnaceState = level.getBlockState(furnacePos);
            
            if (furnaceState.hasProperty(net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT) && 
                furnaceState.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT)) {
                
                double d0 = (double)pos.getX() + 0.5D;
                double d1 = (double)pos.getY() + 0.7D;
                double d2 = (double)pos.getZ() + 0.5D;
                
                if (random.nextDouble() < 0.1D) {
                    level.addParticle(ParticleTypes.LARGE_SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }
} 