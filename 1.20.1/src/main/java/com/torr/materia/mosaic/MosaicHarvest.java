package com.torr.materia.mosaic;

import com.torr.materia.block.MosaicBlock;
import com.torr.materia.blockentity.MosaicBlockEntity;
import com.torr.materia.item.BronzeChiselItem;
import com.torr.materia.item.BronzeHammerItem;
import com.torr.materia.item.IronChiselItem;
import com.torr.materia.item.IronHammerItem;
import com.torr.materia.item.SteelHammerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class MosaicHarvest {
    private MosaicHarvest() {
    }

    public static boolean isMosaic(BlockState state) {
        return state.getBlock() instanceof MosaicBlock;
    }

    public static boolean preservesPaint(ItemStack tool, Player player) {
        if (tool.is(ItemTags.PICKAXES)) {
            return true;
        }
        return player != null && isChiselWithHammer(tool, player);
    }

    public static boolean isChiselWithHammer(ItemStack tool, Player player) {
        if (!(tool.getItem() instanceof BronzeChiselItem || tool.getItem() instanceof IronChiselItem)) {
            return false;
        }
        ItemStack offhand = player.getOffhandItem();
        return offhand.getItem() instanceof BronzeHammerItem
                || offhand.getItem() instanceof IronHammerItem
                || offhand.getItem() instanceof SteelHammerItem;
    }

    public static ItemStack createPaintedDrop(Level level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        MosaicBlockEntity mosaic = blockEntity instanceof MosaicBlockEntity entity ? entity : null;
        return MosaicItemData.createDrop(state, mosaic, true);
    }
}
