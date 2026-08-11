package com.torr.materia.item;

import com.torr.materia.ModSounds;
import com.torr.materia.ModToolTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Iron saw used for precision cutting of wood and other materials.
 * Same role as the bronze saw, but with wrought-iron durability.
 */
public class IronSawItem extends AxeItem {

    public IronSawItem(Properties properties) {
        super(ModToolTiers.WROUGHT_IRON, 4.0F, -2.8F, properties);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.is(BlockTags.LOGS) || state.is(BlockTags.PLANKS)) {
            return super.getDestroySpeed(stack, state) * 1.25F;
        }
        return super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (!level.isClientSide && (state.is(BlockTags.LOGS) || state.is(BlockTags.PLANKS))) {
            level.playSound(null, pos, ModSounds.SAW_CRAFT.get(), SoundSource.BLOCKS, 0.6F, 1.0F);
        }
        return super.mineBlock(stack, level, state, pos, miningEntity);
    }

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
