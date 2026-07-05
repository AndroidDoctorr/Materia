package com.torr.materia.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

/**
 * Rolls an additional Materia loot table when chest (or other) loot is generated.
 */
public class InjectLootTableModifier extends LootModifier {
    private final ResourceLocation lootTable;

    public static final Codec<InjectLootTableModifier> CODEC = RecordCodecBuilder.create(inst -> codecStart(inst).and(
            ResourceLocation.CODEC.fieldOf("loot_table").forGetter(m -> m.lootTable)
    ).apply(inst, InjectLootTableModifier::new));

    protected InjectLootTableModifier(LootItemCondition[] conditionsIn, ResourceLocation lootTable) {
        super(conditionsIn);
        this.lootTable = lootTable;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        LootTable table = context.getResolver().getLootTable(lootTable);
        if (table != LootTable.EMPTY) {
            table.getRandomItems(context, generatedLoot::add);
        }
        return generatedLoot;
    }
}
