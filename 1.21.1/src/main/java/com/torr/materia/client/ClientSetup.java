package com.torr.materia.client;

import com.torr.materia.ModBlocks;
import com.torr.materia.ModMenuTypes;
import com.torr.materia.materia;
import com.torr.materia.client.screen.PrimitiveCraftingScreen;
import com.torr.materia.client.screen.FirePitScreen;
import com.torr.materia.client.screen.KilnScreen;
import com.torr.materia.client.screen.OvenScreen;
import com.torr.materia.client.screen.CokeOvenScreen;
import com.torr.materia.client.screen.AdvancedKilnScreen;
import com.torr.materia.client.screen.AmphoraScreen;
import com.torr.materia.client.screen.BasketScreen;
import com.torr.materia.client.screen.SackScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.BiomeColors;
import com.torr.materia.client.WaterPotRenderer;
import com.torr.materia.client.DryingRackRenderer;
import com.torr.materia.client.TableRenderer;
import com.torr.materia.client.CannonRenderer;
import com.torr.materia.client.SpinningWheelRenderer;
import com.torr.materia.ModBlockEntities;

import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import com.torr.materia.ModEntities;
import com.torr.materia.client.renderer.FlintSpearRenderer;
import com.torr.materia.client.renderer.MetalArrowRenderer;
import com.torr.materia.client.renderer.entity.FallingAmphoraRenderer;
import com.torr.materia.client.translation.ConditionalTranslationManager;
import com.torr.materia.blockentity.FrameLoomBlockEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.Blocks;
import com.torr.materia.entity.CustomSheepColor;

