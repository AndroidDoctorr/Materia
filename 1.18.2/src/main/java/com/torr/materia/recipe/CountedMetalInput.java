package com.torr.materia.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Deserializes anvil kiln-style metal inputs supporting either Forge {@link Ingredient} JSON plus count,
 * or legacy {@code { "item": "...", "count": n }} shape.
 */
public final class CountedMetalInput {
    public final Ingredient ingredient;
    public final int count;

    public CountedMetalInput(Ingredient ingredient, int count) {
        this.ingredient = ingredient;
        this.count = Math.max(1, count);
    }

    public static CountedMetalInput fromJson(JsonObject inputObj) {
        if (inputObj.has("ingredient")) {
            Ingredient ing = Ingredient.fromJson(inputObj.get("ingredient"));
            int c = GsonHelper.getAsInt(inputObj, "count", 1);
            return new CountedMetalInput(ing, c);
        }
        JsonObject ingredientEl = new JsonObject();
        if (inputObj.has("item")) {
            ingredientEl.add("item", inputObj.get("item"));
        } else if (inputObj.has("tag")) {
            ingredientEl.add("tag", inputObj.get("tag"));
        } else {
            throw new JsonParseException(
                    "Anvil metal input requires \"ingredient\", \"item\", or \"tag\" (JSON: " + inputObj + ")");
        }
        Ingredient ing = Ingredient.fromJson(ingredientEl);
        int c = GsonHelper.getAsInt(inputObj, "count", 1);
        return new CountedMetalInput(ing, c);
    }

    public void toNetwork(FriendlyByteBuf buf) {
        this.ingredient.toNetwork(buf);
        buf.writeVarInt(this.count);
    }

    public static CountedMetalInput fromNetwork(FriendlyByteBuf buf) {
        Ingredient ing = Ingredient.fromNetwork(buf);
        int c = buf.readVarInt();
        return new CountedMetalInput(ing, c);
    }
}
