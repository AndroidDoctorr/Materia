#!/usr/bin/env python3
"""Generate tea liquid assets and patch Java sources from beer/wine patterns."""
from __future__ import annotations

import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SHARED = ROOT / "shared" / "src" / "main" / "resources"
ASSETS = SHARED / "assets" / "materia"
VERSIONS = ["1.18.2", "1.19.2", "1.20.1", "1.21.1"]


def replace_beer_to_tea(text: str) -> str:
    text = text.replace("BeerPot", "TeaPot")
    text = text.replace("Beer", "Tea")
    text = text.replace("beer", "tea")
    text = text.replace("BEER", "TEA")
    return text


def write_tea_pot_models() -> None:
    for name in ("low", "medium", "full"):
        src = ASSETS / "models" / "block" / f"beer_pot_{name}.json"
        dst = ASSETS / "models" / "block" / f"tea_pot_{name}.json"
        dst.write_text(replace_beer_to_tea(src.read_text(encoding="utf-8")), encoding="utf-8")

    for item in ("tea_cup", "tea_bottle", "tea_pot", "tea_bucket"):
        path = ASSETS / "models" / "item" / f"{item}.json"
        path.write_text(
            '{\n  "parent": "item/generated",\n  "textures": {\n    "layer0": "materia:item/'
            + item
            + '"\n  }\n}\n',
            encoding="utf-8",
        )

    beer_pot_bs = ASSETS / "blockstates" / "beer_pot.json"
    (ASSETS / "blockstates" / "tea_pot.json").write_text(
        replace_beer_to_tea(beer_pot_bs.read_text(encoding="utf-8")), encoding="utf-8"
    )

    amphora_tea = (ASSETS / "models" / "block" / "amphora_beer.json").read_text(encoding="utf-8")
    amphora_tea = amphora_tea.replace(
        '"up": {\n                    "uv": [\n                        10,\n                        0,\n                        16,\n                        6\n                    ],\n                    "texture": "#top"\n                }',
        '"up": {\n                    "uv": [\n                        0,\n                        10,\n                        6,\n                        16\n                    ],\n                    "texture": "#top"\n                }',
    )
    (ASSETS / "models" / "block" / "amphora_tea.json").write_text(amphora_tea, encoding="utf-8")

    amphora_bs = ASSETS / "blockstates" / "amphora.json"
    text = amphora_bs.read_text(encoding="utf-8")
    if "liquid_type=tea" not in text:
        insert_before_closed = '        "closed=true,liquid_type=empty":'
        tea_open = (
            '        "closed=false,liquid_type=tea": {\n'
            '            "model": "materia:block/amphora_tea"\n'
            "        },\n"
        )
        tea_closed = (
            '        "closed=true,liquid_type=tea": {\n'
            '            "model": "materia:block/amphora_closed"\n'
            "        },\n"
        )
        text = text.replace(
            '        "closed=false,liquid_type=beer": {\n            "model": "materia:block/amphora_beer"\n        },\n',
            '        "closed=false,liquid_type=beer": {\n            "model": "materia:block/amphora_beer"\n        },\n'
            + tea_open,
        )
        text = text.replace(
            '        "closed=true,liquid_type=beer": {\n            "model": "materia:block/amphora_closed"\n        }\n',
            '        "closed=true,liquid_type=beer": {\n            "model": "materia:block/amphora_closed"\n        },\n'
            + tea_closed.rstrip("\n").rstrip(",")[:-1]
            + "\n        }\n",
        )
        # fix: simpler append before final closing
        if "liquid_type=tea" not in text:
            text = amphora_bs.read_text(encoding="utf-8")
            text = text.replace(
                '"closed=false,liquid_type=beer": {\n            "model": "materia:block/amphora_beer"\n        },',
                '"closed=false,liquid_type=beer": {\n            "model": "materia:block/amphora_beer"\n        },\n'
                '        "closed=false,liquid_type=tea": {\n            "model": "materia:block/amphora_tea"\n        },',
            )
            text = text.replace(
                '"closed=true,liquid_type=beer": {\n            "model": "materia:block/amphora_closed"\n        }',
                '"closed=true,liquid_type=tea": {\n            "model": "materia:block/amphora_closed"\n        },\n'
                '        "closed=true,liquid_type=beer": {\n            "model": "materia:block/amphora_closed"\n        }',
            )
        amphora_bs.write_text(text, encoding="utf-8")

    loot_src = SHARED / "data" / "materia" / "loot_tables" / "blocks" / "beer_pot.json"
    loot_dst = SHARED / "data" / "materia" / "loot_tables" / "blocks" / "tea_pot.json"
    loot_dst.write_text(loot_src.read_text(encoding="utf-8"), encoding="utf-8")

    recipe = SHARED / "data" / "materia" / "recipes" / "water_pot_tea_leaves.json"
    if not recipe.exists():
        recipe.write_text(
            """{
    "type": "materia:water_pot",
    "ingredient": {
        "item": "materia:tea_leaves"
    },
    "results": [
        {
            "item": "materia:tea_cup",
            "count": 1
        }
    ],
    "cookingtime": 160,
    "requires_boiling": true
}
""",
            encoding="utf-8",
        )


