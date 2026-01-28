package com.torr.materia.item;

import com.torr.materia.capability.GlassPipeCapability;
import com.torr.materia.capability.HotMetalCapability;
import com.torr.materia.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Steel pipe item that can hold hot glass for glassblowing
 */
public class SteelPipeItem extends Item {
    
    public SteelPipeItem(Properties properties) {
        super(properties);
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
