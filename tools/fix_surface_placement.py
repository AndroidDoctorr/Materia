#!/usr/bin/env python3
"""Fix surface feature placement: correct heightmaps and add air-only filters."""
from __future__ import annotations

import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

CONFIGURED_FILES = list(ROOT.glob("*/src/main/java/com/torr/materia/world/feature/ModConfiguredFeatures.java"))
BOOTSTRAP_FILES = list(ROOT.glob("*/src/main/java/com/torr/materia/datagen/worldgen/materiaWorldgenBootstrap.java"))
PLACED_FILES = list(ROOT.glob("*/src/main/java/com/torr/materia/world/feature/ModPlacedFeatures.java"))

KEEP_OCEAN_FLOOR = {
    "earth_subsoil_placed",
    "bauxite_patch_on_ore_placed",
    "lotus_placed",
    "reeds_placed",
    "wild_grape_vine_placed",
    "wild_wisteria_vine_placed",
    "wild_hops_vine_placed",
    "murex_shell_placed",
    "clam_placed",
}

IMPORTS = """import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
"""

HELPER = """
    private static java.util.List<PlacementModifier> patchBlockPlacement() {
        return java.util.List.of(BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE));
    }
"""


def patch_configured(path: Path) -> bool:
    text = path.read_text(encoding="utf-8")
    if "patchBlockPlacement()" in text:
        return False
    if "import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;" not in text:
        text = text.replace(
            "import net.minecraft.world.level.levelgen.placement.PlacedFeature;",
            "import net.minecraft.world.level.levelgen.placement.PlacedFeature;\n" + IMPORTS,
        )
    text = text.replace(
        "PlacedFeature placed = new PlacedFeature(simpleHolder, java.util.List.of());",
        "PlacedFeature placed = new PlacedFeature(simpleHolder, patchBlockPlacement());",
    )
    text = text.replace(
        "private static ConfiguredFeature<RandomPatchConfiguration, ?> randomPatch(",
        HELPER + "\n    private static ConfiguredFeature<RandomPatchConfiguration, ?> randomPatch(",
    )
    path.write_text(text, encoding="utf-8")
    return True


def patch_bootstrap(path: Path) -> bool:
    text = path.read_text(encoding="utf-8")
    if "patchBlockPlacement()" in text:
        return False
    text = text.replace(
        "PlacedFeature placed = new PlacedFeature(simpleHolder, List.of());",
        "PlacedFeature placed = new PlacedFeature(simpleHolder, patchBlockPlacement());",
    )
    text = text.replace(
        "private static ConfiguredFeature<?, ?> randomPatch(",
        HELPER + "\n    private static ConfiguredFeature<?, ?> randomPatch(",
    )

    # Heightmap fixes in bootstrap placed features
    lines = text.splitlines(keepends=True)
    out: list[str] = []
    current_register: str | None = None
    for line in lines:
        if "context.register(" in line and "_PLACED" in line:
            m = re.search(r'context\.register\((\w+)', line)
            if m:
                key = m.group(1)
                current_register = key.lower().replace("_placed", "_placed")
        if "HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR)" in line:
            register_name = None
            for prev in reversed(out[-8:]):
                m = re.search(r'register\("([^"]+)"', prev)
                if m:
                    register_name = m.group(1)
                    break
                m2 = re.search(r"context\.register\((\w+)", prev)
                if m2:
                    const = m2.group(1).lower()
                    register_name = const.replace("_placed", "_placed")
                    break
            if register_name not in KEEP_OCEAN_FLOOR:
                line = line.replace(
                    "Heightmap.Types.OCEAN_FLOOR",
                    "Heightmap.Types.MOTION_BLOCKING_NO_LEAVES",
                )
        out.append(line)
    text = "".join(out)

    # Surface rock air filter in bootstrap
    text = text.replace(
        "BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.ROCK.get().defaultBlockState(), BlockPos.ZERO)),",
        "BlockPredicateFilter.forPredicate(BlockPredicate.allOf(\n"
        "                                    BlockPredicate.ONLY_IN_AIR_PREDICATE,\n"
        "                                    BlockPredicate.wouldSurvive(ModBlocks.ROCK.get().defaultBlockState(), BlockPos.ZERO))),",
    )

    path.write_text(text, encoding="utf-8")
    return True


def patch_placed(path: Path) -> bool:
    text = path.read_text(encoding="utf-8")
    original = text
    lines = text.splitlines(keepends=True)
    out: list[str] = []
    current_register: str | None = None
    for line in lines:
        m = re.search(r'register\("([^"]+)"', line)
        if m:
            current_register = m.group(1)
        if "HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR)" in line:
            if current_register not in KEEP_OCEAN_FLOOR:
                line = line.replace(
                    "Heightmap.Types.OCEAN_FLOOR",
                    "Heightmap.Types.MOTION_BLOCKING_NO_LEAVES",
                )
        out.append(line)
    text = "".join(out)

    text = text.replace(
        "BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.ROCK.get().defaultBlockState(), BlockPos.ZERO)),",
        "BlockPredicateFilter.forPredicate(BlockPredicate.allOf(\n"
        "                            BlockPredicate.ONLY_IN_AIR_PREDICATE,\n"
        "                            BlockPredicate.wouldSurvive(ModBlocks.ROCK.get().defaultBlockState(), BlockPos.ZERO))),",
    )

    # Surface earth river - add air filter before biome filter
    text = text.replace(
        """    public static final RegistryObject<PlacedFeature> SURFACE_EARTH_RIVER_PLACED = PLACED_FEATURES.register("surface_earth_river_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.SURFACE_EARTH.get()),
                    List.of(RarityFilter.onAverageOnceEvery(4),
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                            BiomeFilter.biome())));""",
        """    public static final RegistryObject<PlacedFeature> SURFACE_EARTH_RIVER_PLACED = PLACED_FEATURES.register("surface_earth_river_placed",
            () -> new PlacedFeature(Holder.direct(ModConfiguredFeatures.SURFACE_EARTH.get()),
                    List.of(RarityFilter.onAverageOnceEvery(4),
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                            BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE),
                            BiomeFilter.biome())));""",
    )

    # 1.18/1.19 use .getHolder().get() instead of Holder.direct
    if "Holder.direct" not in text and "SURFACE_EARTH_RIVER_PLACED" in original:
        text = text.replace(
            """    public static final RegistryObject<PlacedFeature> SURFACE_EARTH_RIVER_PLACED = PLACED_FEATURES.register("surface_earth_river_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.SURFACE_EARTH.getHolder().get(),
                    List.of(RarityFilter.onAverageOnceEvery(4),
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                            BiomeFilter.biome())));""",
            """    public static final RegistryObject<PlacedFeature> SURFACE_EARTH_RIVER_PLACED = PLACED_FEATURES.register("surface_earth_river_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.SURFACE_EARTH.getHolder().get(),
                    List.of(RarityFilter.onAverageOnceEvery(4),
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                            BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE),
                            BiomeFilter.biome())));""",
        )

    if text != original:
        path.write_text(text, encoding="utf-8")
        return True
    return False


def main() -> None:
    for f in CONFIGURED_FILES:
        if patch_configured(f):
            print("configured", f.parts[-5])
    for f in BOOTSTRAP_FILES:
        if patch_bootstrap(f):
            print("bootstrap", f.parts[-5])
    for f in PLACED_FILES:
        if patch_placed(f):
            print("placed", f.parts[-5])


if __name__ == "__main__":
    main()
