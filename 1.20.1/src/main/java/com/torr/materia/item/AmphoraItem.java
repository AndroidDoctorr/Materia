package com.torr.materia.item;

import com.torr.materia.blockentity.AmphoraBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;

public class AmphoraItem extends BlockItem {

    public AmphoraItem(Block block, Properties properties) {
        super(block, properties);
    }

    public static void registerItemProperties() {
        // Register item property for liquid type
        ItemProperties.register(com.torr.materia.ModBlocks.AMPHORA.get().asItem(), 
            new ResourceLocation("liquid_type"), (stack, level, entity, seed) -> {
                if (stack.hasTag() && stack.getTag().contains("BlockEntityTag")) {
                    CompoundTag blockEntityTag = stack.getTag().getCompound("BlockEntityTag");
                    if (blockEntityTag.contains("LiquidType")) {
                        String liquidType = blockEntityTag.getString("LiquidType");
                        return switch (liquidType) {
                            case "water" -> 1.0f;
                            case "wine" -> 2.0f;
                            case "vinegar" -> 3.0f;
                            case "olive_oil" -> 4.0f;
                            case "grape_juice" -> 5.0f;
                            default -> 0.0f; // empty
                        };
                    }
                }
                return 0.0f; // empty
            });
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state) {
        boolean result = super.updateCustomBlockEntityTag(pos, level, player, stack, state);
        
        // If the item has liquid data, apply it to the placed block entity
        if (stack.hasTag() && stack.getTag().contains("BlockEntityTag")) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof AmphoraBlockEntity amphoraEntity) {
                CompoundTag blockEntityTag = stack.getTag().getCompound("BlockEntityTag");
                amphoraEntity.load(blockEntityTag);
                amphoraEntity.setChanged();
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
            int storageMode = blockEntityTag.getInt("StorageMode");
            
            if (storageMode == AmphoraBlockEntity.MODE_LIQUID) {
                // Show liquid contents
                if (blockEntityTag.contains("LiquidAmount") && blockEntityTag.contains("LiquidType")) {
                    int liquidAmount = blockEntityTag.getInt("LiquidAmount");
                    String liquidType = blockEntityTag.getString("LiquidType");
                    
                    if (liquidAmount > 0) {
                        String liquidName = switch (liquidType) {
                            case "water" -> "Water";
                            case "wine" -> "Wine";
                            case "vinegar" -> "Vinegar";
                            case "olive_oil" -> "Olive Oil";
                            case "grape_juice" -> "Grape Juice";
                            default -> liquidType;
                        };
                        
                        tooltip.add(net.minecraft.network.chat.Component.literal("Â§9Contains: " + liquidAmount + " bottles of " + liquidName));
                    }
                }
            } else if (storageMode == AmphoraBlockEntity.MODE_SOLID) {
                // Show solid storage contents
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
                        tooltip.add(net.minecraft.network.chat.Component.literal("Â§6Contains: " + itemCount + " items"));
                    }
                }
            }
            
            // Show lid information
            if (blockEntityTag.contains("HasLid") && blockEntityTag.getBoolean("HasLid")) {
                boolean hasSealed = blockEntityTag.getBoolean("HasSealed");
                String lidType = hasSealed ? "Sealed Lid" : "Lid";
                tooltip.add(net.minecraft.network.chat.Component.literal("Â§7Has " + lidType));
            }
            
            // Show fermentation progress
            if (blockEntityTag.contains("IsFermenting") && blockEntityTag.getBoolean("IsFermenting")) {
                int progress = blockEntityTag.getInt("FermentationProgress");
                boolean hasSealed = blockEntityTag.getBoolean("HasSealed");
                int maxTime = hasSealed ? 72000 : 24000; // Wine vs Vinegar times
                float percent = (float) progress / (float) maxTime * 100f;
                String product = hasSealed ? "Wine" : "Vinegar";
                
                tooltip.add(net.minecraft.network.chat.Component.literal("Â§5Fermenting to " + product + ": " + String.format("%.1f", percent) + "%"));
            }
        }
    }
}
