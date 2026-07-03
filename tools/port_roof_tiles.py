#!/usr/bin/env python3
"""Copy roof tile Java classes from 1.21.1 to other MC versions with API fixes."""
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SRC = ROOT / "1.21.1" / "src" / "main" / "java" / "com" / "torr" / "materia"

USE_LEGACY = '''    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return handleUse(state, level, pos, player.getItemInHand(hand));
    }

    private InteractionResult handleUse'''


def patch_block_for_version(text: str, version: str) -> str:
    if version in ("1.18.2", "1.19.2", "1.20.1"):
        start = text.index("    @Override\n    protected net.minecraft.world.ItemInteractionResult useItemOn")
        end = text.index("    private InteractionResult handleUse")
        text = text[:start] + USE_LEGACY + text[end + len("    private InteractionResult handleUse"):]

    if version == "1.18.2":
        text = text.replace("import net.minecraft.util.RandomSource;\n", "")
        text = text.replace("import net.minecraft.world.level.storage.loot.LootParams;\n",
                            "import net.minecraft.world.level.storage.loot.LootContext;\n")
        text = text.replace("RandomSource random = level.getRandom();", "java.util.Random random = level.random;")
        text = text.replace("RandomSource random = builder.getOptionalParameter(LootParams.BLOCK_STATE).isPresent()\n"
                            "                    ? builder.getLevel().getRandom()\n"
                            "                    : RandomSource.create();",
                            "java.util.Random random = builder.getLevel().random;")
        text = text.replace("public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder)",
                            "public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)")
        text = text.replace("private static void dropTiles(ServerLevel level, BlockPos pos, RandomSource random, int count)",
                            "private static void dropTiles(ServerLevel level, BlockPos pos, java.util.Random random, int count)")
        text = text.replace("private static List<ItemStack> tileStacksForCount(int count, RandomSource random)",
                            "private static List<ItemStack> tileStacksForCount(int count, java.util.Random random)")
    return text


def patch_modblocks(text: str, version: str) -> str:
    if "ROOF_TILES" in text:
        return text
    needle = """        public static final RegistryObject<Block> THATCH_SLOPE = registerBlock("thatch_slope",
                        () -> new com.torr.materia.block.ThatchSlopeBlock(
                                () -> ModBlocks.THATCH.get().defaultBlockState(),
                                BlockBehaviour.Properties.copy(ModBlocks.THATCH.get())));
        public static final RegistryObject<Block> GUNPOWDER_TRAIL"""
    sound = "SoundType.STONE" if version == "1.18.2" else "SoundType.DEEPSLATE_TILES"
    props = ("BlockBehaviour.Properties.of(Material.STONE)\n"
             "                                        .strength(0.8f)") if version == "1.18.2" else (
             "BlockBehaviour.Properties.of()\n"
             "                                        .strength(0.8f)")
    insert = f"""        public static final RegistryObject<Block> THATCH_SLOPE = registerBlock("thatch_slope",
                        () -> new com.torr.materia.block.ThatchSlopeBlock(
                                () -> ModBlocks.THATCH.get().defaultBlockState(),
                                BlockBehaviour.Properties.copy(ModBlocks.THATCH.get())));
        public static final RegistryObject<Block> ROOF_TILES = registerBlockWithCustomItem("roof_tiles",
                        () -> new com.torr.materia.block.RoofTilesBlock(
                                {props}
                                        .sound({sound})),
                        block -> new com.torr.materia.item.RoofTilesBlockItem(block, new Item.Properties(), 8));
        public static final RegistryObject<Block> GUNPOWDER_TRAIL"""
    text = text.replace(needle, insert)
    if version == "1.18.2" and "import net.minecraft.world.level.material.Material;" not in text:
        text = text.replace(
            "import net.minecraft.world.level.block.Block;",
            "import net.minecraft.world.level.block.Block;\nimport net.minecraft.world.level.material.Material;",
        )
    return text


def patch_moditems(text: str) -> str:
    if "CLAY_ROOF_TILE" in text:
        return text
    return text.replace(
        """        public static final RegistryObject<Item> CLAY_BOWL = ITEMS.register("clay_bowl",
                        () -> new Item(new Item.Properties()
                                        ));
        public static final RegistryObject<Item> TERRACOTTA_ROOF_TILE""",
        """        public static final RegistryObject<Item> CLAY_BOWL = ITEMS.register("clay_bowl",
                        () -> new Item(new Item.Properties()
                                        ));
        public static final RegistryObject<Item> CLAY_ROOF_TILE = ITEMS.register("clay_roof_tile",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> ROOF_FRAME = ITEMS.register("roof_frame",
                        () -> new com.torr.materia.item.RoofFrameItem(new Item.Properties()));
        public static final RegistryObject<Item> TERRACOTTA_ROOF_TILE""",
    )


def patch_cannonball(text: str) -> str:
    if "RoofTilesBlock.onCannonballImpact" in text:
        return text
    return text.replace(
        """        // Sand (and red sand) breaks
        if (materiaCommonConfig.CANNONBALL_EFFECT_SAND_BREAKS.get()
                && (block == Blocks.SAND || block == Blocks.RED_SAND)) {
            level.levelEvent(2001, pos, Block.getId(state));
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            Block.popResource(level, pos, new ItemStack(block.asItem(), 1));
            return;
        }

        // Generic:""",
        """        // Sand (and red sand) breaks
        if (materiaCommonConfig.CANNONBALL_EFFECT_SAND_BREAKS.get()
                && (block == Blocks.SAND || block == Blocks.RED_SAND)) {
            level.levelEvent(2001, pos, Block.getId(state));
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            Block.popResource(level, pos, new ItemStack(block.asItem(), 1));
            return;
        }

        if (block instanceof com.torr.materia.block.RoofTilesBlock && level instanceof ServerLevel serverLevel) {
            com.torr.materia.block.RoofTilesBlock.onCannonballImpact(serverLevel, pos, state);
            return;
        }

        // Generic:""",
    )


def main() -> None:
    files = [
        "block/RoofTilesBlock.java",
        "item/RoofFrameItem.java",
        "item/RoofTilesBlockItem.java",
    ]
    for version in ("1.18.2", "1.19.2", "1.20.1"):
        base = ROOT / version / "src" / "main" / "java" / "com" / "torr" / "materia"
        for rel in files:
            text = (SRC / rel).read_text(encoding="utf-8")
            if "RoofTilesBlock" in rel:
                text = patch_block_for_version(text, version)
            (base / rel).write_text(text, encoding="utf-8")
        (base / "ModBlocks.java").write_text(
            patch_modblocks((base / "ModBlocks.java").read_text(encoding="utf-8"), version),
            encoding="utf-8",
        )
        (base / "ModItems.java").write_text(
            patch_moditems((base / "ModItems.java").read_text(encoding="utf-8")),
            encoding="utf-8",
        )
        (base / "entity" / "CannonballEntity.java").write_text(
            patch_cannonball((base / "entity" / "CannonballEntity.java").read_text(encoding="utf-8")),
            encoding="utf-8",
        )
        print("ported", version)


if __name__ == "__main__":
    main()
