package com.torr.materia.item;

import com.torr.materia.ModToolTiers;
import net.minecraft.world.item.HoeItem;

public class BronzeHoeItem extends HoeItem {

    public BronzeHoeItem(Properties properties) {
        // Bronze hoe: slightly slower than iron
        super(ModToolTiers.BRONZE, -1, -3.1F, properties);
    }
}
