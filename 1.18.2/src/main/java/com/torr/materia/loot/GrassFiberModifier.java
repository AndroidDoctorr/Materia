package com.torr.materia.loot;

import com.google.gson.JsonObject;
import com.torr.materia.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Tall grass and large ferns always drop plant fiber; short grass and ferns have a small chance.
 */
public class GrassFiberModifier extends LootModifier {
    private final float chance;

    protected GrassFiberModifier(LootItemCondition[] conditionsIn, float chance) {
        super(conditionsIn);
        this.chance = chance;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        if (state == null) {
            return generatedLoot;
        }

        boolean isTall = state.is(Blocks.TALL_GRASS) || state.is(Blocks.LARGE_FERN);
        boolean isShort = state.is(Blocks.GRASS) || state.is(Blocks.FERN);
        if (!isTall && !isShort) {
            return generatedLoot;
        }

        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        if (tool != null) {
            if (tool.is(Items.SHEARS)) {
                return generatedLoot;
            }
            if (net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0) {
                return generatedLoot;
            }
        }

        if (isTall || context.getRandom().nextFloat() < chance) {
            generatedLoot.add(new ItemStack(ModItems.PLANT_FIBER.get()));
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<GrassFiberModifier> {
        @Override
        public GrassFiberModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditionsIn) {
            float chance = object.has("chance") ? object.get("chance").getAsFloat() : 0.08f;
            return new GrassFiberModifier(conditionsIn, chance);
        }

        @Override
        public JsonObject write(GrassFiberModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.addProperty("chance", instance.chance);
            return json;
        }
    }
}
