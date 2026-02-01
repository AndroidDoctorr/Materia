package com.torr.materia;

import com.torr.materia.blockentity.FirePitBlockEntity;
import com.torr.materia.blockentity.CokeOvenBlockEntity;
import com.torr.materia.blockentity.BriningVatBlockEntity;
import com.torr.materia.blockentity.KilnBlockEntity;
import com.torr.materia.blockentity.OvenBlockEntity;
import com.torr.materia.blockentity.DryingRackBlockEntity;
import com.torr.materia.blockentity.CheeseWheelBlockEntity;
import com.torr.materia.blockentity.WaterPotBlockEntity;
import com.torr.materia.blockentity.MilkPotBlockEntity;
import com.torr.materia.blockentity.BeerPotBlockEntity;
import com.torr.materia.blockentity.WinePotBlockEntity;
import com.torr.materia.blockentity.FishTrapBlockEntity;
import com.torr.materia.blockentity.TableBlockEntity;
import com.torr.materia.blockentity.AmphoraBlockEntity;
import com.torr.materia.blockentity.TimerBlockEntity;
import com.torr.materia.blockentity.CannonBlockEntity;
import com.torr.materia.blockentity.SpinningWheelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
        public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
                        .create(ForgeRegistries.BLOCK_ENTITY_TYPES, materia.MOD_ID);

        public static final RegistryObject<BlockEntityType<FirePitBlockEntity>> FIRE_PIT_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("fire_pit_block_entity", () -> BlockEntityType.Builder.of(FirePitBlockEntity::new,
                                        ModBlocks.FIRE_PIT.get()).build(null));

        public static final RegistryObject<BlockEntityType<KilnBlockEntity>> KILN_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("kiln_block_entity", () -> BlockEntityType.Builder.of(KilnBlockEntity::new,
                                        ModBlocks.KILN.get(),
                                        ModBlocks.FURNACE_KILN.get(),
                                        ModBlocks.BLAST_FURNACE_KILN.get()).build(null));

        public static final RegistryObject<BlockEntityType<OvenBlockEntity>> OVEN_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("oven_block_entity", () -> BlockEntityType.Builder.of(OvenBlockEntity::new,
                                        ModBlocks.OVEN.get()).build(null));

        public static final RegistryObject<BlockEntityType<CokeOvenBlockEntity>> COKE_OVEN_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("coke_oven_block_entity", () -> BlockEntityType.Builder.of(CokeOvenBlockEntity::new,
                                        ModBlocks.COKE_OVEN.get()).build(null));

        public static final RegistryObject<BlockEntityType<TimerBlockEntity>> TIMER_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("timer_block_entity", () -> BlockEntityType.Builder.of(TimerBlockEntity::new,
                                        ModBlocks.TIMER.get()).build(null));

        public static final RegistryObject<BlockEntityType<WaterPotBlockEntity>> WATER_POT_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("water_pot_block_entity", () -> BlockEntityType.Builder.of(WaterPotBlockEntity::new,
                                        ModBlocks.WATER_POT.get()).build(null));

        public static final RegistryObject<BlockEntityType<MilkPotBlockEntity>> MILK_POT_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("milk_pot_block_entity", () -> BlockEntityType.Builder.of(MilkPotBlockEntity::new,
                                        ModBlocks.MILK_POT.get()).build(null));

        public static final RegistryObject<BlockEntityType<WinePotBlockEntity>> WINE_POT_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("wine_pot_block_entity", () -> BlockEntityType.Builder.of(WinePotBlockEntity::new,
                                        ModBlocks.WINE_POT.get()).build(null));

        public static final RegistryObject<BlockEntityType<BeerPotBlockEntity>> BEER_POT_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("beer_pot_block_entity", () -> BlockEntityType.Builder.of(BeerPotBlockEntity::new,
                                        ModBlocks.BEER_POT.get()).build(null));

        public static final RegistryObject<BlockEntityType<BriningVatBlockEntity>> BRINING_VAT_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("brining_vat_block_entity", () -> BlockEntityType.Builder.of(BriningVatBlockEntity::new,
                                        ModBlocks.BRINING_VAT.get()).build(null));

        public static final RegistryObject<BlockEntityType<CannonBlockEntity>> CANNON_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("cannon_block_entity", () -> BlockEntityType.Builder.of(CannonBlockEntity::new,
                                        ModBlocks.CANNON.get()).build(null));

        public static final RegistryObject<BlockEntityType<DryingRackBlockEntity>> DRYING_RACK_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("drying_rack_block_entity",
                                        () -> BlockEntityType.Builder.of(DryingRackBlockEntity::new,
                                                        ModBlocks.DRYING_RACK.get()).build(null));

        public static final RegistryObject<BlockEntityType<CheeseWheelBlockEntity>> CHEESE_WHEEL_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("cheese_wheel_block_entity",
                                        () -> BlockEntityType.Builder.of(CheeseWheelBlockEntity::new,
                                                        ModBlocks.FRESH_CHEESE_WHEEL.get(),
                                                        ModBlocks.AGED_CHEESE_WHEEL.get()).build(null));

        public static final RegistryObject<BlockEntityType<com.torr.materia.blockentity.FrameLoomBlockEntity>> FRAME_LOOM_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("frame_loom_block_entity",
                                        () -> BlockEntityType.Builder.of(com.torr.materia.blockentity.FrameLoomBlockEntity::new,
                                                        ModBlocks.FRAME_LOOM.get()).build(null));

        public static final RegistryObject<BlockEntityType<SpinningWheelBlockEntity>> SPINNING_WHEEL_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("spinning_wheel_block_entity",
                                        () -> BlockEntityType.Builder.of(SpinningWheelBlockEntity::new,
                                                        ModBlocks.SPINNING_WHEEL.get()).build(null));

        public static final RegistryObject<BlockEntityType<FishTrapBlockEntity>> FISH_TRAP_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("fish_trap_block_entity",
                                        () -> BlockEntityType.Builder.of(FishTrapBlockEntity::new,
                                                        ModBlocks.FISH_TRAP.get()).build(null));

        public static final RegistryObject<BlockEntityType<com.torr.materia.blockentity.StoneAnvilBlockEntity>> STONE_ANVIL_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("stone_anvil_block_entity",
                                        () -> BlockEntityType.Builder.of(com.torr.materia.blockentity.StoneAnvilBlockEntity::new,
                                                        ModBlocks.STONE_ANVIL.get()).build(null));

        public static final RegistryObject<BlockEntityType<com.torr.materia.blockentity.BronzeAnvilBlockEntity>> BRONZE_ANVIL_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("bronze_anvil_block_entity",
                                        () -> BlockEntityType.Builder.of(com.torr.materia.blockentity.BronzeAnvilBlockEntity::new,
                                                        ModBlocks.BRONZE_ANVIL.get()).build(null));

        public static final RegistryObject<BlockEntityType<com.torr.materia.blockentity.IronAnvilBlockEntity>> IRON_ANVIL_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("iron_anvil_block_entity",
                                        () -> BlockEntityType.Builder.of(com.torr.materia.blockentity.IronAnvilBlockEntity::new,
                                                        ModBlocks.IRON_ANVIL.get()).build(null));

        public static final RegistryObject<BlockEntityType<TableBlockEntity>> TABLE_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("table_block_entity",
                                        () -> BlockEntityType.Builder.of(TableBlockEntity::new,
                                                        ModBlocks.OAK_TABLE.get(),
                                                        ModBlocks.SPRUCE_TABLE.get(),
                                                        ModBlocks.BIRCH_TABLE.get(),
                                                        ModBlocks.JUNGLE_TABLE.get(),
                                                        ModBlocks.ACACIA_TABLE.get(),
                                                        ModBlocks.DARK_OAK_TABLE.get(),
                                                        ModBlocks.RUBBER_WOOD_TABLE.get()).build(null));

        public static final RegistryObject<BlockEntityType<AmphoraBlockEntity>> AMPHORA_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("amphora_block_entity",
                                        () -> BlockEntityType.Builder.of(AmphoraBlockEntity::new,
                                                        ModBlocks.AMPHORA.get()).build(null));

        public static final RegistryObject<BlockEntityType<com.torr.materia.blockentity.BasketBlockEntity>> BASKET_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("basket_block_entity",
                                        () -> BlockEntityType.Builder.of(com.torr.materia.blockentity.BasketBlockEntity::new,
                                                        ModBlocks.BASKET.get()).build(null));
}