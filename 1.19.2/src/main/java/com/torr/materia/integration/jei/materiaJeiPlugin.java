package com.torr.materia.integration.jei;

import com.torr.materia.ModBlocks;
import com.torr.materia.ModItems;
import com.torr.materia.ModRecipes;
import com.torr.materia.materia;
import com.torr.materia.integration.jei.coke_oven.CokeOvenJeiCategory;
import com.torr.materia.integration.jei.coke_oven.CokeOvenJeiRecipe;
import com.torr.materia.integration.jei.drying_rack.DryingRackJeiCategory;
import com.torr.materia.integration.jei.drying_rack.DryingRackJeiRecipe;
import com.torr.materia.integration.jei.flint_knife.FlintKnifeJeiCategory;
import com.torr.materia.integration.jei.flint_knife.FlintKnifeJeiRecipe;
import com.torr.materia.integration.jei.fire_pit.FirePitJeiCategory;
import com.torr.materia.integration.jei.frame_loom.FrameLoomJeiCategory;
import com.torr.materia.integration.jei.frame_loom.FrameLoomJeiRecipe;
import com.torr.materia.integration.jei.hewing.HewingJeiCategory;
import com.torr.materia.integration.jei.hewing.HewingJeiRecipe;
import com.torr.materia.integration.jei.oven.OvenJeiCategory;
import com.torr.materia.integration.jei.primitive.PrimitiveCraftingJeiCategory;
import com.torr.materia.integration.jei.primitive.PrimitiveCraftingJeiRecipe;
import com.torr.materia.integration.jei.water_pot.WaterPotJeiCategory;
import com.torr.materia.integration.jei.water_pot.WaterPotJeiRecipe;
import com.torr.materia.recipe.AdvancedKilnRecipe;
import com.torr.materia.recipe.BronzeAnvilRecipe;
import com.torr.materia.recipe.IronAnvilRecipe;
import com.torr.materia.recipe.KilnRecipe;
import com.torr.materia.recipe.OvenRecipe;
import com.torr.materia.recipe.StoneAnvilRecipe;
import com.torr.materia.recipe.WaterPotRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

