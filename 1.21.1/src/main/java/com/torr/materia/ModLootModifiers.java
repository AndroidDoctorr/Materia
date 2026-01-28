package com.torr.materia;

import com.mojang.serialization.MapCodec;
import com.torr.materia.loot.AnimalDropModifier;
import com.torr.materia.loot.GrassFiberModifier;
import com.torr.materia.loot.LeafFiberModifier;
import com.torr.materia.loot.SheepWoolModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLM_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, materia.MOD_ID);

    public static final RegistryObject<MapCodec<? extends IGlobalLootModifier>> ANIMAL_DROP_MODIFIER =
            GLM_SERIALIZERS.register("animal_drop_modifier", () -> AnimalDropModifier.CODEC);
    
    public static final RegistryObject<MapCodec<? extends IGlobalLootModifier>> SHEEP_WOOL_MODIFIER =
            GLM_SERIALIZERS.register("sheep_wool_modifier", () -> SheepWoolModifier.CODEC);

    public static final RegistryObject<MapCodec<? extends IGlobalLootModifier>> LEAF_FIBER_MODIFIER =
            GLM_SERIALIZERS.register("leaf_fiber_modifier", () -> LeafFiberModifier.CODEC);

    public static final RegistryObject<MapCodec<? extends IGlobalLootModifier>> GRASS_FIBER_MODIFIER =
            GLM_SERIALIZERS.register("grass_fiber_modifier", () -> GrassFiberModifier.CODEC);
}