def write_java_copies() -> None:
    for ver in VERSIONS:
        base = ROOT / ver / "src" / "main" / "java" / "com" / "torr" / "materia"
        beer_pot = (base / "BeerPotBlock.java").read_text(encoding="utf-8")
        (base / "TeaPotBlock.java").write_text(replace_beer_to_tea(beer_pot), encoding="utf-8")

        beer_be = (base / "blockentity" / "BeerPotBlockEntity.java").read_text(encoding="utf-8")
        (base / "blockentity" / "TeaPotBlockEntity.java").write_text(
            replace_beer_to_tea(beer_be), encoding="utf-8"
        )

        milk_cup = (base / "item" / "MilkCupItem.java").read_text(encoding="utf-8")
        tea_cup = milk_cup.replace("MilkCupItem", "TeaCupItem").replace("milk cup", "tea cup")
        tea_cup = re.sub(
            r"\n\s*// Remove all status effects.*?user\.removeAllEffects\(\);\s*\n\s*\}\s*\n",
            "\n",
            tea_cup,
            flags=re.DOTALL,
        )
        (base / "item" / "TeaCupItem.java").write_text(tea_cup, encoding="utf-8")


def patch_mod_blocks(path: Path) -> None:
    text = path.read_text(encoding="utf-8")
    if "TEA_POT" in text:
        return
    text = text.replace(
        """        public static final RegistryObject<Block> BEER_POT = BLOCKS.register("beer_pot",
                        () -> new BeerPotBlock(BlockBehaviour.Properties.of().strength(0.5f)
                                        .sound(SoundType.STONE).noOcclusion()));""",
        """        public static final RegistryObject<Block> BEER_POT = BLOCKS.register("beer_pot",
                        () -> new BeerPotBlock(BlockBehaviour.Properties.of().strength(0.5f)
                                        .sound(SoundType.STONE).noOcclusion()));
        public static final RegistryObject<Block> TEA_POT = BLOCKS.register("tea_pot",
                        () -> new TeaPotBlock(BlockBehaviour.Properties.of().strength(0.5f)
                                        .sound(SoundType.STONE).noOcclusion()));""",
    )
    path.write_text(text, encoding="utf-8")


def patch_mod_block_entities(path: Path) -> None:
    text = path.read_text(encoding="utf-8")
    if "TEA_POT_BLOCK_ENTITY" in text:
        return
    text = text.replace(
        """        public static final RegistryObject<BlockEntityType<BeerPotBlockEntity>> BEER_POT_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("beer_pot_block_entity", () -> BlockEntityType.Builder.of(BeerPotBlockEntity::new,
                                        ModBlocks.BEER_POT.get()).build(null));""",
        """        public static final RegistryObject<BlockEntityType<BeerPotBlockEntity>> BEER_POT_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("beer_pot_block_entity", () -> BlockEntityType.Builder.of(BeerPotBlockEntity::new,
                                        ModBlocks.BEER_POT.get()).build(null));

        public static final RegistryObject<BlockEntityType<TeaPotBlockEntity>> TEA_POT_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("tea_pot_block_entity", () -> BlockEntityType.Builder.of(TeaPotBlockEntity::new,
                                        ModBlocks.TEA_POT.get()).build(null));""",
    )
    path.write_text(text, encoding="utf-8")


