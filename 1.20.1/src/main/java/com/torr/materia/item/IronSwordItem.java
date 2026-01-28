package com.torr.materia.item;

import com.torr.materia.ModToolTiers;
import net.minecraft.world.item.SwordItem;

public class IronSwordItem extends SwordItem {

    public IronSwordItem(Properties properties) {
        // Wrought iron tier sword: bridges bronze and steel (vanilla iron)
        // Better than bronze, but not quite as good as steel
        // Damage modifier 4, attack speed -2.4F (standard sword speed)
        // Total damage: 4 + 2.0 (WROUGHT_IRON tier) = 6.0 damage
        super(ModToolTiers.WROUGHT_IRON, 4, -2.4F, properties);
    }
}

