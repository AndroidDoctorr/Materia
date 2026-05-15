package com.torr.materia.item;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Tier;

public class HandAxeItem extends AxeItem {
    public HandAxeItem(Tier tier, float attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, properties.attributes(DiggerItem.createAttributes(tier, attackDamageModifier, attackSpeedModifier)));
    }
}
