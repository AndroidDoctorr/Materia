package com.torr.materia.item;

import com.torr.materia.materia;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class BronzeArmorItem extends ArmorItem {
    public BronzeArmorItem(Holder<ArmorMaterial> material, ArmorItem.Type type, Item.Properties properties) {
        super(material, type, properties);
    }

    @Override
    @Nullable
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean inner) {
        boolean legs = slot == EquipmentSlot.LEGS;
        return ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "textures/armor/bronze_armor_" + (legs ? "2" : "1") + ".png");
    }
}
