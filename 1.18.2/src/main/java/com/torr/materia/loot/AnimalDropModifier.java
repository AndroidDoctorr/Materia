package com.torr.materia.loot;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Loot modifier for adding animal drops (bones and fat)
 */
public class AnimalDropModifier extends LootModifier {
    private final Item item;
    private final int minCount;
    private final int maxCount;
    
    protected AnimalDropModifier(LootItemCondition[] conditionsIn, Item item, int minCount, int maxCount) {
        super(conditionsIn);
        this.item = item;
        this.minCount = minCount;
        this.maxCount = maxCount;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        // Get random count between min and max (inclusive)
        // Only called when all JSON conditions are met (including random_chance)
        int count = minCount + context.getRandom().nextInt(maxCount - minCount + 1);
        
        // Add our custom drop
        generatedLoot.add(new ItemStack(item, count));
        
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<AnimalDropModifier> {
        @Override
        public AnimalDropModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditionsIn) {
            JsonObject itemObj = GsonHelper.getAsJsonObject(object, "item");
            String itemName = GsonHelper.getAsString(itemObj, "item");
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
            
            JsonObject countObj = GsonHelper.getAsJsonObject(itemObj, "count");
            int minCount = GsonHelper.getAsInt(countObj, "min", 1);
            int maxCount = GsonHelper.getAsInt(countObj, "max", 1);
            
            return new AnimalDropModifier(conditionsIn, item, minCount, maxCount);
        }

        @Override
        public JsonObject write(AnimalDropModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            
            JsonObject itemObj = new JsonObject();
            itemObj.addProperty("item", ForgeRegistries.ITEMS.getKey(instance.item).toString());
            
            JsonObject countObj = new JsonObject();
            countObj.addProperty("min", instance.minCount);
            countObj.addProperty("max", instance.maxCount);
            itemObj.add("count", countObj);
            
            json.add("item", itemObj);
            
            return json;
        }
    }
}
