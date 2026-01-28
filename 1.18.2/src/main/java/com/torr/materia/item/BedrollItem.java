package com.torr.materia.item;

import com.torr.materia.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public class BedrollItem extends Item {
    public BedrollItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        
        if (player == null) {
            return InteractionResult.FAIL;
        }

        // Only allow use at night or during thunderstorms
        if (!level.isClientSide && !canSleep(level)) {
            player.displayClientMessage(new TranslatableComponent("block.minecraft.bed.no_sleep"), true);
            return InteractionResult.FAIL;
        }

        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        Direction facing = player.getDirection();

        // Check if we have space for 2 blocks
        BlockPos headPos = pos;
        BlockPos footPos = pos.relative(facing.getOpposite());
        BlockPlaceContext place = new BlockPlaceContext(context);
        if (!level.getBlockState(headPos).canBeReplaced(place) || !level.getBlockState(footPos).canBeReplaced(place)) {
            return InteractionResult.FAIL;
        }

        // Place temporary bedroll blocks
        BlockState headState = ModBlocks.BEDROLL.get().defaultBlockState()
                .setValue(net.minecraft.world.level.block.BedBlock.PART, BedPart.HEAD)
                .setValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING, facing)
                .setValue(net.minecraft.world.level.block.BedBlock.OCCUPIED, false);
        BlockState footState = ModBlocks.BEDROLL.get().defaultBlockState()
                .setValue(net.minecraft.world.level.block.BedBlock.PART, BedPart.FOOT)
                .setValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING, facing)
                .setValue(net.minecraft.world.level.block.BedBlock.OCCUPIED, false);

        level.setBlock(headPos, headState, 11);
        level.setBlock(footPos, footState, 11);

        // Consume the bedroll item
        if (!player.isCreative()) {
            context.getItemInHand().shrink(1);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private boolean canSleep(Level level) {
        // Allow sleeping at night or during thunderstorms
        return !level.dimensionType().hasFixedTime() && (level.getDayTime() >= 12541 || level.isThundering());
    }
}


