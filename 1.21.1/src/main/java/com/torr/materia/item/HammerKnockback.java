package com.torr.materia.item;

import com.torr.materia.materia;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;

/** Shared attack knockback modifier for Materia hammer tools (1.21+ attribute component API). */
public final class HammerKnockback {
    private HammerKnockback() {}

    public static final ResourceLocation MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "hammer_knockback");

    /** Additive {@link Attributes#ATTACK_KNOCKBACK} on main hand. */
    public static final float AMOUNT = 0.45f;

    public static ItemAttributeModifiers withHammerKnockback(ItemAttributeModifiers base) {
        return base.withModifierAdded(
                Attributes.ATTACK_KNOCKBACK,
                new AttributeModifier(MODIFIER_ID, AMOUNT, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND);
    }
}
