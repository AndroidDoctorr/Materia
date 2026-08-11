package com.torr.materia.events;

import com.torr.materia.ModItems;
import com.torr.materia.materia;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = materia.MOD_ID)
public class CopperBucketHandler {

    @SubscribeEvent
    public static void onFillBucket(FillBucketEvent event) {
        ItemStack current = event.getEmptyBucket();
        if (!current.is(ModItems.COPPER_BUCKET.get())) {
            return;
        }

        ItemStack filled = event.getFilledBucket();
        if (filled.isEmpty()) {
            return;
        }

        ItemStack replacement = ItemStack.EMPTY;
        if (filled.is(Items.WATER_BUCKET)) {
            replacement = new ItemStack(ModItems.COPPER_BUCKET_WATER.get());
        } else if (filled.is(Items.MILK_BUCKET)) {
            replacement = new ItemStack(ModItems.COPPER_BUCKET_MILK.get());
        }

        if (!replacement.isEmpty()) {
            event.setFilledBucket(replacement);
        }
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getLevel().isClientSide || !event.getItemStack().is(ModItems.COPPER_BUCKET.get())) {
            return;
        }

        if (!(event.getTarget() instanceof net.minecraft.world.entity.animal.Cow cow)) {
            return;
        }

        if (cow.isBaby() || !cow.isAlive()) {
            return;
        }

        ItemStack stack = event.getItemStack();
        if (stack.getCount() != 1) {
            return;
        }

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setResult(Event.Result.ALLOW);

        ItemStack milkBucket = new ItemStack(ModItems.COPPER_BUCKET_MILK.get());
        ItemStack result = net.minecraft.world.item.ItemUtils.createFilledResult(stack, event.getEntity(), milkBucket);
        event.getEntity().setItemInHand(event.getHand(), result);
        cow.playSound(net.minecraft.sounds.SoundEvents.COW_MILK, 1.0F, 1.0F);
    }
}
