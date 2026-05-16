package com.torr.materia.item;

import java.util.UUID;

/** Shared attack knockback modifier for Materia hammer tools. */
public final class HammerKnockback {
    private HammerKnockback() {}

    public static final UUID MODIFIER_UUID = UUID.fromString("8f8e9c3e-7b4a-42df-9e10-b6d701234567");

    /** Additive {@link net.minecraft.world.entity.ai.attributes.Attributes#ATTACK_KNOCKBACK} on main hand. */
    public static final float AMOUNT = 0.45f;
}