def patch_mod_items(path: Path) -> None:
    text = path.read_text(encoding="utf-8")
    if "TEA_CUP" in text:
        return
    insert = """
        public static final RegistryObject<Item> TEA_POT = ITEMS.register("tea_pot",
                        () -> new net.minecraft.world.item.BlockItem(ModBlocks.TEA_POT.get(), new Item.Properties()
                                        .stacksTo(1)));
        public static final RegistryObject<Item> TEA_BUCKET = ITEMS.register("tea_bucket",
                        () -> new Item(new Item.Properties()
                                        .stacksTo(1)));
        public static final RegistryObject<Item> TEA_CUP = ITEMS.register("tea_cup",
                        () -> new com.torr.materia.item.TeaCupItem(new Item.Properties()
                                        .stacksTo(16)));
        public static final RegistryObject<Item> TEA_BOTTLE = ITEMS.register("tea_bottle",
                        () -> new com.torr.materia.item.DrinkableBottleItem(new Item.Properties()
                                        .stacksTo(16)));
"""
    text = text.replace(
        """        public static final RegistryObject<Item> BEER_BOTTLE = ITEMS.register("beer_bottle",""",
        insert
        + """        public static final RegistryObject<Item> BEER_BOTTLE = ITEMS.register("beer_bottle",""",
    )
    path.write_text(text, encoding="utf-8")


def patch_pot_block(path: Path) -> None:
    text = path.read_text(encoding="utf-8")
    if "TEA_CUP" in text and "tea pot" in text.lower():
        return
    beer_block = """        // Convert empty pot into a beer pot (from beer pot item -> empty pot item)
        if (held.is(ModItems.BEER_POT.get())) {
            if (!level.isClientSide) {
                BlockState newState = ModBlocks.BEER_POT.get().defaultBlockState()
                        .setValue(BeerPotBlock.WATER_LEVEL, 3);
                level.setBlock(pos, newState, 3);
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                ItemStack emptyPot = new ItemStack(ModItems.POT.get());
                ItemStack result = ItemUtils.createFilledResult(held, player, emptyPot);
                player.setItemInHand(hand, result);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Convert empty pot into a wine pot"""
    tea_block = beer_block.replace("beer", "tea").replace("Beer", "Tea").replace("BEER", "TEA")
    tea_block = tea_block.replace("wine pot", "beer pot").replace("Wine", "Beer").replace("WINE", "BEER")
    # fix comment
    tea_block = tea_block.replace("beer pot (from beer pot item", "tea pot (from tea pot item")
    if "ModItems.TEA_CUP" not in text:
        text = text.replace(
            """        // Convert empty pot into a wine pot (from wine cup -> crucible)""",
            tea_block
            + """
        // Convert empty pot into a wine pot (from wine cup -> crucible)""",
        )
    path.write_text(text, encoding="utf-8")


