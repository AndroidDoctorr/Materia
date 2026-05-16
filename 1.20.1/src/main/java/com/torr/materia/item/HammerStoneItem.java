package com.torr.materia.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class HammerStoneItem extends Item {
    public HammerStoneItem(Properties properties) {
        super(properties);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create(super.getDefaultAttributeModifiers(slot));
        if (slot == EquipmentSlot.MAINHAND) {
            multimap.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(HammerKnockback.MODIFIER_UUID, "Hammer knockback",
                    HammerKnockback.AMOUNT, AttributeModifier.Operation.ADDITION));
        }
        return multimap;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        ItemStack copy = stack.copy(); // create a separate instance
        copy.setDamageValue(copy.getDamageValue() + 1); // damage by 1 each craft
        // If the tool has reached or exceeded its max durability, it breaks
        if (copy.getDamageValue() >= copy.getMaxDamage()) {
            return ItemStack.EMPTY;
        }
        return copy;
    }
} 