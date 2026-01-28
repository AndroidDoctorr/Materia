package com.torr.materia;

import com.mojang.logging.LogUtils;

import com.torr.materia.block.GrapeVineBlock;
import com.torr.materia.config.materiaCommonConfig;
import com.torr.materia.world.feature.ModConfiguredFeatures;
import com.torr.materia.world.feature.ModFeatures;
import com.torr.materia.world.feature.ModFoliagePlacerTypes;
import com.torr.materia.world.feature.ModPlacedFeatures;
import com.torr.materia.network.NetworkHandler;
import com.torr.materia.util.CannonDispenserBehaviors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("materia")
public class materia
{
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    
    // Define mod ID constant
    public static final String MOD_ID = "materia";

    public materia()
    {
        // Server-side tuning toggles for "world-affecting" mechanics (A2)
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, materiaCommonConfig.SPEC);

        // Register the items
        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        // Register entities
        ModEntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        // Register sounds
        ModSounds.SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
        
        // Register the blocks
        ModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        
        // Register world generation features
        ModFeatures.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModConfiguredFeatures.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModPlacedFeatures.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModFoliagePlacerTypes.register(FMLJavaModLoadingContext.get().getModEventBus());
        
        // Register recipe serializers
        ModRecipes.RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        
        // Register menu types
        ModMenuTypes.MENUS.register(FMLJavaModLoadingContext.get().getModEventBus());
        
        // Register block entities
        ModBlockEntities.BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        
        // Register mob effects
        ModEffects.MOB_EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        
        // Register loot modifier serializers
        ModLootModifiers.GLM_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        
        // Register network handler
        NetworkHandler.register();
        
        // Register recipe types during setup event
        event.enqueueWork(() -> {
            ModRecipes.FIRE_PIT_TYPE = RecipeType.register(new ResourceLocation(materia.MOD_ID, "fire_pit").toString());
            ModRecipes.KILN_TYPE = RecipeType.register(new ResourceLocation(materia.MOD_ID, "kiln").toString());
            ModRecipes.OVEN_TYPE = RecipeType.register(new ResourceLocation(materia.MOD_ID, "oven").toString());
            ModRecipes.ADVANCED_KILN_TYPE = RecipeType.register(new ResourceLocation(materia.MOD_ID, "advanced_kiln").toString());
            ModRecipes.STONE_ANVIL_TYPE = RecipeType.register(new ResourceLocation(materia.MOD_ID, "stone_anvil").toString());
            ModRecipes.BRONZE_ANVIL_TYPE = RecipeType.register(new ResourceLocation(materia.MOD_ID, "bronze_anvil").toString());
            ModRecipes.IRON_ANVIL_TYPE = RecipeType.register(new ResourceLocation(materia.MOD_ID, "iron_anvil").toString());

            // Forge 1.18.2: vanilla BlockEntityType.BED only accepts vanilla bed blocks.
            // If our custom bed blocks are not considered "valid", the game can drop their bed block entities,
            // which makes the beds render invisible (beds rely on a block entity renderer).
            try {
                addValidBlocksToBedType(
                        ModBlocks.OCHRE_BED.get(),
                        ModBlocks.RED_OCHRE_BED.get(),
                        ModBlocks.INDIGO_BED.get(),
                        ModBlocks.OLIVE_BED.get(),
                        ModBlocks.TYRIAN_PURPLE_BED.get(),
                        ModBlocks.LAVENDER_BED.get(),
                        ModBlocks.CHARCOAL_GRAY_BED.get(),
                        ModBlocks.TAUPE_BED.get()
                );
            } catch (Exception e) {
                LOGGER.error("Failed to extend BlockEntityType.BED valid blocks set for materia custom beds", e);
            }

            // Dispenser automation for cannons
            CannonDispenserBehaviors.register();
        });
    }

    /**
     * Reflectively extends BlockEntityType.BED's valid blocks set (Forge 1.18.2 has no public event for this).
     */
    @SuppressWarnings("unchecked")
    private static void addValidBlocksToBedType(Block... blocksToAdd) throws Exception {
        BlockEntityType<?> bedType = BlockEntityType.BED;

        Field targetField = null;
        for (Field f : BlockEntityType.class.getDeclaredFields()) {
            if (!Set.class.isAssignableFrom(f.getType())) continue;
            f.setAccessible(true);
            Object value = f.get(bedType);
            if (!(value instanceof Set)) continue;
            Set<?> set = (Set<?>) value;
            // Heuristic: the BED type's set should contain vanilla bed blocks (e.g., RED_BED)
            if (set.contains(Blocks.RED_BED)) {
                targetField = f;
                break;
            }
        }

        if (targetField == null) {
            throw new IllegalStateException("Could not locate BlockEntityType.BED valid blocks field via reflection");
        }

        Set<Block> current = (Set<Block>) targetField.get(bedType);
        Set<Block> updated = new HashSet<>(current);
        for (Block b : blocksToAdd) {
            updated.add(b);
        }
        targetField.set(bedType, updated);
        LOGGER.info("Extended BlockEntityType.BED valid blocks with {} custom beds", blocksToAdd.length);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // Some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // Some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.messageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
        {
            // Register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
