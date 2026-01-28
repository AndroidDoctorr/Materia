package com.torr.materia.item;

import com.torr.materia.ModToolTiers;
import net.minecraft.world.item.ShovelItem;

public class IronShovelItem extends ShovelItem {

    public IronShovelItem(Properties properties) {
        // Iron shovel: faster than bronze, matches vanilla iron speed
        super(ModToolTiers.WROUGHT_IRON, 1.5F, -3.0F, properties);
    }
}

