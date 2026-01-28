package com.torr.materia.item;

import com.torr.materia.materia;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BronzeArmorItem extends ArmorItem {
    public BronzeArmorItem(ModArmorMaterials material, EquipmentSlot slot, Item.Properties properties) {
        super(material, slot, properties);
    }

    @Override
    public String getArmorTexture(ItemStack stack, net.minecraft.world.entity.Entity entity, EquipmentSlot slot, String type) {
        boolean legs = slot == EquipmentSlot.LEGS;
        return materia.MOD_ID + ":textures/armor/bronze_armor_" + (legs ? "2" : "1") + ".png";
    }
}

