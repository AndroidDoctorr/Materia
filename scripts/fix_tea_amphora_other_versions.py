#!/usr/bin/env python3
"""Apply amphora/pot tea patches to 1.18.2, 1.19.2, 1.20.1 from 1.21.1 patterns."""
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
VERSIONS = ["1.18.2", "1.19.2", "1.20.1"]

BUCKET_EXTRACT = """
                } else if (amphoraEntity.hasTea() && amphoraEntity.getLiquidAmount() >= 3 && amphoraEntity.removeLiquid(3)) {
                    ItemStack teaBucket = new ItemStack(ModItems.TEA_BUCKET.get());
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, teaBucket);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;"""

BOTTLE_EXTRACT = """
                } else if (amphoraEntity.hasTea() && amphoraEntity.removeLiquid(1)) {
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack teaBottle = new ItemStack(ModItems.TEA_BOTTLE.get());
                    ItemStack result = ItemUtils.createFilledResult(held, player, teaBottle);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;"""

TEA_FILL = """
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

        // Tea bottle interactions: tea_bottle -> glass_bottle (add 1 bottle worth)
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

"""

POT_TEA = """
        // Convert empty pot into a tea pot (from tea cup -> crucible)
        if (held.is(ModItems.TEA_CUP.get())) {
            if (!level.isClientSide) {
                BlockState newState = ModBlocks.TEA_POT.get().defaultBlockState()
                        .setValue(TeaPotBlock.WATER_LEVEL, 1);
                level.setBlock(pos, newState, 3);
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

                ItemStack crucible = new ItemStack(ModItems.CRUCIBLE.get());
                ItemStack result = ItemUtils.createFilledResult(held, player, crucible);
                player.setItemInHand(hand, result);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Convert empty pot into a tea pot (from tea bottle -> empty bottle)
        if (held.is(ModItems.TEA_BOTTLE.get())) {
            if (!level.isClientSide) {
                BlockState newState = ModBlocks.TEA_POT.get().defaultBlockState()
                        .setValue(TeaPotBlock.WATER_LEVEL, 1);
                level.setBlock(pos, newState, 3);
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

                ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);
                ItemStack result = ItemUtils.createFilledResult(held, player, emptyBottle);
                player.setItemInHand(hand, result);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Convert empty pot into a tea pot (from tea bucket -> empty bucket)
        if (held.is(ModItems.TEA_BUCKET.get())) {
            if (!level.isClientSide) {
                BlockState newState = ModBlocks.TEA_POT.get().defaultBlockState()
                        .setValue(TeaPotBlock.WATER_LEVEL, 3);
                level.setBlock(pos, newState, 3);
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

                ItemStack emptyBucket = new ItemStack(Items.BUCKET);
                ItemStack result = ItemUtils.createFilledResult(held, player, emptyBucket);
                player.setItemInHand(hand, result);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Convert empty pot into a tea pot (from tea pot item -> empty pot item)
        if (held.is(ModItems.TEA_POT.get())) {
            if (!level.isClientSide) {
                BlockState newState = ModBlocks.TEA_POT.get().defaultBlockState()
                        .setValue(TeaPotBlock.WATER_LEVEL, 3);
                level.setBlock(pos, newState, 3);
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

                ItemStack emptyPot = new ItemStack(ModItems.POT.get());
                ItemStack result = ItemUtils.createFilledResult(held, player, emptyPot);
                player.setItemInHand(hand, result);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

"""


def patch_amphora(path: Path) -> None:
    text = path.read_text(encoding="utf-8")
    if "teaBucket = new ItemStack(ModItems.TEA_BUCKET" not in text:
        text = text.replace(
            """                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        // Glass bottle interactions:""",
            """                    return InteractionResult.SUCCESS;"""
            + BUCKET_EXTRACT
            + """
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        // Glass bottle interactions:""",
            1,
        )
    if "teaBottle = new ItemStack(ModItems.TEA_BOTTLE" not in text:
        text = text.replace(
            """                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        if (held.is(Items.POTION))""",
            """                    return InteractionResult.SUCCESS;"""
            + BOTTLE_EXTRACT
            + """
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        if (held.is(Items.POTION))""",
            1,
        )
    if "// Tea cup interactions: tea_cup" not in text:
        text = text.replace(
            """            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        // Wine bottle interactions: wine_bottle -> glass_bottle (add 1 bottle worth)""",
            """            return InteractionResult.sidedSuccess(level.isClientSide());
        }
"""
            + TEA_FILL
            + """        // Wine bottle interactions: wine_bottle -> glass_bottle (add 1 bottle worth)""",
            1,
        )
    text = text.replace(
        "// Tea bottle fill interactions: milk_cup -> crucible (add milk)",
        "// Milk cup interactions: milk_cup -> crucible (add milk)",
    )
    path.write_text(text, encoding="utf-8")


def patch_pot(path: Path) -> None:
    text = path.read_text(encoding="utf-8")
    if "ModItems.TEA_CUP.get()" in text:
        return
    marker = "        // Convert empty pot into a wine pot (from wine cup -> crucible)"
    if marker in text:
        text = text.replace(marker, POT_TEA + marker)
    path.write_text(text, encoding="utf-8")


for ver in VERSIONS:
    base = ROOT / ver / "src" / "main" / "java" / "com" / "torr" / "materia"
    patch_amphora(base / "AmphoraBlock.java")
    patch_pot(base / "PotBlock.java")
    print(f"Patched {ver}")
