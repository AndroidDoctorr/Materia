package com.torr.materia;

import com.torr.materia.item.ChariotItem;
import com.torr.materia.item.ChariotType;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/** Registers bronze and iron chariot items. */
public final class ModChariots {

    private static final Map<ChariotType, RegistryObject<Item>> CHARIOTS = new EnumMap<>(ChariotType.class);

    private ModChariots() {
    }

    static void registerAll() {
        for (ChariotType type : ChariotType.values()) {
            CHARIOTS.put(type, ModItems.ITEMS.register(type.getItemId(),
                    () -> new ChariotItem(type, new Item.Properties().stacksTo(1))));
        }
    }

    public static RegistryObject<Item> get(ChariotType type) {
        return CHARIOTS.get(type);
    }

    public static Map<ChariotType, RegistryObject<Item>> all() {
        return Collections.unmodifiableMap(CHARIOTS);
    }
}
