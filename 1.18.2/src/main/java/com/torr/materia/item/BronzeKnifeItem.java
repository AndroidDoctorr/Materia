package com.torr.materia.item;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

public class BronzeKnifeItem extends SwordItem {

    public BronzeKnifeItem(Properties properties) {
        // Bronze-tier knife: better damage than flint, same speed
        super(Tiers.IRON, 2, -1.8F, properties);
    }
}


