package com.torr.materia.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Wrought-iron armor (registered as materia:iron_*). Uses vanilla iron armor layer
 * textures so it renders correctly on entities without requiring custom layer assets.
 */
public class IronArmorItem extends ArmorItem {
    public IronArmorItem(ModArmorMaterials material, ArmorItem.Type type, Item.Properties properties) {
        super(material, type, properties);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        boolean legs = slot == EquipmentSlot.LEGS;
        return "minecraft:textures/models/armor/iron_layer_" + (legs ? "2" : "1") + ".png";
    }
}
