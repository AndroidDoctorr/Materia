package com.torr.materia.item;

import com.torr.materia.entity.CartEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CartItem extends Item {

    private final CartWoodType woodType;

    public CartItem(CartWoodType woodType, Properties properties) {
        super(properties);
        this.woodType = woodType;
    }

    public CartWoodType getWoodType() {
        return this.woodType;
    }

    public static CartWoodType getWoodType(ItemStack stack) {
        if (stack.getItem() instanceof CartItem cartItem) {
            return cartItem.getWoodType();
        }
        return CartWoodType.OAK;
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
        BlockPos blockPos = BlockPos.containing(hitLocation);
        if (!level.getBlockState(blockPos).getCollisionShape(level, blockPos).isEmpty()) {
            blockPos = blockPos.relative(Direction.UP);
        }

        CartEntity cart = new CartEntity(level, x, y, z);
        cart.setWoodType(this.woodType);
        cart.loadInventoryFromItem(stack);
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (tag.contains("CartHealth")) {
            cart.setCartHealth(tag.getFloat("CartHealth"));
        } else {
            cart.setCartHealth(cart.getMaxHealth());
        }
        if (tag.contains("ShieldSides")) {
            cart.setShieldSidesMask(tag.getInt("ShieldSides"));
        }
        if (!level.noCollision(cart, cart.getBoundingBox().inflate(-0.1D))) {
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide) {
            level.addFreshEntity(cart);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, cart.position());
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (tag.contains("Items", 9)) {
            int itemCount = countStoredItems(tag);
            if (itemCount > 0) {
                tooltip.add(Component.literal("§6Contains: " + itemCount + " items"));
            }
        }
    }

    private static int countStoredItems(CompoundTag tag) {
        int itemCount = 0;
        for (var itemTag : tag.getList("Items", 10)) {
            if (itemTag instanceof CompoundTag compound && compound.contains("Count")) {
                itemCount += compound.getByte("Count");
            }
        }
        return itemCount;
    }
}
