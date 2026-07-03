from pathlib import Path

ROOT = Path(r"C:/MCMods/Materia")
SRC_BASE = ROOT / "1.20.1/src/main/java/com/torr/materia"

NEW_FILES = [
    "AgaveBlock.java",
    "block/FigTreeLeavesBlock.java",
    "block/FigTreeSaplingBlock.java",
    "block/CedarLeavesBlock.java",
    "block/CedarSaplingBlock.java",
    "block/EucalyptusSaplingBlock.java",
    "block/RainbowEucalyptusSaplingBlock.java",
    "world/feature/FigFoliagePlacer.java",
    "world/feature/EucalyptusTreeFeature.java",
    "world/feature/EucalyptusGroveFeature.java",
    "world/tree/FigTreeGrower.java",
    "world/tree/CedarTreeGrower.java",
    "world/tree/EucalyptusTreeGrower.java",
    "recipe/VerdigrisRecipe.java",
]

FIG_GROWER_120 = Path(SRC_BASE / "world/tree/FigTreeGrower.java").read_text(encoding="utf-8")

def write_file(base: Path, rel: str, text: str):
    p = base / rel.replace("/", "\\") if False else base / rel
    p = base / Path(rel)
    p.parent.mkdir(parents=True, exist_ok=True)
    p.write_text(text, encoding="utf-8")

def fig_foliage_118(text: str) -> str:
    text = text.replace("import net.minecraft.util.RandomSource;\n", "")
    if "java.util.function.BiConsumer" not in text:
        text = text.replace(
            "import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;\n",
            "import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;\n\nimport java.util.Random;\nimport java.util.function.BiConsumer;\n",
        )
    old_sig = "protected void placeLeavesRow(LevelSimulatedReader level, FoliageSetter setter, RandomSource random,"
    if old_sig in text:
        start = text.index("@Override\n    protected void placeLeavesRow")
        end = text.index("}\n", start) + 2
        # replace whole method block until closing of class - use simpler replace
        import re
        text = re.sub(
            r"@Override\s+protected void placeLeavesRow\(LevelSimulatedReader level, FoliageSetter setter, RandomSource random,[\s\S]*?super\.placeLeavesRow\(level, wrappedSetter, random, config, pos, radius, y, giantTrunk\);\s+\}",
            """@Override
    protected void placeLeavesRow(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, Random random, TreeConfiguration config, BlockPos pos, int radius, int y, boolean giantTrunk) {
        super.placeLeavesRow(level, (blockPos, blockState) -> {
            if (blockState.is(ModBlocks.FIG_LEAVES.get()) && random.nextFloat() < 0.5f) {
                blockSetter.accept(blockPos, blockState.setValue(FigTreeLeavesBlock.HAS_FIGS, true));
            } else {
                blockSetter.accept(blockPos, blockState);
            }
        }, random, config, pos, radius, y, giantTrunk);
    }""",
            text,
            count=1,
        )
    return text

def fig_leaves_118(text: str) -> str:
    text = text.replace("import net.minecraft.util.RandomSource;\n", "")
    text = text.replace("RandomSource random", "Random random")
    if "import java.util.Random;" not in text:
        text = text.replace(
            "import net.minecraft.world.phys.BlockHitResult;\n",
            "import net.minecraft.world.phys.BlockHitResult;\n\nimport java.util.Random;\n",
        )
    return text

def random_api(text: str, use_random_source: bool) -> str:
    if use_random_source:
        return text
    text = text.replace("import net.minecraft.util.RandomSource;\n", "")
    text = text.replace("RandomSource random", "Random random")
    if "Eucalyptus" in text and "import java.util.Random;" not in text:
        text = text.replace("import com.mojang.serialization.Codec;\n", "import com.mojang.serialization.Codec;\n\nimport java.util.Random;\n")
    return text

def verdigris_118(text: str) -> str:
    text = text.replace("import net.minecraft.core.RegistryAccess;\n", "")
    text = text.replace("import net.minecraft.world.item.crafting.CraftingBookCategory;\n", "")
    text = text.replace("public VerdigrisRecipe(ResourceLocation id, CraftingBookCategory category) {\n        super(id, category);",
                        "public VerdigrisRecipe(ResourceLocation id) {\n        super(id);")
    text = text.replace("@NotNull CraftingContainer inv, @NotNull RegistryAccess registryAccess", "@NotNull CraftingContainer inv")
    text = text.replace("@NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess)", "@NotNull ItemStack getResultItem()")
    # remove duplicate getResultItem override body if only used for registry
    return text

def tree_grower_118(name: str, normal_key: str, mega_key: str | None = None) -> str:
    if mega_key:
        return f'''package com.torr.materia.world.tree;

import com.torr.materia.world.feature.ModConfiguredFeatures;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import java.util.Random;

public class {name} extends AbstractMegaTreeGrower {{
    @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random random, boolean bees) {{
        return ModConfiguredFeatures.{normal_key}.getHolder().orElse(null);
    }}

    @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredMegaFeature(Random random) {{
        return ModConfiguredFeatures.{mega_key}.getHolder().orElse(null);
    }}
}}
'''
    return f'''package com.torr.materia.world.tree;

import com.torr.materia.world.feature.ModConfiguredFeatures;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import java.util.Random;

public class {name} extends AbstractTreeGrower {{
    @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random random, boolean largeHive) {{
        return ModConfiguredFeatures.{normal_key}.getHolder().orElse(null);
    }}
}}
'''

