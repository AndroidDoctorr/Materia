package com.torr.materia.util;

import com.torr.materia.ModBlocks;
import com.torr.materia.ModItems;
import com.torr.materia.block.CannonBlock;
import com.torr.materia.blockentity.CannonBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CannonDispenserBehaviors {
    private CannonDispenserBehaviors() {
    }

    public static void register() {
        // Powder
        registerPowderBehavior(Items.GUNPOWDER);
        registerPowderBehavior(ModBlocks.GUNPOWDER_TRAIL.get().asItem());

        // Ammo
        registerAmmoBehavior(Items.TNT);
        registerAmmoBehavior(ModItems.STONE_CANNONBALL.get());
        registerAmmoBehavior(ModItems.IRON_CANNONBALL.get());
        registerAmmoBehavior(ModItems.CANISTER_SHOT.get());
    }

    private static void registerPowderBehavior(Item item) {
        DispenserBlock.registerBehavior(item, new DefaultDispenseItemBehavior() {
            @Override
            protected ItemStack execute(BlockSource source, ItemStack stack) {
                if (tryLoadPowder(source, stack)) {
                    return stack;
                }
                return super.execute(source, stack);
            }
        });
    }

    private static void registerAmmoBehavior(Item item) {
        DispenserBlock.registerBehavior(item, new DefaultDispenseItemBehavior() {
            @Override
            protected ItemStack execute(BlockSource source, ItemStack stack) {
                if (tryLoadAmmo(source, stack)) {
                    return stack;
                }
                return super.execute(source, stack);
            }
        });
    }

    private static boolean tryLoadPowder(BlockSource source, ItemStack stack) {
        Level level = source.getLevel();
        BlockState dispenserState = source.getBlockState();
        Direction facing = dispenserState.getValue(DispenserBlock.FACING);
        BlockPos targetPos = source.getPos().relative(facing);

        BlockState targetState = level.getBlockState(targetPos);
        if (!(targetState.getBlock() instanceof CannonBlock)) return false;

        BlockEntity be = level.getBlockEntity(targetPos);
        if (!(be instanceof CannonBlockEntity cannonBe)) return false;

        // Gunpowder first: don't add powder if ammo is already loaded
        if (cannonBe.hasAmmo()) return false;
        if (cannonBe.isFullyCharged()) return false;

        cannonBe.addPowder(1);
        stack.shrink(1);
        level.sendBlockUpdated(targetPos, targetState, targetState, 3);
        return true;
    }

    private static boolean tryLoadAmmo(BlockSource source, ItemStack stack) {
        Level level = source.getLevel();
        BlockState dispenserState = source.getBlockState();
        Direction facing = dispenserState.getValue(DispenserBlock.FACING);
        BlockPos targetPos = source.getPos().relative(facing);

        BlockState targetState = level.getBlockState(targetPos);
        if (!(targetState.getBlock() instanceof CannonBlock)) return false;

        BlockEntity be = level.getBlockEntity(targetPos);
        if (!(be instanceof CannonBlockEntity cannonBe)) return false;

        if (!cannonBe.hasPowder()) return false;
        if (cannonBe.hasAmmo()) return false;

        cannonBe.setAmmo(stack);
        stack.shrink(1);
        level.sendBlockUpdated(targetPos, targetState, targetState, 3);
        return true;
    }
}