def patch_amphora_block_entity(path: Path) -> None:
    text = path.read_text(encoding="utf-8")
    if "hasTea()" in text:
        return

    text = text.replace(
        '                    case "beer":\n                        expectedType = com.torr.materia.AmphoraBlock.LiquidType.BEER;\n                        break;',
        '                    case "beer":\n                        expectedType = com.torr.materia.AmphoraBlock.LiquidType.BEER;\n                        break;\n                    case "tea":\n                        expectedType = com.torr.materia.AmphoraBlock.LiquidType.TEA;\n                        break;',
    )

    text = text.replace(
        """    public boolean hasBeer() {
        return this.hasLiquid() && "beer".equals(this.liquidType);
    }
""",
        """    public boolean hasBeer() {
        return this.hasLiquid() && "beer".equals(this.liquidType);
    }

    public boolean hasTea() {
        return this.hasLiquid() && "tea".equals(this.liquidType);
    }
""",
    )

    text = text.replace(
        """    public boolean canAddBeer() {
        return canAddLiquid("beer");
    }
""",
        """    public boolean canAddBeer() {
        return canAddLiquid("beer");
    }

    public boolean canAddTea() {
        return canAddLiquid("tea");
    }
""",
    )

    text = text.replace(
        """    public void addBeer(int amount) {
        addLiquid("beer", amount);
    }
""",
        """    public void addBeer(int amount) {
        addLiquid("beer", amount);
    }

    public void addTea(int amount) {
        addLiquid("tea", amount);
    }

    public boolean trySteepTeaLeaves(ItemStack ingredient) {
        if (this.storageMode == MODE_SOLID) return false;
        if (this.hasLid()) return false;
        if (!this.hasWater()) return false;
        if (!ingredient.is(com.torr.materia.ModItems.TEA_LEAVES.get())) return false;
        this.liquidType = "tea";
        this.storageMode = MODE_LIQUID;
        this.setChanged();
        return true;
    }
""",
    )

    path.write_text(text, encoding="utf-8")