def tree_grower_192(name: str, normal_key: str, mega_key: str | None = None) -> str:
    if mega_key:
        return f'''package com.torr.materia.world.tree;

import com.torr.materia.world.feature.ModConfiguredFeatures;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;

public class {name} extends AbstractMegaTreeGrower {{
    @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean bees) {{
        return ModConfiguredFeatures.{normal_key}.getHolder().orElse(null);
    }}

    @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource random) {{
        return ModConfiguredFeatures.{mega_key}.getHolder().orElse(null);
    }}
}}
'''
    return f'''package com.torr.materia.world.tree;

import com.torr.materia.world.feature.ModConfiguredFeatures;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;

public class {name} extends AbstractTreeGrower {{
    @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean largeHive) {{
        return ModConfiguredFeatures.{normal_key}.getHolder().orElse(null);
    }}
}}
'''

def tree_grower_211(name: str, id_suffix: str, mega: bool = False) -> str:
    if mega:
        return f'''package com.torr.materia.world.tree;

import com.torr.materia.materia;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Optional;

public final class {name} {{
    private {name}() {{}}

    public static final ResourceKey<ConfiguredFeature<?, ?>> {id_suffix.upper()} = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "{id_suffix}")
    );
    public static final ResourceKey<ConfiguredFeature<?, ?>> {id_suffix.upper()}_MEGA = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "{id_suffix}_mega")
    );

    public static final TreeGrower GROWER = new TreeGrower(
            materia.MOD_ID + ":{id_suffix}",
            0.0F,
            Optional.of({id_suffix.upper()}_MEGA),
            Optional.empty(),
            Optional.of({id_suffix.upper()}),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
    );
}}
'''
    return f'''package com.torr.materia.world.tree;

import com.torr.materia.materia;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Optional;

public final class {name} {{
    private {name}() {{}}

    public static final ResourceKey<ConfiguredFeature<?, ?>> {id_suffix.upper()} = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "{id_suffix}")
    );

    public static final TreeGrower GROWER = new TreeGrower(
            materia.MOD_ID + ":{id_suffix}",
            Optional.empty(),
            Optional.of({id_suffix.upper()}),
            Optional.empty()
    );
}}
'''

def sapling_211(class_name: str, grower_class: str) -> str:
    return f'''package com.torr.materia.block;

import com.torr.materia.world.tree.{grower_class};
import net.minecraft.world.level.block.SaplingBlock;

public class {class_name} extends SaplingBlock {{
    public {class_name}(Properties properties) {{
        super({grower_class}.GROWER, properties);
    }}
}}
'''

def copy_new_files(version: str):
    base = ROOT / version / "src/main/java/com/torr/materia"
    for rel in NEW_FILES:
        src_text = (SRC_BASE / rel).read_text(encoding="utf-8")
        if version == "1.18.2":
            if rel.endswith("FigTreeLeavesBlock.java"):
                src_text = fig_leaves_118(src_text)
            elif rel.endswith("FigFoliagePlacer.java"):
                src_text = fig_foliage_118(src_text)
            elif "Eucalyptus" in rel and rel.endswith(".java"):
                src_text = random_api(src_text, False)
            elif rel.endswith("VerdigrisRecipe.java"):
                src_text = verdigris_118(src_text)
            elif rel == "world/tree/FigTreeGrower.java":
                src_text = tree_grower_118("FigTreeGrower", "FIG_TREE")
            elif rel == "world/tree/EucalyptusTreeGrower.java":
                src_text = tree_grower_118("EucalyptusTreeGrower", "EUCALYPTUS_TREE")
            elif rel == "world/tree/CedarTreeGrower.java":
                src_text = tree_grower_118("CedarTreeGrower", "CEDAR_TREE", "CEDAR_MEGA_TREE")
        elif version == "1.19.2":
            if rel == "world/tree/FigTreeGrower.java":
                src_text = tree_grower_192("FigTreeGrower", "FIG_TREE")
            elif rel == "world/tree/EucalyptusTreeGrower.java":
                src_text = tree_grower_192("EucalyptusTreeGrower", "EUCALYPTUS_TREE")
            elif rel == "world/tree/CedarTreeGrower.java":
                src_text = tree_grower_192("CedarTreeGrower", "CEDAR_TREE", "CEDAR_MEGA_TREE")
        elif version == "1.21.1":
            if rel == "world/tree/FigTreeGrower.java":
                src_text = tree_grower_211("FigTreeGrower", "fig_tree")
            elif rel == "world/tree/EucalyptusTreeGrower.java":
                src_text = tree_grower_211("EucalyptusTreeGrower", "eucalyptus_tree")
            elif rel == "world/tree/CedarTreeGrower.java":
                src_text = tree_grower_211("CedarTreeGrower", "cedar", mega=True)
            elif rel == "block/FigTreeSaplingBlock.java":
                src_text = sapling_211("FigTreeSaplingBlock", "FigTreeGrower")
            elif rel == "block/CedarSaplingBlock.java":
                src_text = sapling_211("CedarSaplingBlock", "CedarTreeGrower")
            elif rel == "block/EucalyptusSaplingBlock.java":
                src_text = sapling_211("EucalyptusSaplingBlock", "EucalyptusTreeGrower")
            elif rel == "block/RainbowEucalyptusSaplingBlock.java":
                src_text = sapling_211("RainbowEucalyptusSaplingBlock", "EucalyptusTreeGrower").replace(
                    "public class RainbowEucalyptusSaplingBlock",
                    "/** Rainbow eucalyptus saplings grow the same single-tree feature; logs/leaves tint at gen time in groves. */\npublic class RainbowEucalyptusSaplingBlock",
                )
        write_file(base, rel, src_text)
    print(f"Copied new files -> {version}")

for v in ("1.18.2", "1.19.2", "1.21.1"):
    copy_new_files(v)
