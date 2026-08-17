package com.torr.materia;

import com.torr.materia.item.CartItem;
import com.torr.materia.item.CartWoodType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/** Registers all {@link CartWoodType} cart and cart-base items. */
public final class ModCarts {

    private static final Map<CartWoodType, RegistryObject<Item>> CARTS = new EnumMap<>(CartWoodType.class);
    private static final Map<CartWoodType, RegistryObject<Item>> CART_BASES = new EnumMap<>(CartWoodType.class);

    private ModCarts() {
    }

    static void registerAll() {
        for (CartWoodType wood : CartWoodType.values()) {
            CART_BASES.put(wood, ModItems.ITEMS.register(wood.getCartBaseId(),
                    () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS))));
            CARTS.put(wood, ModItems.ITEMS.register(wood.getCartId(),
                    () -> new CartItem(wood, new Item.Properties().tab(CreativeModeTab.TAB_TRANSPORTATION).stacksTo(1))));
        }
    }

    public static RegistryObject<Item> get(CartWoodType wood) {
        return CARTS.get(wood);
    }

    public static RegistryObject<Item> getBase(CartWoodType wood) {
        return CART_BASES.get(wood);
    }

    public static Map<CartWoodType, RegistryObject<Item>> all() {
        return Collections.unmodifiableMap(CARTS);
    }

    public static Map<CartWoodType, RegistryObject<Item>> allBases() {
        return Collections.unmodifiableMap(CART_BASES);
    }
}