def patch_amphora_block(path: Path) -> None:
    text = path.read_text(encoding="utf-8")
    if "LiquidType.TEA" in text:
        return

    text = text.replace(
        '        BEER("beer");',
        '        BEER("beer"),\n        TEA("tea");',
    )

    tea_leaves_block = """
        // Steep tea leaves in water amphora
        if (held.is(ModItems.TEA_LEAVES.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.getStorageMode() == AmphoraBlockEntity.MODE_SOLID) {
                    player.displayClientMessage(Component.translatable("message.materia.amphora.solid_mode"), true);
                    return InteractionResult.SUCCESS;
                }
                if (amphoraEntity.hasLid()) {
                    player.displayClientMessage(Component.translatable("message.materia.amphora.liquid_mode"), true);
                    return InteractionResult.SUCCESS;
                }
                if (amphoraEntity.trySteepTeaLeaves(held)) {
                    held.shrink(1);
                    level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.8F, 1.0F);
                    updateBlockState(level, pos, amphoraEntity);
                    player.displayClientMessage(Component.translatable("message.materia.amphora.tea_steeped"), true);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
"""
    text = text.replace(
        "        // Beer mash ingredient interactions (water + wheat/hops)",
        tea_leaves_block + "        // Beer mash ingredient interactions (water + wheat/hops)",
    )

    def after_beer_extract(pattern: str, tea_lines: str) -> None:
        nonlocal text
        if tea_lines.split("\n")[1].strip() in text:
            return
        text = text.replace(pattern, pattern + tea_lines)

    after_beer_extract(
        """                } else if (amphoraEntity.hasBeer() && amphoraEntity.removeLiquid(1)) {
                    ItemStack beerCup = new ItemStack(ModItems.BEER_CUP.get());
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, beerCup);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                }""",
        """
                } else if (amphoraEntity.hasTea() && amphoraEntity.removeLiquid(1)) {
                    ItemStack teaCup = new ItemStack(ModItems.TEA_CUP.get());
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, teaCup);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                }""",
    )

    after_beer_extract(
        """                } else if (amphoraEntity.hasBeer() && amphoraEntity.getLiquidAmount() >= 3 && amphoraEntity.removeLiquid(3)) {
                    ItemStack beerPot = new ItemStack(ModItems.BEER_POT.get());
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, beerPot);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                }""",
        """
                } else if (amphoraEntity.hasTea() && amphoraEntity.getLiquidAmount() >= 3 && amphoraEntity.removeLiquid(3)) {
                    ItemStack teaPot = new ItemStack(ModItems.TEA_POT.get());
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, teaPot);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                }""",
    )

    after_beer_extract(
        """                } else if (amphoraEntity.hasBeer() && amphoraEntity.getLiquidAmount() >= 3 && amphoraEntity.removeLiquid(3)) {
                    ItemStack beerBucket = new ItemStack(ModItems.BEER_BUCKET.get());
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, beerBucket);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                }""",
        """
                } else if (amphoraEntity.hasTea() && amphoraEntity.getLiquidAmount() >= 3 && amphoraEntity.removeLiquid(3)) {
                    ItemStack teaBucket = new ItemStack(ModItems.TEA_BUCKET.get());
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, teaBucket);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                }""",
    )

    after_beer_extract(
        """                } else if (amphoraEntity.hasBeer() && amphoraEntity.removeLiquid(1)) {
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack beerBottle = new ItemStack(ModItems.BEER_BOTTLE.get());
                    ItemStack result = ItemUtils.createFilledResult(held, player, beerBottle);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                }""",
        """
                } else if (amphoraEntity.hasTea() && amphoraEntity.removeLiquid(1)) {
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack teaBottle = new ItemStack(ModItems.TEA_BOTTLE.get());
                    ItemStack result = ItemUtils.createFilledResult(held, player, teaBottle);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                }""",
    )

    beer_bottle_fill = """        // Beer bottle interactions: beer_bottle -> glass_bottle (add 1 bottle worth)
        if (held.is(ModItems.BEER_BOTTLE.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddBeer()) {
                    amphoraEntity.addBeer(1);
                    level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);
                    ItemStack result = ItemUtils.createFilledResult(held, player, emptyBottle);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        // Milk cup interactions"""
    tea_fill = beer_bottle_fill.replace("beer", "tea").replace("Beer", "Tea").replace("BEER", "TEA")
    tea_fill = tea_fill.replace("Milk cup", "Tea bottle duplicate fix")
    text = text.replace(
        beer_bottle_fill.replace("Tea bottle duplicate fix", "Milk cup"),
        beer_bottle_fill.replace("Milk cup", "Tea bottle fill")
        .replace(
            "// Milk cup interactions",
            """        // Tea bottle interactions: tea_bottle -> glass_bottle (add 1 bottle worth)
        if (held.is(ModItems.TEA_BOTTLE.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddTea()) {
                    amphoraEntity.addTea(1);
                    level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);
                    ItemStack result = ItemUtils.createFilledResult(held, player, emptyBottle);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        // Tea cup interactions: tea_cup -> crucible (add tea)
        if (held.is(ModItems.TEA_CUP.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddTea()) {
                    amphoraEntity.addTea(1);
                    ItemStack crucible = new ItemStack(ModItems.CRUCIBLE.get());
                    level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, crucible);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        // Tea pot interactions: tea_pot -> pot (add 3 bottles worth)
        if (held.is(ModItems.TEA_POT.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddTea() && amphoraEntity.getLiquidAmount() <= 6) {
                    amphoraEntity.addTea(3);
                    ItemStack pot = new ItemStack(ModItems.POT.get());
                    level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, pot);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        // Tea bucket interactions: tea_bucket -> bucket (add 3 bottles worth)
        if (held.is(ModItems.TEA_BUCKET.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddTea() && amphoraEntity.getLiquidAmount() <= 6) {
                    amphoraEntity.addTea(3);
                    ItemStack bucket = new ItemStack(Items.BUCKET);
                    level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, bucket);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        // Milk cup interactions""",
        ),
    )

    text = text.replace(
        """               stack.is(ModItems.BEER_BOTTLE.get());""",
        """               stack.is(ModItems.BEER_BOTTLE.get()) ||
               stack.is(ModItems.TEA_CUP.get()) ||
               stack.is(ModItems.TEA_POT.get()) ||
               stack.is(ModItems.TEA_BUCKET.get()) ||
               stack.is(ModItems.TEA_BOTTLE.get());""",
    )

    text = text.replace(
        """                    case "beer":
                        newState = newState.setValue(LIQUID_TYPE, LiquidType.BEER);
                        break;""",
        """                    case "beer":
                        newState = newState.setValue(LIQUID_TYPE, LiquidType.BEER);
                        break;
                    case "tea":
                        newState = newState.setValue(LIQUID_TYPE, LiquidType.TEA);
                        break;""",
    )

    text = text.replace(
        """        } else if (amphoraEntity.hasBeer()) {
            int liquidAmount = amphoraEntity.getLiquidAmount();
            for (int i = 0; i < liquidAmount; i++) {
                Block.popResource(level, pos, new ItemStack(ModItems.BEER_CUP.get()));
            }
        }
    }
}""",
        """        } else if (amphoraEntity.hasBeer()) {
            int liquidAmount = amphoraEntity.getLiquidAmount();
            for (int i = 0; i < liquidAmount; i++) {
                Block.popResource(level, pos, new ItemStack(ModItems.BEER_CUP.get()));
            }
        } else if (amphoraEntity.hasTea()) {
            int liquidAmount = amphoraEntity.getLiquidAmount();
            for (int i = 0; i < liquidAmount; i++) {
                Block.popResource(level, pos, new ItemStack(ModItems.TEA_CUP.get()));
            }
        }
    }
}""",
    )

    path.write_text(text, encoding="utf-8")


