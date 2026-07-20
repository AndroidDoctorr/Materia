package com.torr.materia.item;

import com.torr.materia.blockentity.MosaicBlockEntity;
import com.torr.materia.mosaic.MosaicItemData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MosaicBlockItem extends BlockItem {

    public MosaicBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player,
                                                 ItemStack stack, BlockState state) {
        boolean result = super.updateCustomBlockEntityTag(pos, level, player, stack, state);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof MosaicBlockEntity mosaic) {
            MosaicItemData.applyBlockEntityTag(mosaic, stack);
        }
        return result;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("item.materia.mosaic_block.description")
                .withStyle(ChatFormatting.GRAY));
        if (MosaicItemData.isPainted(stack)) {
            tooltip.add(Component.translatable("item.materia.mosaic_block.painted")
                    .withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
