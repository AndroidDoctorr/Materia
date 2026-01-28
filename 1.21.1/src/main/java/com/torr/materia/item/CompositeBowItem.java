package com.torr.materia.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;

public class CompositeBowItem extends BowItem {
    
    public CompositeBowItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            var enchantments = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            boolean hasAmmo = player.getAbilities().instabuild
                    || EnchantmentHelper.getItemEnchantmentLevel(enchantments.getOrThrow(Enchantments.INFINITY), stack) > 0;
            ItemStack ammo = player.getProjectile(stack);
            
            if (!ammo.isEmpty() || hasAmmo) {
                if (ammo.isEmpty()) {
                    ammo = new ItemStack(Items.ARROW);
                }
                
                int useDuration = this.getUseDuration(stack, entity) - timeLeft;
                float power = getPowerForTime(useDuration);
                
                if (!((double) power < 0.1D)) {
                    boolean infiniteAmmo = player.getAbilities().instabuild || (ammo.getItem() instanceof ArrowItem && ((ArrowItem) ammo.getItem()).isInfinite(ammo, stack, player));
                    
                    if (!level.isClientSide) {
                        ArrowItem arrowItem = (ArrowItem) (ammo.getItem() instanceof ArrowItem ? ammo.getItem() : Items.ARROW);
                        AbstractArrow arrow = arrowItem.createArrow(level, ammo, player, stack);
                        arrow = customArrow(arrow);
                        arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, power * 4.0F, 1.0F); // Increased velocity multiplier
                        
                        if (power == 1.0F) {
                            arrow.setCritArrow(true);
                        }
                        
                        int powerEnchantLevel = EnchantmentHelper.getItemEnchantmentLevel(enchantments.getOrThrow(Enchantments.POWER), stack);
                        if (powerEnchantLevel > 0) {
                            arrow.setBaseDamage(arrow.getBaseDamage() + (double) powerEnchantLevel * 0.5D + 0.5D);
                        }
                        
                        // Additional damage bonus for composite bow
                        arrow.setBaseDamage(arrow.getBaseDamage() + 3.0D); // +3 base damage over vanilla bow
                        
                        if (EnchantmentHelper.getItemEnchantmentLevel(enchantments.getOrThrow(Enchantments.FLAME), stack) > 0) {
                            arrow.setRemainingFireTicks(Math.max(arrow.getRemainingFireTicks(), 100));
                        }
                        
                        stack.hurtAndBreak(1, player, player.getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                        
                        if (infiniteAmmo || player.getAbilities().instabuild && (ammo.getItem() == Items.SPECTRAL_ARROW || ammo.getItem() == Items.TIPPED_ARROW)) {
                            arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }
                        
                        level.addFreshEntity(arrow);
                    }
                    
                    level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F);
                    
                    if (!infiniteAmmo && !player.getAbilities().instabuild) {
                        ammo.shrink(1);
                        if (ammo.isEmpty()) {
                            player.getInventory().removeItem(ammo);
                        }
                    }
                    
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }
    
    public static float getPowerForTime(int useDuration) {
        float f = (float) useDuration / 15.0F; // Faster draw time (was 20.0F for vanilla bow)
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }
    
    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }
    
    @Override
    public net.minecraft.world.item.UseAnim getUseAnimation(ItemStack stack) {
        return net.minecraft.world.item.UseAnim.BOW;
    }
    
    public AbstractArrow customArrow(AbstractArrow arrow) {
        return arrow;
    }
    
    public static void registerItemProperties() {
        ItemProperties.register(com.torr.materia.ModItems.COMPOSITE_BOW.get(), 
            ResourceLocation.withDefaultNamespace("pull"), (stack, level, entity, seed) -> {
                if (entity == null) {
                    return 0.0F;
                } else {
                    // Use vanilla calculation for item properties to ensure zoom works
                    return entity.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
                }
            });
        
        ItemProperties.register(com.torr.materia.ModItems.COMPOSITE_BOW.get(), 
            ResourceLocation.withDefaultNamespace("pulling"), (stack, level, entity, seed) -> {
                return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
            });
    }
}
