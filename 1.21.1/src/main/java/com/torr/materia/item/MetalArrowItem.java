package com.torr.materia.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import com.torr.materia.entity.MetalArrowEntity;

/**
 * Simple arrow item that increases projectile base damage.
 * In vanilla, damage is largely controlled by the projectile's base damage + velocity and bow effects.
 */
public class MetalArrowItem extends ArrowItem {
    private final double baseDamage;

    public MetalArrowItem(Properties properties, double baseDamage) {
        super(properties);
        this.baseDamage = baseDamage;
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, ItemStack weapon) {
        MetalArrowEntity arrow = new MetalArrowEntity(level, shooter, ammo);
        arrow.setBaseDamage(baseDamage);
        return arrow;
    }
}

