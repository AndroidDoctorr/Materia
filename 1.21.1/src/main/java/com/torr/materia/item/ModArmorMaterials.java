package com.torr.materia.item;

import com.torr.materia.ModItems;
import com.torr.materia.materia;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Map;

/**
 * 1.21+ armor materials are registry entries (ArmorMaterial is a record, not an interface).
 * Armor items reference them via Holder&lt;ArmorMaterial&gt;.
 */
public final class ModArmorMaterials {
    private ModArmorMaterials() {}

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, materia.MOD_ID);

    // Keep old multipliers so durability stays consistent with the 1.20.1 behavior.
    public static final int BRONZE_DURABILITY_MULTIPLIER = 16;
    public static final int WROUGHT_IRON_DURABILITY_MULTIPLIER = 14;

    public static final RegistryObject<ArmorMaterial> BRONZE = ARMOR_MATERIALS.register("bronze", () ->
            new ArmorMaterial(
                    defense(2, 5, 6, 2),
                    10,
                    SoundEvents.ARMOR_EQUIP_IRON,
                    () -> Ingredient.of(ModItems.BRONZE_INGOT.get()),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "bronze"))),
                    0.0F,
                    0.0F
            )
    );

    // Wrought iron uses vanilla iron textures for now (same as 1.20.1 behavior).
    public static final RegistryObject<ArmorMaterial> WROUGHT_IRON = ARMOR_MATERIALS.register("wrought_iron", () ->
            new ArmorMaterial(
                    defense(2, 5, 6, 2),
                    9,
                    SoundEvents.ARMOR_EQUIP_IRON,
                    () -> Ingredient.of(ModItems.WROUGHT_IRON_INGOT.get()),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath("minecraft", "iron"))),
                    0.0F,
                    0.0F
            )
    );

    private static Map<ArmorItem.Type, Integer> defense(int helmet, int chest, int legs, int boots) {
        return Map.of(
                ArmorItem.Type.HELMET, helmet,
                ArmorItem.Type.CHESTPLATE, chest,
                ArmorItem.Type.LEGGINGS, legs,
                ArmorItem.Type.BOOTS, boots
        );
    }

    public static Holder<ArmorMaterial> bronzeHolder() {
        return BRONZE.getHolder().orElseThrow();
    }

    public static Holder<ArmorMaterial> wroughtIronHolder() {
        return WROUGHT_IRON.getHolder().orElseThrow();
    }

    public static void register(IEventBus bus) {
        ARMOR_MATERIALS.register(bus);
    }
}

