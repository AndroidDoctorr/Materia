package com.torr.materia;

import com.torr.materia.effect.DrunkEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, materia.MOD_ID);

    public static final RegistryObject<MobEffect> DRUNK = MOB_EFFECTS.register("drunk",
            () -> new DrunkEffect(net.minecraft.world.effect.MobEffectCategory.HARMFUL, 0x8B4513));
}
