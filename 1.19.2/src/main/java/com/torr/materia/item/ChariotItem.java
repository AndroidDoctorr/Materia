package com.torr.materia.item;

import com.torr.materia.entity.ChariotEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ChariotItem extends Item {

    private final ChariotType chariotType;

    public ChariotItem(ChariotType chariotType, Properties properties) {
        super(properties);
        this.chariotType = chariotType;
    }

    public ChariotType getChariotType() {
        return this.chariotType;
    }

    public static ChariotType getChariotType(ItemStack stack) {
        if (stack.getItem() instanceof ChariotItem chariotItem) {
            return chariotItem.getChariotType();
        }
        return ChariotType.BRONZE;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        HitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        if (hit.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(stack);
        }

        Vec3 hitLocation = hit.getLocation();
        BlockPos blockPos = new BlockPos(hitLocation);
        if (!level.getBlockState(blockPos).getCollisionShape(level, blockPos).isEmpty()) {
            blockPos = blockPos.relative(Direction.UP);
        }

        ChariotEntity chariot = new ChariotEntity(level, hitLocation.x, hitLocation.y, hitLocation.z);
        chariot.setChariotType(this.chariotType);
        if (stack.hasTag() && stack.getTag().contains("ChariotHealth")) {
            chariot.setChariotHealth(stack.getTag().getFloat("ChariotHealth"));
        } else {
            chariot.setChariotHealth(chariot.getMaxHealth());
        }
        if (!level.noCollision(chariot, chariot.getBoundingBox().inflate(-0.1D))) {
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide) {
            level.addFreshEntity(chariot);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, chariot.position());
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
