package com.torr.materia.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.torr.materia.capability.HotMetalCapability;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class HeatMetalCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("heatmetal")
            .requires(source -> source.hasPermission(2))
            .executes(HeatMetalCommand::execute));
    }
    
    private static int execute(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getEntity() instanceof Player player) {
            ItemStack heldItem = player.getMainHandItem();
            
            if (!heldItem.isEmpty()) {
                heldItem.getCapability(HotMetalCapability.HOT_METAL_CAPABILITY).ifPresent(hotCapability -> {
                    long beforeHeat = hotCapability.getHeatTime();
                    hotCapability.heat();
                    long afterHeat = hotCapability.getHeatTime();
                    float heatLevel = hotCapability.getHeatLevel();
                    boolean isHot = hotCapability.isHot();
                    context.getSource().sendSuccess(() -> Component.literal("Heated " + heldItem.getDisplayName().getString() + 
                        " (Heat Level: " + String.format("%.2f", heatLevel) + 
                        ", Is Hot: " + isHot + 
                        ", Before: " + beforeHeat + 
                        ", After: " + afterHeat + ")"), false);
                });
                
                if (!heldItem.getCapability(HotMetalCapability.HOT_METAL_CAPABILITY).isPresent()) {
                    // Check if it's in the heatable metals tag
                    boolean inTag = heldItem.is(com.torr.materia.events.HotMetalEventHandler.HEATABLE_METALS);
                    context.getSource().sendFailure(Component.literal("Item cannot be heated! Item: " + heldItem.getItem().toString() + ", In tag: " + inTag));
                }
            } else {
                context.getSource().sendFailure(Component.literal("Hold an item to heat it!"));
            }
        }
        return 1;
    }
}
