package com.torr.materia.events;

import com.torr.materia.ModItems;
import com.torr.materia.materia;
import com.torr.materia.capability.CustomSheepColorCapability;
import com.torr.materia.entity.CustomSheepColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ShearsItem;
import com.torr.materia.item.BronzeKnifeItem;
import com.torr.materia.item.FlintKnifeItem;
import com.torr.materia.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class SheepShearingHandler {
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        // Only handle main hand interactions
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }
        
        // Check if we're interacting with a sheep
        if (!(event.getTarget() instanceof Sheep sheep)) {
            return;
        }
        
        // Check if player is holding an allowed shearing tool
        ItemStack heldItem = event.getItemStack();
        if (!isShearingTool(heldItem)) {
            return;
        }
        
        // Check if sheep can be sheared (not already sheared and not a baby)
        if (sheep.isSheared() || sheep.isBaby()) {
            return;
        }
        
        // Cancel the event to prevent normal shearing
        event.setResult(Event.Result.ALLOW);
        event.setCanceled(true);
        
        if (!event.getLevel().isClientSide()) {
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
                // Scale yield by tool quality
                int baseMin = 1;
                int baseMax = 3;
                ToolTier tier = getToolTier(heldItem);
                switch (tier) {
                    case FLINT_KNIFE -> { baseMin = 1; baseMax = 2; }
                    case BRONZE_KNIFE -> { baseMin = 2; baseMax = 3; }
                    case BRONZE_SHEARS -> { baseMin = 3; baseMax = 4; }
                    case IRON_SHEARS -> { baseMin = 4; baseMax = 5; }
                    case STEEL_SHEARS -> { baseMin = 5; baseMax = 6; }
                }
                int count = baseMin + event.getLevel().random.nextInt((baseMax - baseMin) + 1);
                
                for (int i = 0; i < count; i++) {
                    ItemStack clumpStack = new ItemStack(clumpItem);
                    ItemEntity itemEntity = new ItemEntity(event.getLevel(), 
                        sheep.getX(), sheep.getY() + 1, sheep.getZ(), clumpStack);
                    itemEntity.setDeltaMovement(
                        event.getLevel().random.nextGaussian() * 0.05,
                        0.2,
                        event.getLevel().random.nextGaussian() * 0.05
                    );
                    event.getLevel().addFreshEntity(itemEntity);
                }
            }
            
            // Set sheep as sheared
            sheep.setSheared(true);
            
            // Damage the shears
            heldItem.hurtAndBreak(1, event.getEntity(), event.getHand() == net.minecraft.world.InteractionHand.MAIN_HAND
                    ? net.minecraft.world.entity.EquipmentSlot.MAINHAND
                    : net.minecraft.world.entity.EquipmentSlot.OFFHAND);
            
            // Play shearing sound
            sheep.playSound(net.minecraft.sounds.SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
        }
    }
    
    /**
     * Get the appropriate clump item for a sheep color
     */
    private static Item getClumpForColor(DyeColor color) {
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

    private enum ToolTier { FLINT_KNIFE, BRONZE_KNIFE, BRONZE_SHEARS, IRON_SHEARS, STEEL_SHEARS }

    private static boolean isShearingTool(ItemStack stack) {
        if (stack.isEmpty()) return false;
        // All shears items (vanilla, bronze, iron)
        if (stack.getItem() instanceof ShearsItem) return true;
        // Bronze and iron shears
        if (stack.getItem() == ModItems.BRONZE_SHEARS.get()) return true;
        if (stack.getItem() == ModItems.IRON_SHEARS.get()) return true;
        // Flint/Bronze knives can shear
        return stack.getItem() instanceof FlintKnifeItem || stack.getItem() instanceof BronzeKnifeItem;
    }

    private static ToolTier getToolTier(ItemStack stack) {
        if (stack.getItem() instanceof FlintKnifeItem) return ToolTier.FLINT_KNIFE;
        if (stack.getItem() instanceof BronzeKnifeItem) return ToolTier.BRONZE_KNIFE;
        if (stack.getItem() == ModItems.BRONZE_SHEARS.get()) return ToolTier.BRONZE_SHEARS;
        if (stack.getItem() == ModItems.IRON_SHEARS.get()) return ToolTier.IRON_SHEARS;
        // Vanilla shears (steel/vanilla iron tier)
        if (stack.getItem() == Items.SHEARS) return ToolTier.STEEL_SHEARS;
        // Default other shears to steel-tier behavior
        return ToolTier.STEEL_SHEARS;
    }
}