@Mod.EventBusSubscriber(modid = materia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenuTypes.PRIMITIVE_CRAFTING_MENU.get(), PrimitiveCraftingScreen::new);
            MenuScreens.register(ModMenuTypes.FIRE_PIT_MENU.get(), FirePitScreen::new);
            MenuScreens.register(ModMenuTypes.KILN_MENU.get(), KilnScreen::new);
            MenuScreens.register(ModMenuTypes.OVEN_MENU.get(), OvenScreen::new);
            MenuScreens.register(ModMenuTypes.COKE_OVEN_MENU.get(), CokeOvenScreen::new);
            MenuScreens.register(ModMenuTypes.ADVANCED_KILN_MENU.get(), AdvancedKilnScreen::new);
            MenuScreens.register(ModMenuTypes.FURNACE_KILN_MENU.get(), com.torr.materia.client.screen.FurnaceKilnScreen::new);
            MenuScreens.register(ModMenuTypes.BLAST_FURNACE_MENU.get(), com.torr.materia.client.screen.BlastFurnaceScreen::new);
            MenuScreens.register(ModMenuTypes.STONE_ANVIL_MENU.get(), com.torr.materia.client.screen.StoneAnvilScreen::new);
            MenuScreens.register(ModMenuTypes.BRONZE_ANVIL_MENU.get(), com.torr.materia.client.screen.BronzeAnvilScreen::new);
            MenuScreens.register(ModMenuTypes.IRON_ANVIL_MENU.get(), com.torr.materia.client.screen.IronAnvilScreen::new);
            MenuScreens.register(ModMenuTypes.AMPHORA_MENU.get(), AmphoraScreen::new);
            MenuScreens.register(ModMenuTypes.BASKET_MENU.get(), BasketScreen::new);
            MenuScreens.register(ModMenuTypes.SACK_MENU.get(), SackScreen::new);

            // Item properties
            com.torr.materia.item.AmphoraItem.registerItemProperties();
            com.torr.materia.item.CompositeBowItem.registerItemProperties();
            com.torr.materia.item.DynamiteItem.registerItemProperties();
            com.torr.materia.item.BombItem.registerItemProperties();

            // Register render layer for transparency
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.FLAX_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_FLAX.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.SQUASH_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_SQUASH.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.BEANS_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_BEANS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.PEPPERS_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_PEPPERS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CORN_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.GRAPE_VINE.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.HOPS_VINE.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_CORN.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.THREE_SISTERS_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.THREE_SISTERS_CORN_UPPER.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WATER_POT.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.LAVA_POT.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.OIL_LAMP.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.OCHRE_GLASS.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.RED_OCHRE_GLASS.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.OLIVE_GLASS.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.INDIGO_GLASS.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.TAUPE_GLASS.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.TYRIAN_PURPLE_GLASS.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CHARCOAL_GRAY_GLASS.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.INDIGO.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.FISH_TRAP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.RUBBER_TREE_LEAVES.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.RUBBER_TREE_SAPLING.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.RUBBER_WOOD_TRELLIS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.OAK_TRELLIS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.DARK_OAK_TRELLIS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.SPRUCE_TRELLIS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.BIRCH_TRELLIS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.JUNGLE_TRELLIS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.ACACIA_TRELLIS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.MANGROVE_TRELLIS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CRIMSON_TRELLIS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WARPED_TRELLIS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.OAK_POST.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.SPRUCE_POST.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.BIRCH_POST.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.JUNGLE_POST.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.ACACIA_POST.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.MANGROVE_POST.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CRIMSON_POST.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WARPED_POST.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.RUBBER_WOOD_POST.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.DARK_OAK_POST.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.OAK_JOISTS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.DARK_OAK_JOISTS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.SPRUCE_JOISTS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.BIRCH_JOISTS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.JUNGLE_JOISTS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.ACACIA_JOISTS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.MANGROVE_JOISTS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CRIMSON_JOISTS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WARPED_JOISTS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.RUBBER_WOOD_JOISTS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_GRAPE_VINE.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_HOPS_VINE.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.LAVENDER_GLASS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.FRAME_LOOM.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.SPINNING_WHEEL.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.AQUILA_AUREA.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.OLIVE_SAPLING.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_WISTERIA_VINE.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WISTERIA_VINE.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WISTERIA_HANGING.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.GRAPES_HANGING.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.HOPS_HANGING.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.GUNPOWDER_TRAIL.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.BRINING_VAT.get(), RenderType.translucent());

            // Register water pot BER
            BlockEntityRenderers.register(ModBlockEntities.WATER_POT_BLOCK_ENTITY.get(), WaterPotRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.DRYING_RACK_BLOCK_ENTITY.get(), DryingRackRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.TABLE_BLOCK_ENTITY.get(), TableRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.CANNON_BLOCK_ENTITY.get(), CannonRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.SPINNING_WHEEL_BLOCK_ENTITY.get(), SpinningWheelRenderer::new);
            
            // Register custom bed renderer for all beds (handles both custom and vanilla)
            BlockEntityRenderers.register(net.minecraft.world.level.block.entity.BlockEntityType.BED, 
                com.torr.materia.client.renderer.CustomBedRenderer::new);

            // Register water pot tint so it uses biome water color
            BlockColors blockColors = Minecraft.getInstance().getBlockColors();
            blockColors.register((state, reader, pos, tintIndex) -> {
                if (reader != null && pos != null) {
                    // Get biome water color at position
                    return BiomeColors.getAverageWaterColor(reader, pos);
                }
                // Default blue tint
                return 0x3F76E4;
            }, ModBlocks.WATER_POT.get());

            // Register brining vat water tint so it uses biome water color
            blockColors.register((state, reader, pos, tintIndex) -> {
                if (reader != null && pos != null) {
                    return BiomeColors.getAverageWaterColor(reader, pos);
                }
                return 0x3F76E4;
            }, ModBlocks.BRINING_VAT.get());

            // Register rubber tree leaves to use biome foliage color (like vanilla leaves)
            blockColors.register((state, reader, pos, tintIndex) -> {
                if (reader != null && pos != null) {
                    return BiomeColors.getAverageFoliageColor(reader, pos);
                }
                // Default green color for leaves
                return 0x48B518;
            }, ModBlocks.RUBBER_TREE_LEAVES.get());

            // Tint the loom fabric (#loom faces with tintindex=0) based on selected carpet color
            blockColors.register((state, reader, pos, tintIndex) -> {
                if (tintIndex != 0) return 0xFFFFFF;
                if (reader == null || pos == null) return 0xFFFFFF;
                var be = reader.getBlockEntity(pos);
                if (be instanceof FrameLoomBlockEntity loomBe) {
                    Item item = loomBe.getSelectedOutputItem();
                    if (item instanceof BlockItem bi) {
                        Block b = bi.getBlock();
                        // Vanilla carpets -> use CustomSheepColor hex values (1.18.2 has no CarpetBlock#getColor)
                        if (b == Blocks.WHITE_CARPET) return CustomSheepColor.WHITE.getColor();
                        if (b == Blocks.ORANGE_CARPET) return CustomSheepColor.ORANGE.getColor();
                        if (b == Blocks.MAGENTA_CARPET) return CustomSheepColor.MAGENTA.getColor();
                        if (b == Blocks.LIGHT_BLUE_CARPET) return CustomSheepColor.LIGHT_BLUE.getColor();
                        if (b == Blocks.YELLOW_CARPET) return CustomSheepColor.YELLOW.getColor();
                        if (b == Blocks.LIME_CARPET) return CustomSheepColor.LIME.getColor();
                        if (b == Blocks.PINK_CARPET) return CustomSheepColor.PINK.getColor();
                        if (b == Blocks.GRAY_CARPET) return CustomSheepColor.GRAY.getColor();
                        if (b == Blocks.LIGHT_GRAY_CARPET) return CustomSheepColor.LIGHT_GRAY.getColor();
                        if (b == Blocks.CYAN_CARPET) return CustomSheepColor.CYAN.getColor();
                        if (b == Blocks.PURPLE_CARPET) return CustomSheepColor.PURPLE.getColor();
                        if (b == Blocks.BLUE_CARPET) return CustomSheepColor.BLUE.getColor();
                        if (b == Blocks.BROWN_CARPET) return CustomSheepColor.BROWN.getColor();
                        if (b == Blocks.GREEN_CARPET) return CustomSheepColor.GREEN.getColor();
                        if (b == Blocks.RED_CARPET) return CustomSheepColor.RED.getColor();
                        if (b == Blocks.BLACK_CARPET) return CustomSheepColor.BLACK.getColor();
                        // Custom carpets
                        if (b == ModBlocks.OCHRE_CARPET.get()) return 0xB87E14;        // warm ochre
                        if (b == ModBlocks.RED_OCHRE_CARPET.get()) return 0xB84018;    // red ochre
                        if (b == ModBlocks.OLIVE_CARPET.get()) return 0x808000;        // olive
                        if (b == ModBlocks.INDIGO_CARPET.get()) return 0x4D3CB0;       // indigo
                        if (b == ModBlocks.LAVENDER_CARPET.get()) return 0xB19CD9;     // lavender
                        if (b == ModBlocks.TYRIAN_PURPLE_CARPET.get()) return 0x66023C;// tyrian purple
                        if (b == ModBlocks.CHARCOAL_GRAY_CARPET.get()) return 0x363332;// charcoal gray
                        if (b == ModBlocks.TAUPE_CARPET.get()) return 0xCFC1B0;        // taupe
                    }
                }
                return 0xFFFFFF;
            }, ModBlocks.FRAME_LOOM.get());

            // Register item color for rubber tree leaves (for inventory display)
            ItemColors itemColors = Minecraft.getInstance().getItemColors();
            itemColors.register((stack, tintIndex) -> {
                // Use default foliage color for items (no biome context)
                return 0x48B518;
            }, ModBlocks.RUBBER_TREE_LEAVES.get());

            // Entity renderers
            net.minecraft.client.renderer.entity.EntityRenderers.register(
                ModEntities.ROCK_PROJECTILE.get(), ThrownItemRenderer::new);
            net.minecraft.client.renderer.entity.EntityRenderers.register(
                ModEntities.FLINT_SPEAR_PROJECTILE.get(),
                FlintSpearRenderer::new);
            net.minecraft.client.renderer.entity.EntityRenderers.register(
                ModEntities.FALLING_AMPHORA.get(),
                FallingAmphoraRenderer::new);
            net.minecraft.client.renderer.entity.EntityRenderers.register(
                ModEntities.DYNAMITE_PROJECTILE.get(), ThrownItemRenderer::new);
            net.minecraft.client.renderer.entity.EntityRenderers.register(
                ModEntities.BOMB_PROJECTILE.get(), ThrownItemRenderer::new);
            net.minecraft.client.renderer.entity.EntityRenderers.register(
                ModEntities.CANNONBALL_PROJECTILE.get(), ThrownItemRenderer::new);
            net.minecraft.client.renderer.entity.EntityRenderers.register(
                ModEntities.CANNON_TNT_PROJECTILE.get(), ThrownItemRenderer::new);
            net.minecraft.client.renderer.entity.EntityRenderers.register(
                ModEntities.CANISTER_SHOT_PROJECTILE.get(), ThrownItemRenderer::new);
            net.minecraft.client.renderer.entity.EntityRenderers.register(
                ModEntities.SHRAPNEL_PROJECTILE.get(), ThrownItemRenderer::new);
            net.minecraft.client.renderer.entity.EntityRenderers.register(
                ModEntities.METAL_ARROW.get(), MetalArrowRenderer::new);

            // Sheep layer is now added via EntityRenderersEvent.AddLayers in ClientRenderLayers
            
            // Initialize conditional translation system
            materia.LOGGER.info("Initializing conditional translation system");
            ConditionalTranslationManager.getInstance();
        });
    }
}