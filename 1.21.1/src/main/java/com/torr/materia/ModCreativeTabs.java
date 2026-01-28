package com.torr.materia;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Comparator;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, materia.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TORRS_MOD_TAB = TABS.register("materia", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.materia"))
                    .icon(() -> new ItemStack(ModItems.HAMMER_STONE.get()))
                    .displayItems((params, output) -> ForgeRegistries.ITEMS.getValues().stream()
                            .filter(item -> item != null && item != net.minecraft.world.item.Items.AIR)
                            .filter(item -> {
                                ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
                                return key != null && materia.MOD_ID.equals(key.getNamespace());
                            })
                            .sorted(Comparator.comparing(item -> {
                                ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
                                return key == null ? "" : key.getPath();
                            }))
                            .forEach(output::accept))
                    .build()
    );
}

