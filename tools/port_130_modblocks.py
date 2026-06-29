from pathlib import Path

ROOT = Path(r"C:/MCMods/Materia")

AGAVE_118 = """
        public static final RegistryObject<Block> AGAVE = registerBlock("agave",
                        () -> new AgaveBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak()
                                        .sound(SoundType.GRASS)));
"""

BURGUNDY_TAN = """
        // BURGUNDY BLOCKS
        public static final RegistryObject<Block> BURGUNDY_WOOL = registerBlock("burgundy_wool",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.RED_WOOL)));
        public static final RegistryObject<Block> BURGUNDY_GLASS = registerBlock("burgundy_glass",
                        () -> new net.minecraft.world.level.block.GlassBlock(BlockBehaviour.Properties.copy(Blocks.RED_STAINED_GLASS).noOcclusion()));
        public static final RegistryObject<Block> BURGUNDY_CONCRETE = registerBlock("burgundy_concrete",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.RED_CONCRETE)));
        public static final RegistryObject<Block> BURGUNDY_CONCRETE_POWDER = registerBlock("burgundy_concrete_powder",
                        () -> new net.minecraft.world.level.block.ConcretePowderBlock(ModBlocks.BURGUNDY_CONCRETE.get(),
                                        BlockBehaviour.Properties.copy(Blocks.RED_CONCRETE_POWDER)));
        public static final RegistryObject<Block> BURGUNDY_TERRACOTTA = registerBlock("burgundy_terracotta",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.RED_TERRACOTTA)));
        public static final RegistryObject<Block> BURGUNDY_CANDLE = registerBlock("burgundy_candle",
                        () -> new net.minecraft.world.level.block.CandleBlock(BlockBehaviour.Properties.copy(Blocks.RED_CANDLE)));

        // TAN BLOCKS
        public static final RegistryObject<Block> TAN_WOOL = registerBlock("tan_wool",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.BROWN_WOOL)));
        public static final RegistryObject<Block> TAN_GLASS = registerBlock("tan_glass",
                        () -> new net.minecraft.world.level.block.GlassBlock(BlockBehaviour.Properties.copy(Blocks.BROWN_STAINED_GLASS).noOcclusion()));
        public static final RegistryObject<Block> TAN_CONCRETE = registerBlock("tan_concrete",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.BROWN_CONCRETE)));
        public static final RegistryObject<Block> TAN_CONCRETE_POWDER = registerBlock("tan_concrete_powder",
                        () -> new net.minecraft.world.level.block.ConcretePowderBlock(ModBlocks.TAN_CONCRETE.get(),
                                        BlockBehaviour.Properties.copy(Blocks.BROWN_CONCRETE_POWDER)));
        public static final RegistryObject<Block> TAN_TERRACOTTA = registerBlock("tan_terracotta",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.BROWN_TERRACOTTA)));
        public static final RegistryObject<Block> TAN_CANDLE = registerBlock("tan_candle",
                        () -> new net.minecraft.world.level.block.CandleBlock(BlockBehaviour.Properties.copy(Blocks.BROWN_CANDLE)));

"""

