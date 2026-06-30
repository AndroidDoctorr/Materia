package com.torr.materia.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class TaroItem extends BlockItem {

    private static final int POISON_DURATION = 100;

    public TaroItem(Block block, Properties properties) {
        super(block, properties.food(new FoodProperties.Builder()
                .nutrition(2)
                .saturationMod(0.3f)
                .build()));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (!level.isClientSide) {
            entity.addEffect(new MobEffectInstance(MobEffects.POISON, POISON_DURATION, 0));
        }
        return result;
    }
}
