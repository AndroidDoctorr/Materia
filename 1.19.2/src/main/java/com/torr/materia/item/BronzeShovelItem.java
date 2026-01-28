package com.torr.materia.item;

import com.torr.materia.ModToolTiers;
import net.minecraft.world.item.ShovelItem;

public class BronzeShovelItem extends ShovelItem {

    public BronzeShovelItem(Properties properties) {
        // Bronze shovel: slightly slower than iron
        super(ModToolTiers.BRONZE, 1, -3.1F, properties);
    }
}
