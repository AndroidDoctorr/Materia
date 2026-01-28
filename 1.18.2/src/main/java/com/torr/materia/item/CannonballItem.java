package com.torr.materia.item;

import com.torr.materia.block.CannonballPileBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class CannonballItem extends Item {
    private final Supplier<Block> pileBlock;

    public CannonballItem(Supplier<Block> pileBlock, Properties properties) {
        super(properties);
        this.pileBlock = pileBlock;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        // "Sneak-place" piles. Use isShiftKeyDown for 1.18 consistency.
        if (player == null || !player.isShiftKeyDown()) return InteractionResult.PASS;

        Level level = context.getLevel();
        Block pile = pileBlock.get();

        BlockPos clicked = context.getClickedPos();
        BlockState clickedState = level.getBlockState(clicked);

        // Add to an existing pile if we clicked one directly.
        if (clickedState.getBlock() == pile) {
            return tryAddToPile(context, clicked, clickedState);
        }

        // Place on top of the clicked block (ground placement).
        if (context.getClickedFace() != Direction.UP) return InteractionResult.PASS;

        BlockPos placePos = clicked.relative(context.getClickedFace());
        BlockPlaceContext placeContext = new BlockPlaceContext(context);
        BlockState placeState = level.getBlockState(placePos);

        if (!placeState.canBeReplaced(placeContext)) return InteractionResult.PASS;

        BlockState newState = pile.defaultBlockState().setValue(CannonballPileBlock.COUNT, 1);
        if (!level.isClientSide) {
            level.setBlock(placePos, newState, 3);
            SoundType sound = newState.getSoundType(level, placePos, player);
            level.playSound(null, placePos, sound.getPlaceSound(), SoundSource.BLOCKS, 0.8F, 1.0F);
            if (!player.isCreative()) {
                context.getItemInHand().shrink(1);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private InteractionResult tryAddToPile(UseOnContext context, BlockPos pos, BlockState state) {
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.FAIL;

        int count = state.getValue(CannonballPileBlock.COUNT);
        if (count >= 14) return InteractionResult.PASS;

        Level level = context.getLevel();
        if (!level.isClientSide) {
            BlockState next = state.setValue(CannonballPileBlock.COUNT, count + 1);
            level.setBlock(pos, next, 3);

            SoundType sound = next.getSoundType(level, pos, player);
            level.playSound(null, pos, sound.getPlaceSound(), SoundSource.BLOCKS, 0.8F, 1.0F);

            if (!player.isCreative()) {
                context.getItemInHand().shrink(1);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}

