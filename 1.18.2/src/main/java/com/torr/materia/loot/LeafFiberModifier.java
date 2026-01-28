package com.torr.materia.loot;

import com.google.gson.JsonObject;
import com.torr.materia.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Adds a small chance for leaves to drop plant fibers (similar to sapling odds).
 */
public class LeafFiberModifier extends LootModifier {
    private final float chance;

    protected LeafFiberModifier(LootItemCondition[] conditionsIn, float chance) {
        super(conditionsIn);
        this.chance = chance;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        if (state == null || !state.is(BlockTags.LEAVES)) {
            return generatedLoot;
        }

        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        if (tool != null) {
            if (tool.is(Items.SHEARS)) {
                return generatedLoot; // match vanilla sapling behavior
            }
            if (net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0) {
                return generatedLoot;
            }
        }

        if (context.getRandom().nextFloat() < chance) {
            generatedLoot.add(new ItemStack(ModItems.PLANT_FIBER.get()));
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<LeafFiberModifier> {
        @Override
        public LeafFiberModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditionsIn) {
            float chance = object.has("chance") ? object.get("chance").getAsFloat() : 0.05f;
            return new LeafFiberModifier(conditionsIn, chance);
        }

        @Override
        public JsonObject write(LeafFiberModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.addProperty("chance", instance.chance);
            return json;
        }
    }
}


