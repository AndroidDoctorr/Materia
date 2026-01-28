package com.torr.materia.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.torr.materia.capability.HotMetalCapability;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class TestHeatCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("testheat")
            .requires(source -> source.hasPermission(2))
            .executes(TestHeatCommand::execute));
    }
    
    private static int execute(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getEntity() instanceof Player player) {
            ItemStack heldItem = player.getMainHandItem();
            
            if (!heldItem.isEmpty()) {
                context.getSource().sendSuccess(new TextComponent("Testing heat on: " + heldItem.getDisplayName().getString()), false);
                
                var capOptional = heldItem.getCapability(HotMetalCapability.HOT_METAL_CAPABILITY);
                if (capOptional.isPresent()) {
                    var hotCap = capOptional.resolve().get();
                    
                    // Test instance consistency
                    var capOptional2 = heldItem.getCapability(HotMetalCapability.HOT_METAL_CAPABILITY);
                    var hotCap2 = capOptional2.resolve().get();
                    boolean sameInstance = hotCap == hotCap2;
                    context.getSource().sendSuccess(new TextComponent("Same capability instance: " + sameInstance + " (Hash1: " + hotCap.hashCode() + ", Hash2: " + hotCap2.hashCode() + ")"), false);
                    
                    // Test 1: Initial state
                    context.getSource().sendSuccess(new TextComponent("Initial - Heat Time: " + hotCap.getHeatTime() + ", Heat Level: " + String.format("%.2f", hotCap.getHeatLevel()) + ", Is Hot: " + hotCap.isHot()), false);
                    
                    // Test 2: Heat the item
                    long systemTime = System.currentTimeMillis();
                    context.getSource().sendSuccess(new TextComponent("System.currentTimeMillis(): " + systemTime), false);
                    
                    hotCap.heat();
                    
                    // Test immediately after heat() call with same instance
                    long currentTime2 = System.currentTimeMillis();
                    long elapsed = currentTime2 - hotCap.getHeatTime();
                    long maxTime = 1200 * 50; // heatDuration * 50
                    boolean heatTimeIsZero = (hotCap.getHeatTime() == 0);
                    context.getSource().sendSuccess(new TextComponent("Debug - Current: " + currentTime2 + ", Elapsed: " + elapsed + "ms, MaxTime: " + maxTime + "ms, Elapsed < MaxTime: " + (elapsed < maxTime) + ", HeatTime == 0: " + heatTimeIsZero), false);
                    // Test the isHot logic manually
                    long heatTime = hotCap.getHeatTime();
                    long currentTime3 = System.currentTimeMillis();
                    long heatDuration = 1200; // Same as in capability
                    boolean manualIsHot = (heatTime != 0) && ((currentTime3 - heatTime) < (heatDuration * 50));
                    
                    long actualHeatDuration = hotCap.getHeatDuration();
                    context.getSource().sendSuccess(new TextComponent("Manual isHot calculation: " + manualIsHot + " (heatTime != 0: " + (heatTime != 0) + ", condition: " + ((currentTime3 - heatTime) < (heatDuration * 50)) + ")"), false);
                    context.getSource().sendSuccess(new TextComponent("Capability heatDuration: " + actualHeatDuration + " (expected: 1200)"), false);
                    context.getSource().sendSuccess(new TextComponent("After heat() - Heat Time: " + hotCap.getHeatTime() + ", Heat Level: " + String.format("%.2f", hotCap.getHeatLevel()) + ", Is Hot: " + hotCap.isHot()), false);
                    
                    // Test with fresh capability reference
                    var capOptional3 = heldItem.getCapability(HotMetalCapability.HOT_METAL_CAPABILITY);
                    var hotCap3 = capOptional3.resolve().get();
                    context.getSource().sendSuccess(new TextComponent("Fresh reference - Heat Time: " + hotCap3.getHeatTime() + ", Heat Level: " + String.format("%.2f", hotCap3.getHeatLevel()) + ", Is Hot: " + hotCap3.isHot()), false);
                    
                    // Test 3: Force NBT save and reload
                    var nbtData = hotCap.serializeNBT();
                    context.getSource().sendSuccess(new TextComponent("NBT Data: " + nbtData.toString()), false);
                    
                    // Test 4: Create new capability and deserialize
                    var newCap = new HotMetalCapability();
                    newCap.deserializeNBT(nbtData);
                    context.getSource().sendSuccess(new TextComponent("After NBT roundtrip - Heat Time: " + newCap.getHeatTime() + ", Heat Level: " + String.format("%.2f", newCap.getHeatLevel()) + ", Is Hot: " + newCap.isHot()), false);
                    
                } else {
                    context.getSource().sendFailure(new TextComponent("Item has no HotMetalCapability!"));
                }
            } else {
                context.getSource().sendFailure(new TextComponent("Hold an item to test!"));
            }
        }
        return 1;
    }
}