def patch_falling_amphora(path: Path) -> None:
    text = path.read_text(encoding="utf-8")
    if "hasTea()" in text:
        return
    text = text.replace(
        """        } else if (tempEntity.hasBeer()) {
            // Drop beer as items (1 beer_cup item per bottle worth)
            int liquidAmount = tempEntity.getLiquidAmount();
            for (int i = 0; i < liquidAmount; i++) {
                ItemEntity itemEntity = new ItemEntity(this.level(),
                    pos.getX() + 0.5 + (this.random.nextDouble() - 0.5) * 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5 + (this.random.nextDouble() - 0.5) * 0.5,
                    new ItemStack(ModItems.BEER_CUP.get()));
                itemEntity.setDefaultPickUpDelay();
                this.level().addFreshEntity(itemEntity);
            }
        }
    }
}""",
        """        } else if (tempEntity.hasBeer()) {
            // Drop beer as items (1 beer_cup item per bottle worth)
            int liquidAmount = tempEntity.getLiquidAmount();
            for (int i = 0; i < liquidAmount; i++) {
                ItemEntity itemEntity = new ItemEntity(this.level(),
                    pos.getX() + 0.5 + (this.random.nextDouble() - 0.5) * 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5 + (this.random.nextDouble() - 0.5) * 0.5,
                    new ItemStack(ModItems.BEER_CUP.get()));
                itemEntity.setDefaultPickUpDelay();
                this.level().addFreshEntity(itemEntity);
            }
        } else if (tempEntity.hasTea()) {
            int liquidAmount = tempEntity.getLiquidAmount();
            for (int i = 0; i < liquidAmount; i++) {
                ItemEntity itemEntity = new ItemEntity(this.level(),
                    pos.getX() + 0.5 + (this.random.nextDouble() - 0.5) * 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5 + (this.random.nextDouble() - 0.5) * 0.5,
                    new ItemStack(ModItems.TEA_CUP.get()));
                itemEntity.setDefaultPickUpDelay();
                this.level().addFreshEntity(itemEntity);
            }
        }
    }
}""",
    )
    path.write_text(text, encoding="utf-8")


def patch_mod_fluids(path: Path) -> None:
    text = path.read_text(encoding="utf-8")
    if "TEA_TYPE" in text:
        return
    text = text.replace(
        '    public static final RegistryObject<FluidType> BEER_MASH_TYPE = registerType("beer_mash", 1050, 3500, 300);',
        '    public static final RegistryObject<FluidType> BEER_MASH_TYPE = registerType("beer_mash", 1050, 3500, 300);\n'
        '    public static final RegistryObject<FluidType> TEA_TYPE = registerType("tea", 1005, 1800, 320);',
    )
    text = text.replace(
        "    private static ForgeFlowingFluid.Properties beerMashProps;",
        "    private static ForgeFlowingFluid.Properties beerMashProps;\n    private static ForgeFlowingFluid.Properties teaProps;",
    )
    text = text.replace(
        """    public static final RegistryObject<FlowingFluid> BEER_MASH_STILL = FLUIDS.register("beer_mash",
            () -> new ForgeFlowingFluid.Source(beerMashProps()));""",
        """    public static final RegistryObject<FlowingFluid> BEER_MASH_STILL = FLUIDS.register("beer_mash",
            () -> new ForgeFlowingFluid.Source(beerMashProps()));

    public static final RegistryObject<FlowingFluid> TEA_FLOWING = FLUIDS.register("tea_flowing",
            () -> new ForgeFlowingFluid.Flowing(teaProps()));
    public static final RegistryObject<FlowingFluid> TEA_STILL = FLUIDS.register("tea",
            () -> new ForgeFlowingFluid.Source(teaProps()));""",
    )
    text = text.replace(
        """    private ModFluids() {}
}""",
        """
    private static ForgeFlowingFluid.Properties teaProps() {
        if (teaProps == null) {
            teaProps = new ForgeFlowingFluid.Properties(TEA_TYPE::get, TEA_STILL::get, TEA_FLOWING::get);
        }
        return teaProps;
    }

    private ModFluids() {}
}""",
    )
    path.write_text(text, encoding="utf-8")


