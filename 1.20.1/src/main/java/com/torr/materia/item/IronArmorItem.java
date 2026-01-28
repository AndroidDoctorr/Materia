package com.torr.materia.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;

/**
 * Wrought-iron armor (registered as materia:iron_*). For now, it uses the vanilla iron armor layer
 * textures so it renders correctly on entities without requiring new texture assets.
 */
public class IronArmorItem extends ArmorItem {
    public IronArmorItem(ModArmorMaterials material, ArmorItem.Type type, Item.Properties properties) {
        super(material, type, properties);
    }
}

