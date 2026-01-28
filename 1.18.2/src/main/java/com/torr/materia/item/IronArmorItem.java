package com.torr.materia.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Wrought-iron armor (registered as materia:iron_*). For now, it uses the vanilla iron armor layer
 * textures so it renders correctly on entities without requiring new texture assets.
 */
public class IronArmorItem extends ArmorItem {
    public IronArmorItem(ModArmorMaterials material, EquipmentSlot slot, Item.Properties properties) {
        super(material, slot, properties);
    }

    @Override
    public String getArmorTexture(ItemStack stack, net.minecraft.world.entity.Entity entity, EquipmentSlot slot, String type) {
        boolean legs = slot == EquipmentSlot.LEGS;
        return "minecraft:textures/models/armor/iron_layer_" + (legs ? "2" : "1") + ".png";
    }
}

