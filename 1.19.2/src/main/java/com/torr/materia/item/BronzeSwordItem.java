package com.torr.materia.item;

import com.torr.materia.ModToolTiers;
import net.minecraft.world.item.SwordItem;

public class BronzeSwordItem extends SwordItem {

    public BronzeSwordItem(Properties properties) {
        // Bronze-tier sword: weaker than iron and steel
        // Damage modifier 3, attack speed -2.5F (slightly slower than iron/steel)
        // Total damage: 3 + 2.0 (BRONZE tier) = 5.0 damage
        super(ModToolTiers.BRONZE, 3, -2.5F, properties);
    }
}
