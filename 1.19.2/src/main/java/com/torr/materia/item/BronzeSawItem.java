package com.torr.materia.item;

import com.torr.materia.ModSounds;
import com.torr.materia.ModToolTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Bronze saw used for precision cutting of wood and other materials.
 * Faster than bronze axe for cutting logs, optimized for woodcutting.
 * It survives crafting recipes while losing durability.
 */
public class BronzeSawItem extends AxeItem {

    public BronzeSawItem(Properties properties) {
        // Bronze tier, but faster attack speed for woodcutting (-2.8F vs -3.1F for axe)
        // Lower attack damage (4.0F vs 6.0F) since it's specialized for cutting, not combat
        super(ModToolTiers.BRONZE, 4.0F, -2.8F, properties);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        // Extra speed bonus for logs and wood blocks
        if (state.is(BlockTags.LOGS) || state.is(BlockTags.PLANKS)) {
            return super.getDestroySpeed(stack, state) * 1.25F; // 25% faster than normal bronze axe
        }
        return super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        // Play saw sound when cutting wood blocks
        if (!level.isClientSide && (state.is(BlockTags.LOGS) || state.is(BlockTags.PLANKS))) {
            level.playSound(null, pos, ModSounds.SAW_CRAFT.get(), SoundSource.BLOCKS, 0.6F, 1.0F);
        }
        return super.mineBlock(stack, level, state, pos, miningEntity);
    }

    /* Container behaviour for crafting */
    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setDamageValue(copy.getDamageValue() + 1);
        return copy.getDamageValue() >= copy.getMaxDamage() ? ItemStack.EMPTY : copy;
    }
} 