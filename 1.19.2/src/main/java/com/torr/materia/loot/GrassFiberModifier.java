package com.torr.materia.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.torr.materia.ModItems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

/**
 * Adds a small chance for grass / ferns to drop plant fibers when broken normally.
 */
public class GrassFiberModifier extends LootModifier {
    private final float chance;

    public static final Codec<GrassFiberModifier> CODEC = RecordCodecBuilder.create(inst -> codecStart(inst).and(
            Codec.FLOAT.optionalFieldOf("chance", 0.08f).forGetter(m -> m.chance)
    ).apply(inst, GrassFiberModifier::new));

    protected GrassFiberModifier(LootItemCondition[] conditionsIn, float chance) {
        super(conditionsIn);
        this.chance = chance;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        if (state == null) return generatedLoot;

        boolean isGrassLike =
                state.is(Blocks.GRASS) ||
                state.is(Blocks.TALL_GRASS) ||
                state.is(Blocks.FERN) ||
                state.is(Blocks.LARGE_FERN);

        if (!isGrassLike) return generatedLoot;

        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        if (tool != null) {
            // If you shear/silk-touch grass, vanilla expects the block itself, so don't add fiber.
            if (tool.is(Items.SHEARS)) return generatedLoot;
            if (net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0) {
                return generatedLoot;
            }
        }

        if (context.getRandom().nextFloat() < chance) {
            generatedLoot.add(new ItemStack(ModItems.PLANT_FIBER.get()));
        }
        return generatedLoot;
    }
}