def trees_block_snippet(material: bool) -> str:
    p = lambda mat, rest: f"BlockBehaviour.Properties.of({mat}).{rest}" if material else f"BlockBehaviour.Properties.of().{rest}"
    wood = "Material.WOOD" if material else ""
    plant = "Material.PLANT" if material else ""
    leaves = "Material.LEAVES" if material else ""
    return f"""
        // Fig tree
        public static final RegistryObject<Block> FIG_LOG = registerBlock("fig_log",
                        () -> new com.torr.materia.block.FlammableRotatedPillarBlock({p(wood, "strength(2.0f).sound(SoundType.WOOD)")}));
        public static final RegistryObject<Block> FIG_SAPLING = registerBlock("fig_sapling",
                        () -> new com.torr.materia.block.FigTreeSaplingBlock({p(plant, "noCollission().instabreak().sound(SoundType.GRASS).randomTicks()")}));
        public static final RegistryObject<Block> FIG_LEAVES = registerBlock("fig_leaves",
                        () -> new com.torr.materia.block.FigTreeLeavesBlock({p(leaves, "strength(0.2f).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn((state, world, pos, type) -> false).isSuffocating((state, world, pos) -> false).isViewBlocking((state, world, pos) -> false)")}));

        // Cedar tree
        public static final RegistryObject<Block> CEDAR_LOG = registerBlock("cedar_log",
                        () -> new com.torr.materia.block.FlammableRotatedPillarBlock({p(wood, "strength(2.0f).sound(SoundType.WOOD)")}));
        public static final RegistryObject<Block> CEDAR_SAPLING = registerBlock("cedar_sapling",
                        () -> new com.torr.materia.block.CedarSaplingBlock({p(plant, "noCollission().instabreak().sound(SoundType.GRASS).randomTicks()")}));
        public static final RegistryObject<Block> CEDAR_LEAVES = registerBlock("cedar_leaves",
                        () -> new com.torr.materia.block.CedarLeavesBlock({p(leaves, "strength(0.2f).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn((state, world, pos, type) -> false).isSuffocating((state, world, pos) -> false).isViewBlocking((state, world, pos) -> false)")}));

        // Eucalyptus
        public static final RegistryObject<Block> EUCALYPTUS_LOG = registerBlock("eucalyptus_log",
                        () -> new com.torr.materia.block.FlammableRotatedPillarBlock({p(wood, "strength(2.0f).sound(SoundType.WOOD)")}));
        public static final RegistryObject<Block> RAINBOW_EUCALYPTUS_LOG = registerBlock("rainbow_eucalyptus_log",
                        () -> new com.torr.materia.block.FlammableRotatedPillarBlock({p(wood, "strength(2.0f).sound(SoundType.WOOD)")}));
        public static final RegistryObject<Block> EUCALYPTUS_LEAVES = registerBlock("eucalyptus_leaves",
                        () -> new com.torr.materia.block.CedarLeavesBlock({p(leaves, "strength(0.2f).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn((state, world, pos, type) -> false).isSuffocating((state, world, pos) -> false).isViewBlocking((state, world, pos) -> false)")}));
        public static final RegistryObject<Block> EUCALYPTUS_SAPLING = registerBlock("eucalyptus_sapling",
                        () -> new com.torr.materia.block.EucalyptusSaplingBlock({p(plant, "noCollission().instabreak().sound(SoundType.GRASS).randomTicks()")}));
        public static final RegistryObject<Block> RAINBOW_EUCALYPTUS_SAPLING = registerBlock("rainbow_eucalyptus_sapling",
                        () -> new com.torr.materia.block.RainbowEucalyptusSaplingBlock({p(plant, "noCollission().instabreak().sound(SoundType.GRASS).randomTicks()")}));

"""

def patch_modblocks(path: Path, material: bool):
    text = path.read_text(encoding="utf-8")
    if "FIG_LOG" in text:
        return
    if material:
        if "AGAVE" not in text:
            text = text.replace(
                """        public static final RegistryObject<Block> ESPARTO = registerBlock("esparto",
                        () -> new EspartoBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak()
                                        .sound(SoundType.GRASS)));
        
        // OTHER BLOCKS""",
                """        public static final RegistryObject<Block> ESPARTO = registerBlock("esparto",
                        () -> new EspartoBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak()
                                        .sound(SoundType.GRASS)));"""
                + AGAVE_118
                + """
        // OTHER BLOCKS""",
            )
    else:
        if "AGAVE" not in text:
            text = text.replace(
                """        public static final RegistryObject<Block> ESPARTO = registerBlock("esparto",
                        () -> new EspartoBlock(BlockBehaviour.Properties.of().noCollission().instabreak()
                                        .sound(SoundType.GRASS)));
        
        // OTHER BLOCKS""",
                """        public static final RegistryObject<Block> ESPARTO = registerBlock("esparto",
                        () -> new EspartoBlock(BlockBehaviour.Properties.of().noCollission().instabreak()
                                        .sound(SoundType.GRASS)));
        public static final RegistryObject<Block> AGAVE = registerBlock("agave",
                        () -> new AgaveBlock(BlockBehaviour.Properties.of().noCollission().instabreak()
                                        .sound(SoundType.GRASS)));
        
        // OTHER BLOCKS""",
            )
    if "BURGUNDY_WOOL" not in text:
        text = text.replace(
            """        public static final RegistryObject<Block> OLIVE_CARPET = registerBlock("olive_carpet",
                        () -> new CarpetBlock(BlockBehaviour.Properties.copy(Blocks.GREEN_CARPET)));

        // INDIGO BLOCKS""",
            """        public static final RegistryObject<Block> OLIVE_CARPET = registerBlock("olive_carpet",
                        () -> new CarpetBlock(BlockBehaviour.Properties.copy(Blocks.GREEN_CARPET)));
"""
            + BURGUNDY_TAN
            + """
        // INDIGO BLOCKS""",
        )
    trees = trees_block_snippet(material)
    text = text.replace(
        """
        // TABLES""",
        trees + """
        // TABLES""",
        1,
    )
    path.write_text(text, encoding="utf-8")

for ver, mat in [("1.18.2", True), ("1.19.2", True), ("1.21.1", False)]:
    patch_modblocks(ROOT / ver / "src/main/java/com/torr/materia/ModBlocks.java", mat)
    print("ModBlocks", ver)
