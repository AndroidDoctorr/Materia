from pathlib import Path

ROOT = Path(r"C:/MCMods/Materia")

def write_118_growers():
    base = ROOT / "1.18.2/src/main/java/com/torr/materia/world/tree"
    base.mkdir(parents=True, exist_ok=True)
    fig = '''package com.torr.materia.world.tree;

import com.torr.materia.world.feature.ModConfiguredFeatures;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import java.util.Random;

public class FigTreeGrower extends AbstractTreeGrower {
    @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random random, boolean largeHive) {
        return ModConfiguredFeatures.FIG_TREE.getHolder().orElse(null);
    }
}
'''
    euc = fig.replace("FigTreeGrower", "EucalyptusTreeGrower").replace("FIG_TREE", "EUCALYPTUS_TREE")
    cedar = '''package com.torr.materia.world.tree;

import com.torr.materia.world.feature.ModConfiguredFeatures;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import java.util.Random;

public class CedarTreeGrower extends AbstractMegaTreeGrower {
    @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random random, boolean bees) {
        return ModConfiguredFeatures.CEDAR_TREE.getHolder().orElse(null);
    }

    @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredMegaFeature(Random random) {
        return ModConfiguredFeatures.CEDAR_MEGA_TREE.getHolder().orElse(null);
    }
}
'''
    for name, content in [("FigTreeGrower.java", fig), ("EucalyptusTreeGrower.java", euc), ("CedarTreeGrower.java", cedar)]:
        (base / name).write_text(content, encoding="utf-8")

def fix_eucalyptus_can_replace(ver):
    p = ROOT / ver / "src/main/java/com/torr/materia/world/feature/EucalyptusTreeFeature.java"
    t = p.read_text(encoding="utf-8")
    t = t.replace("state.isAir() || state.canBeReplaced()", "state.isAir() || state.getMaterial().isReplaceable()")
    p.write_text(t, encoding="utf-8")

def fix_fig_foliage_192():
    p = ROOT / "1.19.2/src/main/java/com/torr/materia/world/feature/FigFoliagePlacer.java"
    import re
    t = p.read_text(encoding="utf-8")
    t = re.sub(
        r"@Override\s+protected void placeLeavesRow\(LevelSimulatedReader level, FoliageSetter setter, RandomSource random,[\s\S]*?super\.placeLeavesRow\(level, wrappedSetter, random, config, pos, radius, y, giantTrunk\);\s+\}",
        """@Override
    protected void placeLeavesRow(LevelSimulatedReader level, java.util.function.BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, TreeConfiguration config, BlockPos pos, int radius, int y, boolean giantTrunk) {
        super.placeLeavesRow(level, (blockPos, blockState) -> {
            if (blockState.is(ModBlocks.FIG_LEAVES.get()) && random.nextFloat() < 0.5f) {
                blockSetter.accept(blockPos, blockState.setValue(FigTreeLeavesBlock.HAS_FIGS, true));
            } else {
                blockSetter.accept(blockPos, blockState);
            }
        }, random, config, pos, radius, y, giantTrunk);
    }""",
        t,
        count=1,
    )
    p.write_text(t, encoding="utf-8")

def fix_verdigris_192():
    src = (ROOT / "1.18.2/src/main/java/com/torr/materia/recipe/VerdigrisRecipe.java").read_text(encoding="utf-8")
    (ROOT / "1.19.2/src/main/java/com/torr/materia/recipe/VerdigrisRecipe.java").write_text(src, encoding="utf-8")

