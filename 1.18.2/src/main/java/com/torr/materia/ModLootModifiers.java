package com.torr.materia;

import com.torr.materia.loot.AnimalDropModifier;
import com.torr.materia.loot.LeafFiberModifier;
import com.torr.materia.loot.SheepWoolModifier;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModLootModifiers {
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLM_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, materia.MOD_ID);

    public static final RegistryObject<GlobalLootModifierSerializer<AnimalDropModifier>> ANIMAL_DROP_MODIFIER =
            GLM_SERIALIZERS.register("animal_drop_modifier", AnimalDropModifier.Serializer::new);
    
    public static final RegistryObject<GlobalLootModifierSerializer<SheepWoolModifier>> SHEEP_WOOL_MODIFIER =
            GLM_SERIALIZERS.register("sheep_wool_modifier", SheepWoolModifier.Serializer::new);

    public static final RegistryObject<GlobalLootModifierSerializer<LeafFiberModifier>> LEAF_FIBER_MODIFIER =
            GLM_SERIALIZERS.register("leaf_fiber_modifier", LeafFiberModifier.Serializer::new);
}
