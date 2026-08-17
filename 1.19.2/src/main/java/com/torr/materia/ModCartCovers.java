package com.torr.materia;

import com.torr.materia.item.CartCoverColor;
import com.torr.materia.item.CartCoverItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/** Registers all {@link CartCoverColor} cover items from one place. */
public final class ModCartCovers {

    private static final Map<CartCoverColor, RegistryObject<Item>> COVERS = new EnumMap<>(CartCoverColor.class);

    private ModCartCovers() {
    }

    static void registerAll() {
        for (CartCoverColor color : CartCoverColor.values()) {
            COVERS.put(color, ModItems.ITEMS.register(color.getItemId(),
                    () -> new CartCoverItem(color, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS))));
        }
    }

    public static RegistryObject<Item> get(CartCoverColor color) {
        return COVERS.get(color);
    }

    public static Map<CartCoverColor, RegistryObject<Item>> all() {
        return Collections.unmodifiableMap(COVERS);
    }
}
