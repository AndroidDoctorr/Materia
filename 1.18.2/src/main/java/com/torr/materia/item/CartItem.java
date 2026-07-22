package com.torr.materia.item;

import com.torr.materia.entity.CartEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class CartItem extends Item {

    public CartItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        HitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        if (hit.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(stack);
        }

        Vec3 hitLocation = hit.getLocation();
        double x = hitLocation.x;
        double y = hitLocation.y;
        double z = hitLocation.z;
        BlockPos blockPos = new BlockPos(hitLocation);
        if (!level.getBlockState(blockPos).getCollisionShape(level, blockPos).isEmpty()) {
            blockPos = blockPos.relative(Direction.UP);
        }

        CartEntity cart = new CartEntity(level, x, y, z);
        if (!level.noCollision(cart, cart.getBoundingBox().inflate(-0.1D))) {
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide) {
            level.addFreshEntity(cart);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
