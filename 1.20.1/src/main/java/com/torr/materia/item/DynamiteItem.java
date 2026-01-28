package com.torr.materia.item;

import com.torr.materia.ModEntities;
import com.torr.materia.entity.DynamiteEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class DynamiteItem extends Item {
    
    public DynamiteItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ItemStack offhandStack = player.getOffhandItem();
        
        // Check if player has a bow drill or flint and steel in offhand
        boolean hasIgniter = offhandStack.getItem() instanceof BowDrillItem || 
                            offhandStack.getItem() == Items.FLINT_AND_STEEL;
        
        if (!hasIgniter) {
            return InteractionResultHolder.fail(stack);
        }
        
        // Start using the item (wind up like a bow)
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }
    
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            ItemStack offhandStack = player.getOffhandItem();
            
            // Double-check igniter is still there
            boolean hasIgniter = offhandStack.getItem() instanceof BowDrillItem || 
                                offhandStack.getItem() == Items.FLINT_AND_STEEL;
            
            if (!hasIgniter) {
                return;
            }
            
            int useDuration = this.getUseDuration(stack) - timeLeft;
            float power = getPowerForTime(useDuration);
            
            if (!((double) power < 0.1D)) {
                if (!level.isClientSide) {
                    DynamiteEntity dynamite = new DynamiteEntity(level, player);
                    dynamite.setItem(stack);
                    dynamite.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, power * 1.5F, 1.0F);
                    level.addFreshEntity(dynamite);
                    
                    // Damage the igniter tool
                    if (offhandStack.getItem() instanceof BowDrillItem) {
                        offhandStack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(InteractionHand.OFF_HAND));
                    } else if (offhandStack.getItem() == Items.FLINT_AND_STEEL) {
                        offhandStack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(InteractionHand.OFF_HAND));
                    }
                }
                
                level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), 
                    SoundEvents.TNT_PRIMED, SoundSource.PLAYERS, 1.0F, 
                    1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F);
                
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
        }
    }
    
    public static float getPowerForTime(int useDuration) {
        float f = (float) useDuration / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }
    
    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
    
    @Override
    public net.minecraft.world.item.UseAnim getUseAnimation(ItemStack stack) {
        return net.minecraft.world.item.UseAnim.BOW;
    }
    
    public static void registerItemProperties() {
        ItemProperties.register(com.torr.materia.ModItems.DYNAMITE.get(), 
            new ResourceLocation("pull"), (stack, level, entity, seed) -> {
                if (entity == null) {
                    return 0.0F;
                } else {
                    return entity.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
                }
            });
        
        ItemProperties.register(com.torr.materia.ModItems.DYNAMITE.get(), 
            new ResourceLocation("pulling"), (stack, level, entity, seed) -> {
                return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
            });
    }
}
