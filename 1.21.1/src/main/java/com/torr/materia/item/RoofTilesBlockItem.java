package com.torr.materia.item;

import com.torr.materia.block.RoofTilesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RoofTilesBlockItem extends BlockItem {
    public static final String STAGE_TAG = "RoofTileStage";
    public static final String THATCH_TAG = "RoofTileThatch";

    private final int defaultPlacementStage;

    public RoofTilesBlockItem(Block block, Properties properties, int defaultPlacementStage) {
        super(block, properties);
        this.defaultPlacementStage = defaultPlacementStage;
    }

    public static int placementStage(ItemStack stack) {
        if (stack.getItem() instanceof ThatchRoofBlockItem) {
            return 8;
        }
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (tag.contains(STAGE_TAG)) {
            return tag.getInt(STAGE_TAG);
        }
        return 8;
    }

    public static boolean placementThatch(ItemStack stack) {
        if (stack.getItem() instanceof ThatchRoofBlockItem) {
            return true;
        }
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return tag.getBoolean(THATCH_TAG);
    }

    public static ItemStack withStage(ItemStack stack, int stage) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        tag.putInt(STAGE_TAG, stage);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return stack;
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, net.minecraft.world.level.Level level, net.minecraft.world.entity.player.Player player, ItemStack stack, BlockState state) {
        BlockState placed = state
                .setValue(RoofTilesBlock.STAGE, placementStage(stack))
                .setValue(RoofTilesBlock.THATCH, placementThatch(stack));
        if (placed != state) {
            level.setBlock(pos, placed, 2);
        }
        return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
    }
}
