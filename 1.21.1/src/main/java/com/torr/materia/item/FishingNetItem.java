package com.torr.materia.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;

public class FishingNetItem extends Item {
    public FishingNetItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide) {
            return InteractionResultHolder.pass(stack);
        }

        // Require being on a boat to use effectively
        if (!isOnBoat(player)) {
            // Soft fail: play a subtle splash and cooldown, but no catch
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.FISHING_BOBBER_SPLASH, SoundSource.PLAYERS, 0.4F, 1.0F);
            player.getCooldowns().addCooldown(this, 80);
            return InteractionResultHolder.success(stack);
        }

        BlockHitResult target = rayTraceToWater(level, player, 6.0D);
        if (target == null || target.getType() != HitResult.Type.BLOCK) {
            player.getCooldowns().addCooldown(this, 40);
            return InteractionResultHolder.success(stack);
        }

        BlockPos hitPos = target.getBlockPos();
        // If we hit a non-water block face, try the adjacent in the hit direction
        if (!isWater(level.getFluidState(hitPos))) {
            hitPos = hitPos.relative(target.getDirection());
        }

        // Validate deep water and open surface
        int depth = measureWaterDepth(level, hitPos);
        boolean openSurface = isOpenWaterSurface(level, hitPos);

        if (depth < 4 || !openSurface) {
            // Too shallow or cluttered
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.PLAYERS, 0.4F, 0.8F);
            player.getCooldowns().addCooldown(this, 80);
            return InteractionResultHolder.success(stack);
        }

        // Roll loot based on conditions
        RandomSource random = level.getRandom();
        List<ItemStack> loot = rollCatch(random, depth, level.isRainingAt(hitPos));

        for (ItemStack drop : loot) {
            player.getInventory().placeItemBackInInventory(drop);
        }

        // Feedback
        level.playSound(null, hitPos.getX() + 0.5, hitPos.getY() + 0.5, hitPos.getZ() + 0.5,
                SoundEvents.FISHING_BOBBER_SPLASH, SoundSource.PLAYERS, 0.6F, 0.9F + random.nextFloat() * 0.2F);

        // Durability and cooldown scale a bit with catch size
        int durabilityUse = Math.max(1, loot.size());
        stack.hurtAndBreak(durabilityUse, player, hand == net.minecraft.world.InteractionHand.MAIN_HAND
                ? net.minecraft.world.entity.EquipmentSlot.MAINHAND
                : net.minecraft.world.entity.EquipmentSlot.OFFHAND);
        player.getCooldowns().addCooldown(this, 160);

        return InteractionResultHolder.success(stack);
    }

    private static boolean isOnBoat(Player player) {
        Entity vehicle = player.getVehicle();
        return vehicle instanceof Boat;
    }

    private static BlockHitResult rayTraceToWater(Level level, Player player, double distance) {
        Vec3 eye = player.getEyePosition(1.0F);
        Vec3 look = player.getViewVector(1.0F);
        Vec3 reach = eye.add(look.scale(distance));
        ClipContext context = new ClipContext(eye, reach, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, player);
        BlockHitResult result = level.clip(context);
        if (result.getType() == HitResult.Type.MISS) {
            return null;
        }
        return result;
    }

    private static boolean isWater(FluidState fluidState) {
        return fluidState.getType() == Fluids.WATER || fluidState.getType() == Fluids.FLOWING_WATER;
    }

    private static int measureWaterDepth(Level level, BlockPos surfacePos) {
        // Start at surfacePos and count consecutive water blocks downward until non-water
        int depth = 0;
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(surfacePos.getX(), surfacePos.getY(), surfacePos.getZ());
        for (int i = 0; i < 16; i++) { // cap search depth for safety
            FluidState fluid = level.getFluidState(cursor);
            if (isWater(fluid)) {
                depth++;
                cursor.move(0, -1, 0);
            } else {
                break;
            }
        }
        return depth;
    }

    private static boolean isOpenWaterSurface(Level level, BlockPos surfacePos) {
        // Ensure a 3x3 area at and just above the surface is not blocked by solid blocks
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos pos = surfacePos.offset(dx, 0, dz);
                if (!isWater(level.getFluidState(pos))) return false;
                // Above the surface should be air or water
                if (!level.getBlockState(pos.above()).isAir() && level.getBlockState(pos.above()).getBlock() != Blocks.WATER) {
                    return false;
                }
            }
        }
        return true;
    }

    private static List<ItemStack> rollCatch(RandomSource random, int depth, boolean raining) {
        List<ItemStack> result = new ArrayList<>();
        // Reduced overall catch rate; still allow multiple rolls in excellent conditions
        int rolls = 1 + (depth >= 8 ? 1 : 0) + (raining ? 1 : 0);

        float catchChance = 0.35f;
        if (depth >= 6) catchChance += 0.10f;
        if (raining) catchChance += 0.05f;
        if (catchChance > 0.75f) catchChance = 0.75f; // cap chance

        for (int i = 0; i < rolls; i++) {
            if (random.nextFloat() >= catchChance) {
                continue;
            }

            int r = random.nextInt(100);
            if (r < 30) {
                result.add(random.nextBoolean() ? new ItemStack(Items.KELP) : new ItemStack(Items.SEAGRASS));
            } else if (r < 80) {
                result.add(new ItemStack(Items.COD, 1 + (random.nextFloat() < 0.10F ? 1 : 0)));
            } else if (r < 90) {
                result.add(new ItemStack(Items.SALMON));
            } else if (r < 95) {
                result.add(new ItemStack(Items.PUFFERFISH));
            } else {
                result.add(new ItemStack(Items.TROPICAL_FISH));
            }
        }
        return result;
    }
}


