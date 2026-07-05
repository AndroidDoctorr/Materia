package com.torr.materia.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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

    public static final MapCodec<InjectLootTableModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> codecStart(inst).and(
            ResourceLocation.CODEC.fieldOf("loot_table").forGetter(m -> m.lootTable)
    ).apply(inst, InjectLootTableModifier::new));

    protected InjectLootTableModifier(LootItemCondition[] conditionsIn, ResourceLocation lootTable) {
        super(conditionsIn);
        this.lootTable = lootTable;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (context.getLevel() instanceof ServerLevel serverLevel) {
            ResourceKey<LootTable> key = ResourceKey.create(Registries.LOOT_TABLE, lootTable);
            LootTable table = serverLevel.getServer().reloadableRegistries().getLootTable(key);
            if (table != LootTable.EMPTY) {
                table.getRandomItems(context, generatedLoot::add);
            }
        }
        return generatedLoot;
    }
}