def burgundy_blocks_211():
    p = ROOT / "1.21.1/src/main/java/com/torr/materia/ModBlocks.java"
    t = p.read_text(encoding="utf-8")
    if "BURGUNDY_WOOL" in t:
        return
    ins = """
        // BURGUNDY BLOCKS
        public static final RegistryObject<Block> BURGUNDY_WOOL = registerBlock("burgundy_wool",
                        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_WOOL)));
        public static final RegistryObject<Block> BURGUNDY_GLASS = registerBlock("burgundy_glass",
                        () -> new net.minecraft.world.level.block.StainedGlassBlock(DyeColor.RED, BlockBehaviour.Properties.ofFullCopy(Blocks.RED_STAINED_GLASS).noOcclusion()));
        public static final RegistryObject<Block> BURGUNDY_CONCRETE = registerBlock("burgundy_concrete",
                        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_CONCRETE)));
        public static final RegistryObject<Block> BURGUNDY_CONCRETE_POWDER = registerBlock("burgundy_concrete_powder",
                        () -> new net.minecraft.world.level.block.ConcretePowderBlock(ModBlocks.BURGUNDY_CONCRETE.get(),
                                        BlockBehaviour.Properties.ofFullCopy(Blocks.RED_CONCRETE_POWDER)));
        public static final RegistryObject<Block> BURGUNDY_TERRACOTTA = registerBlock("burgundy_terracotta",
                        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_TERRACOTTA)));
        public static final RegistryObject<Block> BURGUNDY_CANDLE = registerBlock("burgundy_candle",
                        () -> new net.minecraft.world.level.block.CandleBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_CANDLE)));

        // TAN BLOCKS
        public static final RegistryObject<Block> TAN_WOOL = registerBlock("tan_wool",
                        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_WOOL)));
        public static final RegistryObject<Block> TAN_GLASS = registerBlock("tan_glass",
                        () -> new net.minecraft.world.level.block.StainedGlassBlock(DyeColor.BROWN, BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_STAINED_GLASS).noOcclusion()));
        public static final RegistryObject<Block> TAN_CONCRETE = registerBlock("tan_concrete",
                        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_CONCRETE)));
        public static final RegistryObject<Block> TAN_CONCRETE_POWDER = registerBlock("tan_concrete_powder",
                        () -> new net.minecraft.world.level.block.ConcretePowderBlock(ModBlocks.TAN_CONCRETE.get(),
                                        BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_CONCRETE_POWDER)));
        public static final RegistryObject<Block> TAN_TERRACOTTA = registerBlock("tan_terracotta",
                        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_TERRACOTTA)));
        public static final RegistryObject<Block> TAN_CANDLE = registerBlock("tan_candle",
                        () -> new net.minecraft.world.level.block.CandleBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_CANDLE)));

"""
    t = t.replace(
        """        public static final RegistryObject<Block> OLIVE_CARPET = registerBlock("olive_carpet",
                        () -> new CarpetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GREEN_CARPET)));

        // INDIGO BLOCKS""",
        """        public static final RegistryObject<Block> OLIVE_CARPET = registerBlock("olive_carpet",
                        () -> new CarpetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GREEN_CARPET)));
""" + ins + """
        // INDIGO BLOCKS""",
    )
    p.write_text(t, encoding="utf-8")

def fix_moditems_clumps_211():
    p = ROOT / "1.21.1/src/main/java/com/torr/materia/ModItems.java"
    t = p.read_text(encoding="utf-8")
    if "CLUMP_OF_BURGUNDY_WOOL" in t:
        return
    t = t.replace(
        """        public static final RegistryObject<Item> CLUMP_OF_OLIVE_WOOL = ITEMS.register("clump_of_olive_wool",
                        () -> new Item(new Item.Properties()
                                        ));
        public static final RegistryObject<Item> CLUMP_OF_OCHRE_WOOL = ITEMS.register("clump_of_ochre_wool",""",
        """        public static final RegistryObject<Item> CLUMP_OF_OLIVE_WOOL = ITEMS.register("clump_of_olive_wool",
                        () -> new Item(new Item.Properties()
                                        ));
        public static final RegistryObject<Item> CLUMP_OF_BURGUNDY_WOOL = ITEMS.register("clump_of_burgundy_wool",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> CLUMP_OF_TAN_WOOL = ITEMS.register("clump_of_tan_wool",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> CLUMP_OF_OCHRE_WOOL = ITEMS.register("clump_of_ochre_wool",""",
    )
    t = t.replace(
        """        public static final RegistryObject<Item> OLIVE_STRING = ITEMS.register("olive_string",
                        () -> new Item(new Item.Properties()
                                        ));
        public static final RegistryObject<Item> OCHRE_STRING = ITEMS.register("ochre_string",""",
        """        public static final RegistryObject<Item> OLIVE_STRING = ITEMS.register("olive_string",
                        () -> new Item(new Item.Properties()
                                        ));
        public static final RegistryObject<Item> BURGUNDY_STRING = ITEMS.register("burgundy_string",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> TAN_STRING = ITEMS.register("tan_string",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> OCHRE_STRING = ITEMS.register("ochre_string",""",
    )
    p.write_text(t, encoding="utf-8")

