package com.torr.materia;

import com.torr.materia.menu.PrimitiveCraftingMenu;
import com.torr.materia.menu.FirePitMenu;
import com.torr.materia.menu.KilnMenu;
import com.torr.materia.menu.OvenMenu;
import com.torr.materia.menu.CokeOvenMenu;
import com.torr.materia.menu.AdvancedKilnMenu;
import com.torr.materia.menu.FurnaceKilnMenu;
import com.torr.materia.menu.BlastFurnaceMenu;
import com.torr.materia.menu.AmphoraMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, materia.MOD_ID);

    public static final RegistryObject<MenuType<PrimitiveCraftingMenu>> PRIMITIVE_CRAFTING_MENU =
            MENUS.register("primitive_crafting_menu", () -> IForgeMenuType.create(PrimitiveCraftingMenu::new));
    
    public static final RegistryObject<MenuType<FirePitMenu>> FIRE_PIT_MENU =
            MENUS.register("fire_pit_menu", () -> IForgeMenuType.create(FirePitMenu::new));
    
    public static final RegistryObject<MenuType<KilnMenu>> KILN_MENU =
            MENUS.register("kiln_menu", () -> IForgeMenuType.create(KilnMenu::new));
    
    public static final RegistryObject<MenuType<OvenMenu>> OVEN_MENU =
            MENUS.register("oven_menu", () -> IForgeMenuType.create(OvenMenu::new));

    public static final RegistryObject<MenuType<CokeOvenMenu>> COKE_OVEN_MENU =
            MENUS.register("coke_oven_menu", () -> IForgeMenuType.create(CokeOvenMenu::new));
    
    public static final RegistryObject<MenuType<AdvancedKilnMenu>> ADVANCED_KILN_MENU =
            MENUS.register("advanced_kiln_menu", () -> IForgeMenuType.create(AdvancedKilnMenu::new));
    
    public static final RegistryObject<MenuType<FurnaceKilnMenu>> FURNACE_KILN_MENU =
            MENUS.register("furnace_kiln_menu", () -> IForgeMenuType.create(FurnaceKilnMenu::new));
    
    public static final RegistryObject<MenuType<BlastFurnaceMenu>> BLAST_FURNACE_MENU =
            MENUS.register("blast_furnace_menu", () -> IForgeMenuType.create(BlastFurnaceMenu::new));
    
    public static final RegistryObject<MenuType<com.torr.materia.menu.StoneAnvilMenu>> STONE_ANVIL_MENU =
            MENUS.register("stone_anvil_menu", () -> IForgeMenuType.create(com.torr.materia.menu.StoneAnvilMenu::new));
    
    public static final RegistryObject<MenuType<com.torr.materia.menu.BronzeAnvilMenu>> BRONZE_ANVIL_MENU =
            MENUS.register("bronze_anvil_menu", () -> IForgeMenuType.create(com.torr.materia.menu.BronzeAnvilMenu::new));
    
    public static final RegistryObject<MenuType<com.torr.materia.menu.IronAnvilMenu>> IRON_ANVIL_MENU =
            MENUS.register("iron_anvil_menu", () -> IForgeMenuType.create(com.torr.materia.menu.IronAnvilMenu::new));
    
    public static final RegistryObject<MenuType<AmphoraMenu>> AMPHORA_MENU =
            MENUS.register("amphora_menu", () -> IForgeMenuType.create(AmphoraMenu::new));
    
    public static final RegistryObject<MenuType<com.torr.materia.menu.BasketMenu>> BASKET_MENU =
            MENUS.register("basket_menu", () -> IForgeMenuType.create(com.torr.materia.menu.BasketMenu::new));
    
    public static final RegistryObject<MenuType<com.torr.materia.menu.SackMenu>> SACK_MENU =
            MENUS.register("sack_menu", () -> IForgeMenuType.create(com.torr.materia.menu.SackMenu::new));
} 