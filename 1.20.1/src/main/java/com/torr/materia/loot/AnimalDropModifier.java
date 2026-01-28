package com.torr.materia.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Loot modifier for adding animal drops (bones and fat)
 */
public class AnimalDropModifier extends LootModifier {
    private final Item item;
    private final int minCount;
    private final int maxCount;

    private record CountRange(int min, int max) {
        static final Codec<CountRange> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.INT.fieldOf("min").forGetter(CountRange::min),
                Codec.INT.fieldOf("max").forGetter(CountRange::max)
        ).apply(inst, CountRange::new));
    }

    private record ItemEntry(Item item, CountRange count) {
        static final Codec<ItemEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(ItemEntry::item),
                CountRange.CODEC.fieldOf("count").forGetter(ItemEntry::count)
        ).apply(inst, ItemEntry::new));
    }

    public static final Codec<AnimalDropModifier> CODEC = RecordCodecBuilder.create(inst -> codecStart(inst).and(
            ItemEntry.CODEC.fieldOf("item").forGetter(m -> new ItemEntry(m.item, new CountRange(m.minCount, m.maxCount)))
    ).apply(inst, (conditions, entry) -> new AnimalDropModifier(conditions, entry.item(), entry.count().min(), entry.count().max())));
    
    protected AnimalDropModifier(LootItemCondition[] conditionsIn, Item item, int minCount, int maxCount) {
        super(conditionsIn);
        this.item = item;
        this.minCount = minCount;
        this.maxCount = maxCount;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // Get random count between min and max (inclusive)
        // Only called when all JSON conditions are met (including random_chance)
        int count = minCount + context.getRandom().nextInt(maxCount - minCount + 1);
        
        // Add our custom drop
        generatedLoot.add(new ItemStack(item, count));
        
        return generatedLoot;
    }
}