def fix_agave_211():
    p = ROOT / "1.21.1/src/main/java/com/torr/materia/AgaveBlock.java"
    p.write_text('''package com.torr.materia;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

public class AgaveBlock extends BushBlock {
    public static final MapCodec<AgaveBlock> CODEC = simpleCodec(AgaveBlock::new);

    public AgaveBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(Blocks.COARSE_DIRT)
                || state.is(Blocks.SAND) || state.is(Blocks.RED_SAND);
    }
}
''', encoding="utf-8")

def fix_fig_leaves_211():
    src = (ROOT / "1.21.1/src/main/java/com/torr/materia/block/OliveTreeLeavesBlock.java").read_text(encoding="utf-8")
    t = src.replace("OliveTreeLeavesBlock", "FigTreeLeavesBlock").replace("HAS_OLIVES", "HAS_FIGS").replace("ModItems.OLIVES.get()", "ModItems.FIG.get()").replace("1 + level.random.nextInt(3)", "1 + level.random.nextInt(2)")
    (ROOT / "1.21.1/src/main/java/com/torr/materia/block/FigTreeLeavesBlock.java").write_text(t, encoding="utf-8")

def fix_verdigris_211():
    p = ROOT / "1.21.1/src/main/java/com/torr/materia/recipe/VerdigrisRecipe.java"
    p.write_text('''package com.torr.materia.recipe;

import com.torr.materia.ModItems;
import com.torr.materia.ModRecipes;
import com.torr.materia.materia;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class VerdigrisRecipe extends CustomRecipe {
    private static final TagKey<Item> VINEGAR_TAG = ItemTags.create(materia.id("vinegar"));
    private static final TagKey<Item> RAW_COPPER_TAG = ItemTags.create(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("forge", "raw_materials/copper"));

    public VerdigrisRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput inv, @NotNull Level level) {
        boolean hasVinegar = false;
        boolean hasCopper = false;
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.is(VINEGAR_TAG)) {
                if (hasVinegar) return false;
                hasVinegar = true;
            } else if (stack.is(RAW_COPPER_TAG) || stack.is(Items.RAW_COPPER)) {
                if (hasCopper) return false;
                hasCopper = true;
            } else {
                return false;
            }
        }
        return hasVinegar && hasCopper;
    }

    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registryAccess) {
        return new ItemStack(ModItems.VERDIGRIS.get());
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingInput inv) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(inv.size(), ItemStack.EMPTY);
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.is(VINEGAR_TAG)) {
                remaining.set(i, getEmptyVessel(stack));
            }
        }
        return remaining;
    }

    private static ItemStack getEmptyVessel(ItemStack vinegarStack) {
        Item item = vinegarStack.getItem();
        if (item == ModItems.VINEGAR.get()) {
            return new ItemStack(ModItems.CLAY_BOWL.get());
        }
        if (item == ModItems.VINEGAR_BOTTLE.get()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        }
        if (item == ModItems.VINEGAR_POT.get()) {
            return new ItemStack(ModItems.POT.get());
        }
        if (item == ModItems.VINEGAR_BUCKET.get()) {
            return new ItemStack(Items.BUCKET);
        }
        if (item == ModItems.CRUCIBLE.get().asItem()) {
            return new ItemStack(ModItems.CRUCIBLE.get());
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.VERDIGRIS_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }
}
''', encoding="utf-8")

def fix_modrecipes_import_211():
    p = ROOT / "1.21.1/src/main/java/com/torr/materia/ModRecipes.java"
    t = p.read_text(encoding="utf-8")
    if "import com.torr.materia.recipe.VerdigrisRecipe;" not in t:
        t = t.replace(
            "import com.torr.materia.recipe.LeatherArmorDyeRecipe;\n",
            "import com.torr.materia.recipe.LeatherArmorDyeRecipe;\nimport com.torr.materia.recipe.VerdigrisRecipe;\n",
        )
        p.write_text(t, encoding="utf-8")

write_118_growers()
fix_eucalyptus_can_replace("1.18.2")
fix_eucalyptus_can_replace("1.19.2")
fix_fig_foliage_192()
fix_verdigris_192()
burgundy_blocks_211()
fix_moditems_clumps_211()
fix_agave_211()
fix_fig_leaves_211()
fix_verdigris_211()
fix_modrecipes_import_211()
print("compile fixes applied")
