package com.torr.materia.item;

import com.torr.materia.capability.GlassPipeCapability;
import com.torr.materia.capability.HotMetalCapability;
import com.torr.materia.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Steel pipe item that can hold hot glass for glassblowing
 */
public class SteelPipeItem extends Item {
    
    public SteelPipeItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
        if (this.allowedIn(tab)) {
            // Add normal steel pipe
            items.add(new ItemStack(this));
            
            // Add steel pipe with hot glass for testing
            ItemStack pipeWithGlass = new ItemStack(this);
            var pipeCapOptional = pipeWithGlass.getCapability(GlassPipeCapability.GLASS_PIPE_CAPABILITY);
            if (pipeCapOptional.isPresent()) {
                var pipeCap = pipeCapOptional.resolve().get();
                
                // Create hot glass puck
                ItemStack hotGlass = new ItemStack(ModItems.GLASS_PUCK.get());
                var hotCapOptional = hotGlass.getCapability(HotMetalCapability.HOT_METAL_CAPABILITY);
                if (hotCapOptional.isPresent()) {
                    hotCapOptional.resolve().get().heat(); // Make it hot
                }
                
                // Add glass to pipe
                pipeCap.addGlass(hotGlass);
                items.add(pipeWithGlass);
            }
        }
    }
    
    /**
     * Get the model suffix based on whether the pipe has glass
     */
    public String getModelSuffix(ItemStack stack) {
        var pipeCapOptional = stack.getCapability(GlassPipeCapability.GLASS_PIPE_CAPABILITY);
        if (pipeCapOptional.isPresent()) {
            var pipeCap = pipeCapOptional.resolve().get();
            if (pipeCap.hasGlass()) {
                return "_with_glass";
            }
        }
        return "";
    }
    
    /**
     * Check if this pipe has hot glass
     */
    public boolean hasHotGlass(ItemStack stack) {
        var pipeCapOptional = stack.getCapability(GlassPipeCapability.GLASS_PIPE_CAPABILITY);
        if (pipeCapOptional.isPresent()) {
            return pipeCapOptional.resolve().get().hasHotGlass();
        }
        return false;
    }
}
