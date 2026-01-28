package com.torr.materia.item;

import com.torr.materia.blockentity.BasketBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class BasketItem extends BlockItem {

    public BasketItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state) {
        boolean result = super.updateCustomBlockEntityTag(pos, level, player, stack, state);
        
        // If the item has stored data, apply it to the placed block entity
        if (stack.hasTag() && stack.getTag().contains("BlockEntityTag")) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof BasketBlockEntity basketEntity) {
                CompoundTag blockEntityTag = stack.getTag().getCompound("BlockEntityTag");
                basketEntity.load(blockEntityTag);
                basketEntity.setChanged();
            }
        }
        
        return result;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        
        // Show contents in tooltip
        if (stack.hasTag() && stack.getTag().contains("BlockEntityTag")) {
            CompoundTag blockEntityTag = stack.getTag().getCompound("BlockEntityTag");
            
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
                    tooltip.add(new TextComponent("§6Contains: " + itemCount + " items"));
                }
            }
        }
    }
}
