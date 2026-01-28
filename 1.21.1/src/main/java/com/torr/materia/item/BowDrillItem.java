package com.torr.materia.item;

import com.torr.materia.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * A custom flint-and-steelâ€“style item that can start fires and is also usable
 * as a container ingredient in crafting recipes, losing durability each time
 * while surviving the craft (until it eventually breaks).
 */
public class BowDrillItem extends FlintAndSteelItem {

    public BowDrillItem(Properties properties) {
        super(properties);
    }

    /* --------------------------------------------------------------------- */
    /* Crafting container behaviour                                          */
    /* --------------------------------------------------------------------- */

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

    /* --------------------------------------------------------------------- */
    /* World interaction â€“ start fires like flint and steel                  */
    /* --------------------------------------------------------------------- */

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Direction face = context.getClickedFace();
        BlockPos targetPos = clickedPos.relative(face);

        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        // Cannot place fire here? fall back to vanilla handling.
        if (!player.mayUseItemAt(targetPos, face, stack)) {
            return InteractionResult.FAIL;
        }

        BlockState targetState = level.getBlockState(targetPos);

        // Try lighting an un-lit campfire first.
        if (CampfireBlock.canLight(targetState)) {
            level.playSound(player, targetPos, ModSounds.BOW_DRILL.get(), SoundSource.BLOCKS, 1.0F,
                    level.random.nextFloat() * 0.4F + 0.8F);
            level.setBlock(targetPos, targetState.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
            damageItem(stack, player, context);
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        // Otherwise try to place regular fire.
        if (BaseFireBlock.canBePlacedAt(level, targetPos, context.getHorizontalDirection())) {
            level.playSound(player, targetPos, ModSounds.BOW_DRILL.get(), SoundSource.BLOCKS, 1.0F,
                    level.random.nextFloat() * 0.4F + 0.8F);
            BlockState fireState = net.minecraft.world.level.block.Blocks.FIRE.defaultBlockState();
            level.setBlock(targetPos, fireState, 11);
            damageItem(stack, player, context);
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.FAIL;
    }

    private void damageItem(ItemStack stack, Player player, UseOnContext context) {
        stack.hurtAndBreak(1, player, context.getHand() == net.minecraft.world.InteractionHand.MAIN_HAND
                ? net.minecraft.world.entity.EquipmentSlot.MAINHAND
                : net.minecraft.world.entity.EquipmentSlot.OFFHAND);
    }
} 