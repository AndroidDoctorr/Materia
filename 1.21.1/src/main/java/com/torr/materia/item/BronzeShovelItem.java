package com.torr.materia.item;

import com.torr.materia.ModToolTiers;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ShovelItem;

public class BronzeShovelItem extends ShovelItem {

    public BronzeShovelItem(Properties properties) {
        // Bronze shovel: slightly slower than iron
        super(ModToolTiers.BRONZE, properties.attributes(DiggerItem.createAttributes(ModToolTiers.BRONZE, 1.0F, -3.1F)));
    }
}
