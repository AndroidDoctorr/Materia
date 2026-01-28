package com.torr.materia.loot;

import com.torr.materia.ModItems;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Adds a small chance for leaves to drop plant fibers (similar to sapling odds).
 */
public class LeafFiberModifier extends LootModifier {
    private final float chance;

    public static final MapCodec<LeafFiberModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> codecStart(inst).and(
            Codec.FLOAT.optionalFieldOf("chance", 0.05f).forGetter(m -> m.chance)
    ).apply(inst, LeafFiberModifier::new));

    protected LeafFiberModifier(LootItemCondition[] conditionsIn, float chance) {
        super(conditionsIn);
        this.chance = chance;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        if (state == null || !state.is(BlockTags.LEAVES)) {
            return generatedLoot;
        }

        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        if (tool != null) {
            if (tool.is(Items.SHEARS)) {
                return generatedLoot; // match vanilla sapling behavior
            }
            var enchantments = context.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            if (net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(enchantments.getOrThrow(Enchantments.SILK_TOUCH), tool) > 0) {
                return generatedLoot;
            }
        }

        if (context.getRandom().nextFloat() < chance) {
            generatedLoot.add(new ItemStack(ModItems.PLANT_FIBER.get()));
        }
        return generatedLoot;
    }
}


