package com.torr.materia.mosaic;

import com.torr.materia.blockentity.MosaicBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

/** NBT helpers for mosaic block items (canvas data in {@code BlockEntityTag}). */
public final class MosaicItemData {
    private static final String BLOCK_ENTITY_TAG = "BlockEntityTag";

    private MosaicItemData() {
    }

    public static boolean isBlank(ItemStack stack) {
        if (!stack.hasTag() || !stack.getTag().contains(BLOCK_ENTITY_TAG)) {
            return true;
        }
        CompoundTag blockEntityTag = stack.getTag().getCompound(BLOCK_ENTITY_TAG);
        if (!blockEntityTag.contains(MosaicFaceData.CANVAS_KEY, Tag.TAG_BYTE_ARRAY)) {
            return true;
        }
        for (byte value : blockEntityTag.getByteArray(MosaicFaceData.CANVAS_KEY)) {
            if (value != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPainted(ItemStack stack) {
        return !isBlank(stack);
    }

    public static ItemStack createDrop(ItemStack blockItem, MosaicBlockEntity entity, boolean preservePaint) {
        ItemStack stack = blockItem.copy();
        if (preservePaint && entity != null && entity.hasAnyPaint()) {
            CompoundTag blockEntityTag = entity.saveWithoutMetadata();
            stack.getOrCreateTag().put(BLOCK_ENTITY_TAG, blockEntityTag);
        }
        return stack;
    }

    public static ItemStack createDrop(BlockState state, MosaicBlockEntity entity, boolean preservePaint) {
        return createDrop(new ItemStack(state.getBlock()), entity, preservePaint);
    }

    public static void applyBlockEntityTag(MosaicBlockEntity entity, ItemStack stack) {
        if (!stack.hasTag() || !stack.getTag().contains(BLOCK_ENTITY_TAG)) {
            return;
        }
        entity.load(stack.getTag().getCompound(BLOCK_ENTITY_TAG));
        entity.setChanged();
    }

    public static ItemStack copyDesign(ItemStack source) {
        ItemStack result = new ItemStack(source.getItem());
        if (source.hasTag() && source.getTag().contains(BLOCK_ENTITY_TAG)) {
            result.getOrCreateTag().put(BLOCK_ENTITY_TAG,
                    source.getTag().getCompound(BLOCK_ENTITY_TAG).copy());
        }
        return result;
    }
}
