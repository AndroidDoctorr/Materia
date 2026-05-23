package com.torr.materia.recipe;

/** How much metal to remove from iron-anvil slots 3 (left) and 4 (right). */
public record IronConsumptionPlan(int takeFromSlot3, int takeFromSlot4) {}
