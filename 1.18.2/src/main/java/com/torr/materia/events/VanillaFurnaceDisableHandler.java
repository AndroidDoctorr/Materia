package com.torr.materia.events;

import com.torr.materia.materia;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;

/**
 * Keeps {@code minecraft:furnace} craftable for recipe compatibility but prevents player use.
 * Smoker and blast furnace are unaffected.
 */
@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public final class VanillaFurnaceDisableHandler {

    private static Field litTimeField;
    private static Field litDurationField;
    private static Field cookingProgressField;
    private static Field cookingTotalTimeField;

    private VanillaFurnaceDisableHandler() {
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCanceled() || event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }
        if (event.getPlayer().isShiftKeyDown()) {
            return;
        }
        Level level = event.getWorld();
        if (level.isClientSide() || !level.getBlockState(event.getPos()).is(Blocks.FURNACE)) {
            return;
        }

        haltSmelting(level, event.getPos());
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.FAIL);
        event.getPlayer().displayClientMessage(
                new TranslatableComponent("message.materia.vanilla_furnace_disabled"), true);
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getWorld().isClientSide() || !event.getPlacedBlock().is(Blocks.FURNACE)) {
            return;
        }
        if (event.getWorld() instanceof Level level) {
            haltSmelting(level, event.getPos());
        }
    }

    private static void haltSmelting(Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof AbstractFurnaceBlockEntity furnace)) {
            return;
        }
        setIntField(furnace, litTimeField(), 0);
        setIntField(furnace, litDurationField(), 0);
        setIntField(furnace, cookingProgressField(), 0);
        setIntField(furnace, cookingTotalTimeField(), 0);
        furnace.setChanged();
    }

    private static Field litTimeField() {
        return field(litTimeField, "litTime", f -> litTimeField = f);
    }

    private static Field litDurationField() {
        return field(litDurationField, "litDuration", f -> litDurationField = f);
    }

    private static Field cookingProgressField() {
        return field(cookingProgressField, "cookingProgress", f -> cookingProgressField = f);
    }

    private static Field cookingTotalTimeField() {
        return field(cookingTotalTimeField, "cookingTotalTime", f -> cookingTotalTimeField = f);
    }

    private static Field field(Field cached, String name, java.util.function.Consumer<Field> cache) {
        if (cached != null) {
            return cached;
        }
        try {
            Field resolved = AbstractFurnaceBlockEntity.class.getDeclaredField(name);
            resolved.setAccessible(true);
            cache.accept(resolved);
            return resolved;
        } catch (ReflectiveOperationException exception) {
            materia.LOGGER.error("Failed to access AbstractFurnaceBlockEntity.{}", name, exception);
            throw new IllegalStateException(exception);
        }
    }

    private static void setIntField(AbstractFurnaceBlockEntity furnace, Field field, int value) {
        try {
            field.setInt(furnace, value);
        } catch (IllegalAccessException exception) {
            materia.LOGGER.error("Failed to reset vanilla furnace smelting state", exception);
        }
    }
}
