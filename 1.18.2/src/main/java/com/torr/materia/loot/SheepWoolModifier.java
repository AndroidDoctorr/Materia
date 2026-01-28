package com.torr.materia.loot;

import com.google.gson.JsonObject;
import com.torr.materia.ModItems;
import com.torr.materia.capability.CustomSheepColorCapability;
import com.torr.materia.entity.CustomSheepColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Loot modifier that replaces sheep wool blocks with colored clumps of wool
 */
public class SheepWoolModifier extends LootModifier {
    
    protected SheepWoolModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        // Get the sheep entity from the loot context
        if (context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof Sheep sheep) {
            // Remove all wool blocks from the loot
            generatedLoot.removeIf(stack -> isWoolBlock(stack.getItem()));
            
            // Check for custom sheep color first
            CustomSheepColor customColor = null;
            var capOptional = sheep.getCapability(CustomSheepColorCapability.CUSTOM_SHEEP_COLOR);
            if (capOptional.isPresent()) {
                var capability = capOptional.resolve().get();
                customColor = capability.getCustomColor();
            }
            
            Item clumpItem;
            if (customColor != null) {
                // Use custom color clump
                clumpItem = customColor.getClumpItem();
            } else {
                // Use vanilla color clump
                DyeColor sheepColor = sheep.getColor();
                clumpItem = getClumpForColor(sheepColor);
            }
            
            if (clumpItem != null) {
                // Drop 1-3 clumps (similar to vanilla wool drop amounts)
                int count = 1 + context.getRandom().nextInt(3);
                generatedLoot.add(new ItemStack(clumpItem, count));
            }
        }
        
        return generatedLoot;
    }
    
    /**
     * Check if an item is a wool block
     */
    private boolean isWoolBlock(Item item) {
        return item == Items.WHITE_WOOL ||
               item == Items.ORANGE_WOOL ||
               item == Items.MAGENTA_WOOL ||
               item == Items.LIGHT_BLUE_WOOL ||
               item == Items.YELLOW_WOOL ||
               item == Items.LIME_WOOL ||
               item == Items.PINK_WOOL ||
               item == Items.GRAY_WOOL ||
               item == Items.LIGHT_GRAY_WOOL ||
               item == Items.CYAN_WOOL ||
               item == Items.PURPLE_WOOL ||
               item == Items.BLUE_WOOL ||
               item == Items.BROWN_WOOL ||
               item == Items.GREEN_WOOL ||
               item == Items.RED_WOOL ||
               item == Items.BLACK_WOOL;
    }
    
    /**
     * Get the appropriate clump item for a sheep color
     */
    private Item getClumpForColor(DyeColor color) {
        return switch (color) {
            case WHITE -> ModItems.CLUMP_OF_WHITE_WOOL.get();
            case ORANGE -> ModItems.CLUMP_OF_ORANGE_WOOL.get();
            case MAGENTA -> ModItems.CLUMP_OF_MAGENTA_WOOL.get();
            case LIGHT_BLUE -> ModItems.CLUMP_OF_LIGHT_BLUE_WOOL.get();
            case YELLOW -> ModItems.CLUMP_OF_YELLOW_WOOL.get();
            case LIME -> ModItems.CLUMP_OF_LIME_WOOL.get();
            case PINK -> ModItems.CLUMP_OF_PINK_WOOL.get();
            case GRAY -> ModItems.CLUMP_OF_GRAY_WOOL.get();
            case LIGHT_GRAY -> ModItems.CLUMP_OF_LIGHT_GRAY_WOOL.get();
            case CYAN -> ModItems.CLUMP_OF_CYAN_WOOL.get();
            case PURPLE -> ModItems.CLUMP_OF_PURPLE_WOOL.get();
            case BLUE -> ModItems.CLUMP_OF_BLUE_WOOL.get();
            case BROWN -> ModItems.CLUMP_OF_BROWN_WOOL.get();
            case GREEN -> ModItems.CLUMP_OF_GREEN_WOOL.get();
            case RED -> ModItems.CLUMP_OF_RED_WOOL.get();
            case BLACK -> ModItems.CLUMP_OF_BLACK_WOOL.get();
        };
    }

    public static class Serializer extends GlobalLootModifierSerializer<SheepWoolModifier> {
        @Override
        public SheepWoolModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditionsIn) {
            return new SheepWoolModifier(conditionsIn);
        }

        @Override
        public JsonObject write(SheepWoolModifier instance) {
            return makeConditions(instance.conditions);
        }
    }
}