def patch_amphora_fluid_handler(path: Path) -> None:
    text = path.read_text(encoding="utf-8")
    if '"tea"' in text:
        return
    text = text.replace(
        '            case "beer_mash" -> ModFluids.BEER_MASH_STILL.get();',
        '            case "beer_mash" -> ModFluids.BEER_MASH_STILL.get();\n            case "tea" -> ModFluids.TEA_STILL.get();',
    )
    text = text.replace(
        """        if (fluid.isSame(ModFluids.BEER_MASH_STILL.get())) {
            return "beer_mash";
        }""",
        """        if (fluid.isSame(ModFluids.BEER_MASH_STILL.get())) {
            return "beer_mash";
        }
        if (fluid.isSame(ModFluids.TEA_STILL.get())) {
            return "tea";
        }""",
    )
    path.write_text(text, encoding="utf-8")


def patch_lang() -> None:
    for lang_file in (ASSETS / "lang" / "en_us.json", ASSETS / "lang" / "nl_be.json"):
        text = lang_file.read_text(encoding="utf-8")
        if "block.materia.tea_pot" in text:
            continue
        text = text.replace(
            '    "block.materia.beer_pot": "Beer Pot",',
            '    "block.materia.beer_pot": "Beer Pot",\n    "block.materia.tea_pot": "Tea Pot",',
        )
        text = text.replace(
            '    "item.materia.beer_bottle": "Beer Bottle",',
            '    "item.materia.tea_pot": "Tea Pot",\n    "item.materia.tea_bucket": "Tea Bucket",\n    "item.materia.tea_cup": "Tea Cup",\n    "item.materia.tea_bottle": "Tea Bottle",\n    "item.materia.beer_bottle": "Beer Bottle",',
        )
        if lang_file.name == "en_us.json":
            text = text.replace(
                '    "fluid_type.materia.beer_mash": "Beer Mash",',
                '    "fluid_type.materia.beer_mash": "Beer Mash",\n    "fluid_type.materia.tea": "Tea",\n    "message.materia.amphora.tea_steeped": "The water has steeped into tea",',
            )
        lang_file.write_text(text, encoding="utf-8")


def main() -> None:
    write_tea_pot_models()
    write_java_copies()
    for ver in VERSIONS:
        base = ROOT / ver / "src" / "main" / "java" / "com" / "torr" / "materia"
        patch_mod_blocks(base / "ModBlocks.java")
        patch_mod_block_entities(base / "ModBlockEntities.java")
        patch_mod_items(base / "ModItems.java")
        patch_pot_block(base / "PotBlock.java")
        patch_amphora_block_entity(base / "blockentity" / "AmphoraBlockEntity.java")
        patch_amphora_block(base / "AmphoraBlock.java")
        patch_falling_amphora(base / "entity" / "FallingAmphoraEntity.java")
        if ver in ("1.20.1", "1.21.1"):
            patch_mod_fluids(base / "ModFluids.java")
            patch_amphora_fluid_handler(base / "blockentity" / "AmphoraFluidHandler.java")
    patch_lang()
    print("Tea liquid setup complete.")


if __name__ == "__main__":
    main()
