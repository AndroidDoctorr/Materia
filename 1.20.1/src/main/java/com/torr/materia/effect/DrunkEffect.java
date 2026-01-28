package com.torr.materia.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

/**
 * Drunk status effect that causes visual distortion and movement impairment
 */
public class DrunkEffect extends MobEffect {
    
    public DrunkEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {
            // Add slight random movement on server side
            if (!entity.level().isClientSide && entity.getRandom().nextFloat() < 0.1f) {
                // Small random push every so often
                double pushX = (entity.getRandom().nextDouble() - 0.5) * 0.05;
                double pushZ = (entity.getRandom().nextDouble() - 0.5) * 0.05;
                entity.push(pushX, 0, pushZ);
            }
        }
        super.applyEffectTick(entity, amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        // Apply effect every 20 ticks (1 second)
        return duration % 20 == 0;
    }
}
