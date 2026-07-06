package com.torr.materia.loot;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Rolls an additional Materia loot table when chest (or other) loot is generated.
 */
public class InjectLootTableModifier extends LootModifier {
    private final ResourceLocation lootTable;

    protected InjectLootTableModifier(LootItemCondition[] conditionsIn, ResourceLocation lootTable) {
        super(conditionsIn);
        this.lootTable = lootTable;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        if (context.getLevel() instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel) context.getLevel();
            LootTable table = serverLevel.getServer().getLootTables().get(lootTable);
            if (table != null && table != LootTable.EMPTY) {
                table.getRandomItems(context, generatedLoot::add);
            }
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<InjectLootTableModifier> {
        @Override
        public InjectLootTableModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditionsIn) {
            ResourceLocation table = new ResourceLocation(object.get("loot_table").getAsString());
            return new InjectLootTableModifier(conditionsIn, table);
        }

        @Override
        public JsonObject write(InjectLootTableModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.addProperty("loot_table", instance.lootTable.toString());
            return json;
        }
    }
}