@JeiPlugin
public class materiaJeiPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_ID = new ResourceLocation(materia.MOD_ID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();

        registration.addRecipeCategories(
                new KilnJeiCategory(guiHelper),
                new AdvancedKilnJeiCategory(guiHelper),
                new OvenJeiCategory(guiHelper),
                new CokeOvenJeiCategory(guiHelper),
                new StoneAnvilJeiCategory(guiHelper),
                new BronzeAnvilJeiCategory(guiHelper),
                new IronAnvilJeiCategory(guiHelper),
                new HewingJeiCategory(guiHelper),
                new PrimitiveCraftingJeiCategory(guiHelper),
                new FlintKnifeJeiCategory(guiHelper),
                new FrameLoomJeiCategory(guiHelper),
                new WaterPotJeiCategory(guiHelper),
                new DryingRackJeiCategory(guiHelper),
                new FirePitJeiCategory(guiHelper)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var level = Minecraft.getInstance().level;
        if (level == null) return;

        RecipeManager recipeManager = level.getRecipeManager();

        List<KilnRecipe> kiln = recipeManager.getAllRecipesFor(ModRecipes.KILN_TYPE.get());
        List<AdvancedKilnRecipe> advancedKiln = recipeManager.getAllRecipesFor(ModRecipes.ADVANCED_KILN_TYPE.get());
        List<OvenRecipe> oven = recipeManager.getAllRecipesFor(ModRecipes.OVEN_TYPE.get());
        List<StoneAnvilRecipe> stoneAnvil = recipeManager.getAllRecipesFor(ModRecipes.STONE_ANVIL_TYPE.get());
        List<BronzeAnvilRecipe> bronzeAnvil = recipeManager.getAllRecipesFor(ModRecipes.BRONZE_ANVIL_TYPE.get());
        List<IronAnvilRecipe> ironAnvil = recipeManager.getAllRecipesFor(ModRecipes.IRON_ANVIL_TYPE.get());

        registration.addRecipes(materiaJeiRecipeTypes.FIRE_PIT,
                recipeManager.getAllRecipesFor(ModRecipes.FIRE_PIT_TYPE.get()));

        registration.addRecipes(materiaJeiRecipeTypes.KILN, kiln);
        registration.addRecipes(materiaJeiRecipeTypes.ADVANCED_KILN, advancedKiln);
        registration.addRecipes(materiaJeiRecipeTypes.OVEN, oven);
        ItemStack coalCokeForJei = new ItemStack(ModItems.COAL_COKE.get());
        Ingredient vanillaCoalOnly = Ingredient.of(Items.COAL);
        List<CokeOvenJeiRecipe> cokeOvenRecipes = List.of(
                new CokeOvenJeiRecipe(vanillaCoalOnly, Ingredient.of(Items.COAL), coalCokeForJei),
                new CokeOvenJeiRecipe(vanillaCoalOnly, Ingredient.of(Items.CHARCOAL), coalCokeForJei)
        );
        registration.addRecipes(materiaJeiRecipeTypes.COKE_OVEN, cokeOvenRecipes);
        registration.addRecipes(materiaJeiRecipeTypes.STONE_ANVIL, stoneAnvil);
        registration.addRecipes(materiaJeiRecipeTypes.BRONZE_ANVIL, bronzeAnvil);
        registration.addRecipes(materiaJeiRecipeTypes.IRON_ANVIL, ironAnvil);

        // JEI-only helper category: Flint Knife assembly
        FlintKnifeJeiRecipe flintKnife = new FlintKnifeJeiRecipe(
                Ingredient.of(ModItems.KNAPPED_FLINT.get()),
                Ingredient.of(ModItems.BONE_HANDLE.get()),
                Ingredient.of(ModItems.LASHING.get(), ModItems.GLUE.get()),
                new ItemStack(ModItems.FLINT_KNIFE.get())
        );
        registration.addRecipes(materiaJeiRecipeTypes.FLINT_KNIFE, List.of(flintKnife));

        // Custom JEI displays for "special" crafting that JEI won't reliably list:
        // - Hewing (log + axe -> rough planks, output depends on log)
        // - Primitive crafting (rock + hammer + cutting tool + crucible -> primitive table)
        Ingredient basicAxes = Ingredient.of(ItemTags.create(new ResourceLocation(materia.MOD_ID, "basic_axes")));

        List<HewingJeiRecipe> hewing = List.of(
                new HewingJeiRecipe(Ingredient.of(Items.OAK_LOG), basicAxes, new ItemStack(ModItems.ROUGH_OAK_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(Items.SPRUCE_LOG), basicAxes, new ItemStack(ModItems.ROUGH_SPRUCE_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(Items.BIRCH_LOG), basicAxes, new ItemStack(ModItems.ROUGH_BIRCH_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(Items.JUNGLE_LOG), basicAxes, new ItemStack(ModItems.ROUGH_JUNGLE_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(Items.ACACIA_LOG), basicAxes, new ItemStack(ModItems.ROUGH_ACACIA_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(Items.DARK_OAK_LOG), basicAxes, new ItemStack(ModItems.ROUGH_DARK_OAK_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(Items.MANGROVE_LOG), basicAxes, new ItemStack(ModItems.ROUGH_MANGROVE_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(Items.CRIMSON_STEM), basicAxes, new ItemStack(ModItems.ROUGH_CRIMSON_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(Items.WARPED_STEM), basicAxes, new ItemStack(ModItems.ROUGH_WARPED_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(ModBlocks.RUBBER_WOOD_LOG.get().asItem()), basicAxes, new ItemStack(ModItems.ROUGH_RUBBER_WOOD_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(ModBlocks.TAPPED_RUBBER_WOOD_LOG.get().asItem()), basicAxes, new ItemStack(ModItems.ROUGH_RUBBER_WOOD_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(ModBlocks.SAPPED_SPRUCE_LOG.get().asItem()), basicAxes, new ItemStack(ModItems.ROUGH_SPRUCE_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(ModBlocks.OLIVE_TREE_LOG.get().asItem()), basicAxes, new ItemStack(ModItems.ROUGH_OAK_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(ModBlocks.PALM_LOG.get().asItem()), basicAxes, new ItemStack(ModItems.ROUGH_OAK_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(ModBlocks.CYPRESS_LOG.get().asItem()), basicAxes, new ItemStack(ModItems.ROUGH_OAK_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(ModBlocks.BAOBAB_LOG.get().asItem()), basicAxes, new ItemStack(ModItems.ROUGH_OAK_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(ModBlocks.MAPLE_LOG.get().asItem()), basicAxes, new ItemStack(ModItems.ROUGH_BIRCH_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(ModBlocks.FIG_LOG.get().asItem()), basicAxes, new ItemStack(ModItems.ROUGH_FIG_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(ModBlocks.TAPPED_FIG_LOG.get().asItem()), basicAxes, new ItemStack(ModItems.ROUGH_FIG_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(ModBlocks.CEDAR_LOG.get().asItem()), basicAxes, new ItemStack(ModItems.ROUGH_CEDAR_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(ModBlocks.EUCALYPTUS_LOG.get().asItem()), basicAxes, new ItemStack(ModItems.ROUGH_EUCALYPTUS_PLANK.get(), 4)),
                new HewingJeiRecipe(Ingredient.of(ModBlocks.RAINBOW_EUCALYPTUS_LOG.get().asItem()), basicAxes, new ItemStack(ModItems.ROUGH_EUCALYPTUS_PLANK.get(), 4))

        );

        registration.addRecipes(materiaJeiRecipeTypes.HEWING, hewing);

        PrimitiveCraftingJeiRecipe primitive = new PrimitiveCraftingJeiRecipe(
                Ingredient.of(ModBlocks.ROCK.get().asItem()),
                Ingredient.of(ModItems.STONE_HAMMER.get(), ModItems.HAMMER_STONE.get()),
                Ingredient.of(ModItems.FLINT_KNIFE.get(), ModItems.KNAPPED_FLINT.get()),
                Ingredient.of(ModItems.CRUCIBLE.get()),
                new ItemStack(ModBlocks.PRIMITIVE_CRAFTING_TABLE.get().asItem())
        );
        registration.addRecipes(materiaJeiRecipeTypes.PRIMITIVE_CRAFTING, List.of(primitive));

        // Frame Loom (8x string -> carpet; output depends on string color)
        List<FrameLoomJeiRecipe> frameLoom = List.of(
                new FrameLoomJeiRecipe(List.of(new ItemStack(Items.STRING, 8)), new ItemStack(ModBlocks.TAUPE_CARPET.get())),
                new FrameLoomJeiRecipe(List.of(new ItemStack(ModItems.LASHING.get(), 8)), new ItemStack(ModBlocks.TAUPE_CARPET.get())),

                new FrameLoomJeiRecipe(List.of(new ItemStack(ModItems.WHITE_STRING.get(), 8)), new ItemStack(Items.WHITE_CARPET)),
                new FrameLoomJeiRecipe(List.of(new ItemStack(ModItems.LIGHT_GRAY_STRING.get(), 8)), new ItemStack(Items.LIGHT_GRAY_CARPET)),
                new FrameLoomJeiRecipe(List.of(new ItemStack(ModItems.GRAY_STRING.get(), 8)), new ItemStack(Items.GRAY_CARPET)),
                new FrameLoomJeiRecipe(List.of(new ItemStack(ModItems.BLACK_STRING.get(), 8)), new ItemStack(Items.BLACK_CARPET)),
                new FrameLoomJeiRecipe(List.of(new ItemStack(ModItems.BROWN_STRING.get(), 8)), new ItemStack(Items.BROWN_CARPET)),
                new FrameLoomJeiRecipe(List.of(new ItemStack(ModItems.RED_STRING.get(), 8)), new ItemStack(Items.RED_CARPET)),
                new FrameLoomJeiRecipe(List.of(new ItemStack(ModItems.ORANGE_STRING.get(), 8)), new ItemStack(Items.ORANGE_CARPET)),
                new FrameLoomJeiRecipe(List.of(new ItemStack(ModItems.YELLOW_STRING.get(), 8)), new ItemStack(Items.YELLOW_CARPET)),
                new FrameLoomJeiRecipe(List.of(new ItemStack(ModItems.LIME_STRING.get(), 8)), new ItemStack(Items.LIME_CARPET)),
                new FrameLoomJeiRecipe(List.of(new ItemStack(ModItems.GREEN_STRING.get(), 8)), new ItemStack(Items.GREEN_CARPET)),
                new FrameLoomJeiRecipe(List.of(new ItemStack(ModItems.CYAN_STRING.get(), 8)), new ItemStack(Items.CYAN_CARPET)),
                new FrameLoomJeiRecipe(List.of(new ItemStack(ModItems.LIGHT_BLUE_STRING.get(), 8)), new ItemStack(Items.LIGHT_BLUE_CARPET)),
                new FrameLoomJeiRecipe(List.of(new ItemStack(ModItems.BLUE_STRING.get(), 8)), new ItemStack(Items.BLUE_CARPET)),
                new FrameLoomJeiRecipe(List.of(new ItemStack(ModItems.PURPLE_STRING.get(), 8)), new ItemStack(Items.PURPLE_CARPET)),
                new FrameLoomJeiRecipe(List.of(new ItemStack(ModItems.MAGENTA_STRING.get(), 8)), new ItemStack(Items.MAGENTA_CARPET)),
                new FrameLoomJeiRecipe(List.of(new ItemStack(ModItems.PINK_STRING.get(), 8)), new ItemStack(Items.PINK_CARPET)),

                new FrameLoomJeiRecipe(List.of(new ItemStack(ModItems.OCHRE_STRING.get(), 8)), new ItemStack(ModBlocks.OCHRE_CARPET.get())),
                new FrameLoomJeiRecipe(List.of(new ItemStack(ModItems.RED_OCHRE_STRING.get(), 8)), new ItemStack(ModBlocks.RED_OCHRE_CARPET.get())),
                new FrameLoomJeiRecipe(List.of(new ItemStack(ModItems.OLIVE_STRING.get(), 8)), new ItemStack(ModBlocks.OLIVE_CARPET.get())),
                new FrameLoomJeiRecipe(List.of(new ItemStack(ModItems.INDIGO_STRING.get(), 8)), new ItemStack(ModBlocks.INDIGO_CARPET.get())),
                new FrameLoomJeiRecipe(List.of(new ItemStack(ModItems.LAVENDER_STRING.get(), 8)), new ItemStack(ModBlocks.LAVENDER_CARPET.get())),
                new FrameLoomJeiRecipe(List.of(new ItemStack(ModItems.TYRIAN_PURPLE_STRING.get(), 8)), new ItemStack(ModBlocks.TYRIAN_PURPLE_CARPET.get())),
                new FrameLoomJeiRecipe(List.of(new ItemStack(ModItems.CHARCOAL_GRAY_STRING.get(), 8)), new ItemStack(ModBlocks.CHARCOAL_GRAY_CARPET.get()))
        );
        registration.addRecipes(materiaJeiRecipeTypes.FRAME_LOOM, frameLoom);

        // Water Pot recipes from datapack (includes cooked rice, earth→clay, etc.)
        List<WaterPotRecipe> waterPotRecipes = recipeManager.getAllRecipesFor(ModRecipes.WATER_POT_TYPE.get());
        List<WaterPotJeiRecipe> waterPot = waterPotRecipes.stream()
                .map(WaterPotJeiRecipe::fromRecipe)
                .toList();
        registration.addRecipes(materiaJeiRecipeTypes.WATER_POT, waterPot);

        // Drying Rack
        List<DryingRackJeiRecipe> dryingRack = List.of(
                new DryingRackJeiRecipe(List.of(new ItemStack(ModItems.LEATHER_STRETCHED.get())), new ItemStack(ModItems.TANNED_LEATHER_STRETCHED.get())),
                new DryingRackJeiRecipe(List.of(new ItemStack(Items.PORKCHOP)), new ItemStack(ModItems.JERKY.get())),
                new DryingRackJeiRecipe(List.of(new ItemStack(Items.BEEF)), new ItemStack(ModItems.JERKY.get())),
                new DryingRackJeiRecipe(List.of(new ItemStack(Items.MUTTON)), new ItemStack(ModItems.JERKY.get())),
                new DryingRackJeiRecipe(List.of(new ItemStack(ModItems.CLEAN_GUT.get())), new ItemStack(ModItems.CORD.get()))
        );
        registration.addRecipes(materiaJeiRecipeTypes.DRYING_RACK, dryingRack);

        registerMetalworkingJeiTips(registration);
        registerPlantJeiTips(registration);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FURNACE_KILN.get()), materiaJeiRecipeTypes.KILN);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.BLAST_FURNACE_KILN.get()), materiaJeiRecipeTypes.ADVANCED_KILN);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.OVEN.get()), materiaJeiRecipeTypes.OVEN);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.COKE_OVEN.get()), materiaJeiRecipeTypes.COKE_OVEN);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.STONE_ANVIL.get()), materiaJeiRecipeTypes.STONE_ANVIL);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.BRONZE_ANVIL.get()), materiaJeiRecipeTypes.BRONZE_ANVIL);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.IRON_ANVIL.get()), materiaJeiRecipeTypes.IRON_ANVIL);

        registration.addRecipeCatalyst(new ItemStack(ModItems.ROUGH_OAK_PLANK.get()), materiaJeiRecipeTypes.HEWING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.PRIMITIVE_CRAFTING_TABLE.get()), materiaJeiRecipeTypes.PRIMITIVE_CRAFTING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.PRIMITIVE_CRAFTING_TABLE.get()), materiaJeiRecipeTypes.FLINT_KNIFE);
        registration.addRecipeCatalyst(new ItemStack(Blocks.CRAFTING_TABLE), materiaJeiRecipeTypes.FLINT_KNIFE);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FRAME_LOOM.get()), materiaJeiRecipeTypes.FRAME_LOOM);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.WATER_POT.get()), materiaJeiRecipeTypes.WATER_POT);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.DRYING_RACK.get()), materiaJeiRecipeTypes.DRYING_RACK);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FIRE_PIT.get()), materiaJeiRecipeTypes.FIRE_PIT);
    }

    private void registerMetalworkingJeiTips(IRecipeRegistration registration) {
        registration.addIngredientInfo(
                List.of(
                        new ItemStack(ModBlocks.STONE_ANVIL.get()),
                        new ItemStack(ModBlocks.BRONZE_ANVIL.get()),
                        new ItemStack(ModBlocks.IRON_ANVIL.get())
                ),
                VanillaTypes.ITEM_STACK,
                Component.literal("Metalworking tip: anvil inputs must be hot."),
                Component.literal("Creative/testing: use /heatmetal on the held item."),
                Component.literal("Some recipes consume multiple units (for example, 9 ingots -> 1 metal block).")
        );

        registration.addIngredientInfo(
                List.of(
                        new ItemStack(Items.RAW_COPPER),
                        new ItemStack(ModItems.TIN_NUGGET.get()),
                        new ItemStack(ModItems.BRONZE_INGOT.get())
                ),
                VanillaTypes.ITEM_STACK,
                Component.literal("Bronze alloy: 1x raw copper + 1x tin nugget.")
        );

        registration.addIngredientInfo(
                List.of(
                        new ItemStack(Items.RAW_COPPER),
                        new ItemStack(ModItems.RAW_ZINC.get()),
                        new ItemStack(ModItems.BRASS_INGOT.get())
                ),
                VanillaTypes.ITEM_STACK,
                Component.literal("Brass alloy: 2x raw copper + 1x raw zinc.")
        );
    }

    private void registerPlantJeiTips(IRecipeRegistration registration) {
        registration.addIngredientInfo(
                List.of(
                        new ItemStack(ModItems.RICE_SEEDS.get()),
                        new ItemStack(ModBlocks.WILD_RICE.get().asItem()),
                        new ItemStack(ModItems.SHELLED_RICE.get())
                ),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.materia.rice")
        );

        registration.addIngredientInfo(
                List.of(
                        new ItemStack(ModItems.COTTON.get()),
                        new ItemStack(ModItems.COTTON_SEEDS.get()),
                        new ItemStack(ModBlocks.COTTON_CROP.get().asItem())
                ),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.materia.cotton")
        );

        registration.addIngredientInfo(
                List.of(
                        new ItemStack(ModItems.TEA_SEEDS.get()),
                        new ItemStack(ModBlocks.TEA_BUSH.get().asItem()),
                        new ItemStack(ModItems.TEA_LEAVES.get())
                ),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.materia.tea_bush")
        );

        registration.addIngredientInfo(
                List.of(
                        new ItemStack(ModItems.TARO.get()),
                        new ItemStack(ModItems.COOKED_TARO.get()),
                        new ItemStack(ModBlocks.TARO_CROP.get().asItem())
                ),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.materia.taro")
        );

        registration.addIngredientInfo(
                List.of(new ItemStack(ModBlocks.SPINNING_WHEEL.get())),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.materia.spinning_wheel")
        );
    }
}

