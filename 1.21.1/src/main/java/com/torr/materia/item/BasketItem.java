package com.torr.materia.item;

import com.torr.materia.blockentity.BasketBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class BasketItem extends BlockItem {

    public BasketItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, Player player, ItemStack stack, BlockState state) {
        boolean result = super.updateCustomBlockEntityTag(pos, level, player, stack, state);
        
        // If the item has stored data, apply it to the placed block entity
        CompoundTag blockEntityTag = stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY).copyTag();
        if (!blockEntityTag.isEmpty()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof BasketBlockEntity basketEntity) {
                basketEntity.loadWithComponents(blockEntityTag, level.registryAccess());
                basketEntity.setChanged();
            }
        }
        
        return result;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        
        // Show contents in tooltip
        CompoundTag blockEntityTag = stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY).copyTag();
        if (!blockEntityTag.isEmpty()) {
            
            // ContainerHelper.saveAllItems saves items as a ListTag under "Items"
            if (blockEntityTag.contains("Items", 9)) { // 9 = ListTag type
                net.minecraft.nbt.ListTag itemsList = blockEntityTag.getList("Items", 10); // 10 = CompoundTag type
                int itemCount = 0;
                
                for (int i = 0; i < itemsList.size(); i++) {
                    CompoundTag itemTag = itemsList.getCompound(i);
                    if (itemTag.contains("Count")) {
                        itemCount += itemTag.getByte("Count");
                    }
                }
                
                if (itemCount > 0) {
                    tooltip.add(Component.literal("Â§6Contains: " + itemCount + " items"));
                }
            }
        }
    }
}
