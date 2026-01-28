package com.torr.materia.item;

import com.torr.materia.ModToolTiers;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.HoeItem;

public class IronHoeItem extends HoeItem {

    public IronHoeItem(Properties properties) {
        // Iron hoe: faster than bronze, matches vanilla iron speed
        super(ModToolTiers.WROUGHT_IRON, properties.attributes(DiggerItem.createAttributes(ModToolTiers.WROUGHT_IRON, -1.0F, -2.9F)));
    }
}

