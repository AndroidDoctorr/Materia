package com.torr.materia;

import com.torr.materia.blockentity.AmphoraBlockEntity;
import com.torr.materia.entity.FallingAmphoraEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.network.NetworkHooks;
import java.util.Random;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AmphoraBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<LiquidType> LIQUID_TYPE = EnumProperty.create("liquid_type", LiquidType.class);
    public static final BooleanProperty CLOSED = BooleanProperty.create("closed");

    protected static final VoxelShape AMPHORA_SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);

    public enum LiquidType implements StringRepresentable {
        EMPTY("empty"),
        WATER("water"),
        WINE("wine"),
        VINEGAR("vinegar"),
        OLIVE_OIL("olive_oil"),
        LAVA("lava"),
        GRAPE_JUICE("grape_juice"),
        MILK("milk"),
        BEER_MASH("beer_mash"),
        BEER("beer");

        private final String name;

        LiquidType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

    public AmphoraBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(WATERLOGGED, false).setValue(LIQUID_TYPE, LiquidType.EMPTY).setValue(CLOSED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AMPHORA_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AMPHORA_SHAPE;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AMPHORA_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, LIQUID_TYPE, CLOSED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        return defaultBlockState().setValue(WATERLOGGED, fluid.getType() == Fluids.WATER).setValue(LIQUID_TYPE, LiquidType.EMPTY).setValue(CLOSED, false);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    // Falling block functionality - we need to extend FallingBlock for this
    // but also need BaseEntityBlock for block entities. We'll handle falling manually.

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        level.scheduleTick(pos, this, this.getDelayAfterPlace());
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        if (FallingBlock.isFree(level.getBlockState(pos.below())) && pos.getY() >= level.getMinBuildHeight()) {
            // Capture block entity data before removing the block
            CompoundTag blockEntityData = null;
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof AmphoraBlockEntity) {
                blockEntityData = blockEntity.saveWithoutMetadata();
            }

            // Create our custom falling entity with the block entity data
            FallingAmphoraEntity fallingAmphora = new FallingAmphoraEntity(level,
                (double)pos.getX() + 0.5D,
                (double)pos.getY(),
                (double)pos.getZ() + 0.5D,
                level.getBlockState(pos),
                blockEntityData);

            this.configureFallingBlockEntity(fallingAmphora);
            level.addFreshEntity(fallingAmphora);
            level.removeBlock(pos, false);
        }
    }

    protected void configureFallingBlockEntity(FallingAmphoraEntity entity) {
        // Configure the falling entity if needed
        // This can be overridden for custom behavior
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, this.getDelayAfterPlace());
        }
    }

    protected int getDelayAfterPlace() {
        return 2;
    }

    // Block Entity Methods
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AmphoraBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.AMPHORA_BLOCK_ENTITY.get(),
                (level1, pos, state1, blockEntity) -> AmphoraBlockEntity.tick(level1, pos, state1, blockEntity));
    }

    // GUI Interaction
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);
        BlockEntity entity = level.getBlockEntity(pos);
        if (!(entity instanceof AmphoraBlockEntity amphoraEntity)) {
            return InteractionResult.PASS;
        }

        // Prevent all interactions with fermenting amphorae
        if (amphoraEntity.isFermenting()) {
            return InteractionResult.PASS;
        }

        // Handle lid interactions first
        InteractionResult lidResult = handleLidInteractions(state, level, pos, player, hand, held, amphoraEntity);
        if (lidResult != InteractionResult.PASS) {
            return lidResult;
        }

        // Handle liquid interactions
        InteractionResult liquidResult = handleLiquidInteractions(state, level, pos, player, hand, held, amphoraEntity);
        if (liquidResult != InteractionResult.PASS) {
            return liquidResult;
        }

        // Handle GUI opening for solid storage
        if (!level.isClientSide()) {
            // Only open GUI if amphora is in solid storage mode or empty (and held item is empty)
            if (held.isEmpty() && amphoraEntity.canAcceptSolidItems()) {
                MenuProvider containerProvider = createMenuProvider(state, level, pos);
                NetworkHooks.openGui((ServerPlayer) player, containerProvider, pos);
                return InteractionResult.SUCCESS;
            } else if (amphoraEntity.getStorageMode() == AmphoraBlockEntity.MODE_LIQUID) {
                player.displayClientMessage(new TranslatableComponent("message.materia.amphora.liquid_mode"), true);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
    
    private InteractionResult handleLidInteractions(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack held, AmphoraBlockEntity amphoraEntity) {
        // Add lid with regular lid item
        if (held.is(ModItems.LID.get())) {
            if (!level.isClientSide() && !amphoraEntity.canApplyLidNow()) {
                player.displayClientMessage(amphoraEntity.getMashStatusMessage(), true);
                return InteractionResult.SUCCESS;
            }
            if (!level.isClientSide() && !amphoraEntity.hasLid()) {
                amphoraEntity.setLid(false); // regular lid
                held.shrink(1);
                updateBlockState(level, pos, amphoraEntity);
                level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                // Force sync to client for immediate particle updates
                level.sendBlockUpdated(pos, state, state, 3);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        
        // Add lid with sealed lid item
        if (held.is(ModItems.SEALED_LID.get())) {
            if (!level.isClientSide() && !amphoraEntity.canApplyLidNow()) {
                player.displayClientMessage(amphoraEntity.getMashStatusMessage(), true);
                return InteractionResult.SUCCESS;
            }
            if (!level.isClientSide() && !amphoraEntity.hasLid()) {
                amphoraEntity.setLid(true); // sealed lid
                held.shrink(1);
                updateBlockState(level, pos, amphoraEntity);
                level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                // Force sync to client for immediate particle updates
                level.sendBlockUpdated(pos, state, state, 3);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        
        // Remove lid with empty hand
        if (held.isEmpty() && amphoraEntity.hasLid()) {
            if (!level.isClientSide()) {
                ItemStack lidItem = amphoraEntity.getLidItem();
                amphoraEntity.removeLid();
                updateBlockState(level, pos, amphoraEntity);
                
                // Give lid to player or drop it
                if (!player.addItem(lidItem)) {
                    Block.popResource(level, pos, lidItem);
                }
                
                level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        
        return InteractionResult.PASS;
    }

    private InteractionResult handleLiquidInteractions(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack held, AmphoraBlockEntity amphoraEntity) {
        // Check if this is a liquid vessel and the amphora contains solid items
        if (amphoraEntity.getStorageMode() == AmphoraBlockEntity.MODE_SOLID && isLiquidVessel(held)) {
            if (!level.isClientSide()) {
                player.displayClientMessage(new TranslatableComponent("message.materia.amphora.solid_mode"), true);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        // Beer mash ingredient interactions (water + wheat/hops)
        if (held.is(net.minecraft.world.item.Items.WHEAT) || held.is(ModItems.HOPS.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.getStorageMode() == AmphoraBlockEntity.MODE_SOLID) {
                    player.displayClientMessage(new TranslatableComponent("message.materia.amphora.solid_mode"), true);
                    return InteractionResult.SUCCESS;
                }
                if (amphoraEntity.hasLid()) {
                    player.displayClientMessage(new TranslatableComponent("message.materia.amphora.liquid_mode"), true);
                    return InteractionResult.SUCCESS;
                }

                boolean consumed = amphoraEntity.tryAddBeerMashIngredient(held);
                if (consumed) {
                    held.shrink(1);
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.6F, 1.0F);
                    updateBlockState(level, pos, amphoraEntity);
                }

                player.displayClientMessage(amphoraEntity.getMashStatusMessage(), true);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        
        // Crucible interactions: empty crucible -> water cup, grape_juice, olive_oil, vinegar, or wine_cup (if enough liquid)
        if (held.is(ModItems.CRUCIBLE.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.hasWater() && amphoraEntity.removeLiquid(1)) {
                    ItemStack waterCup = new ItemStack(ModItems.WATER_CUP.get());
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, waterCup);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                } else if (amphoraEntity.hasGrapeJuice() && amphoraEntity.removeLiquid(1)) {
                    ItemStack grapeJuice = new ItemStack(ModItems.GRAPE_JUICE.get());
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, grapeJuice);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                } else if (amphoraEntity.hasOliveOil() && amphoraEntity.removeLiquid(1)) {
                    ItemStack oliveOil = new ItemStack(ModItems.OLIVE_OIL.get());
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, oliveOil);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                } else if (amphoraEntity.hasVinegar() && amphoraEntity.removeLiquid(1)) {
                    ItemStack vinegar = new ItemStack(ModItems.VINEGAR.get());
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, vinegar);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                } else if (amphoraEntity.hasWine() && amphoraEntity.removeLiquid(1)) {
                    ItemStack wineCup = new ItemStack(ModItems.WINE_CUP.get());
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, wineCup);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                } else if (amphoraEntity.hasMilk() && amphoraEntity.removeLiquid(1)) {
                    ItemStack milkCup = new ItemStack(ModItems.MILK_CUP.get());
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, milkCup);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                } else if (amphoraEntity.hasBeer() && amphoraEntity.removeLiquid(1)) {
                    ItemStack beerCup = new ItemStack(ModItems.BEER_CUP.get());
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, beerCup);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        // Water cup interactions: water cup -> crucible (add water)
        if (held.is(ModItems.WATER_CUP.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddWater()) {
                    amphoraEntity.addWater(1);
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

        // Empty pot interactions: pot -> water pot, grape_juice_pot, olive_oil_pot, vinegar_pot, or wine_pot (if enough liquid)
        if (held.is(ModItems.POT.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.hasWater() && amphoraEntity.getLiquidAmount() >= 3 && amphoraEntity.removeLiquid(3)) {
                    ItemStack waterPot = new ItemStack(ModItems.WATER_POT.get());
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, waterPot);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                } else if (amphoraEntity.hasGrapeJuice() && amphoraEntity.getLiquidAmount() >= 3 && amphoraEntity.removeLiquid(3)) {
                    ItemStack grapeJuicePot = new ItemStack(ModItems.GRAPE_JUICE_POT.get());
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, grapeJuicePot);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                } else if (amphoraEntity.hasOliveOil() && amphoraEntity.getLiquidAmount() >= 3 && amphoraEntity.removeLiquid(3)) {
                    ItemStack oliveOilPot = new ItemStack(ModItems.OLIVE_OIL_POT.get());
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, oliveOilPot);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                } else if (amphoraEntity.hasVinegar() && amphoraEntity.getLiquidAmount() >= 3 && amphoraEntity.removeLiquid(3)) {
                    ItemStack vinegarPot = new ItemStack(ModItems.VINEGAR_POT.get());
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, vinegarPot);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                } else if (amphoraEntity.hasWine() && amphoraEntity.getLiquidAmount() >= 3 && amphoraEntity.removeLiquid(3)) {
                    ItemStack winePot = new ItemStack(ModItems.WINE_POT.get());
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, winePot);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                } else if (amphoraEntity.hasMilk() && amphoraEntity.getLiquidAmount() >= 3 && amphoraEntity.removeLiquid(3)) {
                    ItemStack milkPot = new ItemStack(ModBlocks.MILK_POT.get());
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, milkPot);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                } else if (amphoraEntity.hasBeer() && amphoraEntity.getLiquidAmount() >= 3 && amphoraEntity.removeLiquid(3)) {
                    ItemStack beerPot = new ItemStack(ModItems.BEER_POT.get());
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, beerPot);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        // Water pot interactions: water pot -> pot (add 3 bottles worth of water)
        if (held.is(ModItems.WATER_POT.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddWater() && amphoraEntity.getLiquidAmount() <= 6) { // Can fit 3 more
                    amphoraEntity.addWater(3);
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

        // Bucket interactions: water bucket -> bucket (add 3 bottles worth), bucket -> water bucket (take 3 bottles worth)
        if (held.is(Items.WATER_BUCKET)) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddWater() && amphoraEntity.getLiquidAmount() <= 6) { // Can fit 3 more
                    amphoraEntity.addWater(3);
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

        if (held.is(Items.BUCKET)) {
            if (!level.isClientSide()) {
                if (amphoraEntity.hasWater() && amphoraEntity.getLiquidAmount() >= 3 && amphoraEntity.removeLiquid(3)) {
                    ItemStack waterBucket = new ItemStack(Items.WATER_BUCKET);
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, waterBucket);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                } else if (amphoraEntity.hasGrapeJuice() && amphoraEntity.getLiquidAmount() >= 3 && amphoraEntity.removeLiquid(3)) {
                    ItemStack grapeJuiceBucket = new ItemStack(ModItems.GRAPE_JUICE_BUCKET.get());
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, grapeJuiceBucket);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                } else if (amphoraEntity.hasOliveOil() && amphoraEntity.getLiquidAmount() >= 3 && amphoraEntity.removeLiquid(3)) {
                    ItemStack oliveOilBucket = new ItemStack(ModItems.OLIVE_OIL_BUCKET.get());
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, oliveOilBucket);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                } else if (amphoraEntity.hasVinegar() && amphoraEntity.getLiquidAmount() >= 3 && amphoraEntity.removeLiquid(3)) {
                    ItemStack vinegarBucket = new ItemStack(ModItems.VINEGAR_BUCKET.get());
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, vinegarBucket);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                } else if (amphoraEntity.hasWine() && amphoraEntity.getLiquidAmount() >= 3 && amphoraEntity.removeLiquid(3)) {
                    ItemStack wineBucket = new ItemStack(ModItems.WINE_BUCKET.get());
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, wineBucket);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                } else if (amphoraEntity.hasMilk() && amphoraEntity.getLiquidAmount() >= 3 && amphoraEntity.removeLiquid(3)) {
                    ItemStack milkBucket = new ItemStack(Items.MILK_BUCKET);
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, milkBucket);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                } else if (amphoraEntity.hasBeer() && amphoraEntity.getLiquidAmount() >= 3 && amphoraEntity.removeLiquid(3)) {
                    ItemStack beerBucket = new ItemStack(ModItems.BEER_BUCKET.get());
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack result = ItemUtils.createFilledResult(held, player, beerBucket);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        // Glass bottle interactions: bottle -> water bottle, grape_juice_bottle, olive_oil_bottle, vinegar_bottle, or wine_bottle (take 1)
        if (held.is(Items.GLASS_BOTTLE)) {
            if (!level.isClientSide()) {
                if (amphoraEntity.hasWater() && amphoraEntity.removeLiquid(1)) {
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack waterBottle = new ItemStack(Items.POTION);
                    PotionUtils.setPotion(waterBottle, Potions.WATER);
                    ItemStack result = ItemUtils.createFilledResult(held, player, waterBottle);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                } else if (amphoraEntity.hasGrapeJuice() && amphoraEntity.removeLiquid(1)) {
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack grapeJuiceBottle = new ItemStack(ModItems.GRAPE_JUICE_BOTTLE.get());
                    ItemStack result = ItemUtils.createFilledResult(held, player, grapeJuiceBottle);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                } else if (amphoraEntity.hasOliveOil() && amphoraEntity.removeLiquid(1)) {
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack oliveOilBottle = new ItemStack(ModItems.OLIVE_OIL_BOTTLE.get());
                    ItemStack result = ItemUtils.createFilledResult(held, player, oliveOilBottle);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                } else if (amphoraEntity.hasVinegar() && amphoraEntity.removeLiquid(1)) {
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack vinegarBottle = new ItemStack(ModItems.VINEGAR_BOTTLE.get());
                    ItemStack result = ItemUtils.createFilledResult(held, player, vinegarBottle);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                } else if (amphoraEntity.hasWine() && amphoraEntity.removeLiquid(1)) {
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack wineBottle = new ItemStack(ModItems.WINE_BOTTLE.get());
                    ItemStack result = ItemUtils.createFilledResult(held, player, wineBottle);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                } else if (amphoraEntity.hasMilk() && amphoraEntity.removeLiquid(1)) {
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack milkBottle = new ItemStack(ModItems.MILK_BOTTLE.get());
                    ItemStack result = ItemUtils.createFilledResult(held, player, milkBottle);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                } else if (amphoraEntity.hasBeer() && amphoraEntity.removeLiquid(1)) {
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemStack beerBottle = new ItemStack(ModItems.BEER_BOTTLE.get());
                    ItemStack result = ItemUtils.createFilledResult(held, player, beerBottle);
                    player.setItemInHand(hand, result);
                    updateBlockState(level, pos, amphoraEntity);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        if (held.is(Items.POTION) && PotionUtils.getPotion(held) == Potions.WATER) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddWater()) {
                    amphoraEntity.addWater(1);
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

        // Grape juice interactions: grape_juice (crucible) -> crucible (add grape juice)
        if (held.is(ModItems.GRAPE_JUICE.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddGrapeJuice()) {
                    amphoraEntity.addGrapeJuice(1);
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

        // Grape juice pot interactions: grape_juice_pot -> pot (add 3 bottles worth)
        if (held.is(ModItems.GRAPE_JUICE_POT.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddGrapeJuice() && amphoraEntity.getLiquidAmount() <= 6) { // Can fit 3 more
                    amphoraEntity.addGrapeJuice(3);
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

        // Grape juice bucket interactions: grape_juice_bucket -> bucket (add 3 bottles worth)
        if (held.is(ModItems.GRAPE_JUICE_BUCKET.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddGrapeJuice() && amphoraEntity.getLiquidAmount() <= 6) { // Can fit 3 more
                    amphoraEntity.addGrapeJuice(3);
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

        // Grape juice bottle interactions: grape_juice_bottle -> glass_bottle (add 1 bottle worth)
        if (held.is(ModItems.GRAPE_JUICE_BOTTLE.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddGrapeJuice()) {
                    amphoraEntity.addGrapeJuice(1);
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

        // Olive oil interactions: olive_oil (crucible) -> crucible (add olive oil)
        if (held.is(ModItems.OLIVE_OIL.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddOliveOil()) {
                    amphoraEntity.addOliveOil(1);
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

        // Olive oil pot interactions: olive_oil_pot -> pot (add 3 bottles worth)
        if (held.is(ModItems.OLIVE_OIL_POT.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddOliveOil() && amphoraEntity.getLiquidAmount() <= 6) { // Can fit 3 more
                    amphoraEntity.addOliveOil(3);
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

        // Olive oil bucket interactions: olive_oil_bucket -> bucket (add 3 bottles worth)
        if (held.is(ModItems.OLIVE_OIL_BUCKET.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddOliveOil() && amphoraEntity.getLiquidAmount() <= 6) { // Can fit 3 more
                    amphoraEntity.addOliveOil(3);
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

        // Olive oil bottle interactions: olive_oil_bottle -> glass_bottle (add 1 bottle worth)
        if (held.is(ModItems.OLIVE_OIL_BOTTLE.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddOliveOil()) {
                    amphoraEntity.addOliveOil(1);
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

        // Vinegar interactions: vinegar (crucible) -> crucible (add vinegar)
        if (held.is(ModItems.VINEGAR.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddVinegar()) {
                    amphoraEntity.addVinegar(1);
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

        // Vinegar pot interactions: vinegar_pot -> pot (add 3 bottles worth)
        if (held.is(ModItems.VINEGAR_POT.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddVinegar() && amphoraEntity.getLiquidAmount() <= 6) { // Can fit 3 more
                    amphoraEntity.addVinegar(3);
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

        // Vinegar bucket interactions: vinegar_bucket -> bucket (add 3 bottles worth)
        if (held.is(ModItems.VINEGAR_BUCKET.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddVinegar() && amphoraEntity.getLiquidAmount() <= 6) { // Can fit 3 more
                    amphoraEntity.addVinegar(3);
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

        // Vinegar bottle interactions: vinegar_bottle -> glass_bottle (add 1 bottle worth)
        if (held.is(ModItems.VINEGAR_BOTTLE.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddVinegar()) {
                    amphoraEntity.addVinegar(1);
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

        // Wine interactions: wine_cup (crucible) -> crucible (add wine)
        if (held.is(ModItems.WINE_CUP.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddWine()) {
                    amphoraEntity.addWine(1);
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

        // Beer interactions: beer_cup -> crucible (add beer)
        if (held.is(ModItems.BEER_CUP.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddBeer()) {
                    amphoraEntity.addBeer(1);
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

        // Wine pot interactions: wine_pot -> pot (add 3 bottles worth)
        if (held.is(ModItems.WINE_POT.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddWine() && amphoraEntity.getLiquidAmount() <= 6) { // Can fit 3 more
                    amphoraEntity.addWine(3);
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

        // Beer pot interactions: beer_pot -> pot (add 3 bottles worth)
        if (held.is(ModItems.BEER_POT.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddBeer() && amphoraEntity.getLiquidAmount() <= 6) { // Can fit 3 more
                    amphoraEntity.addBeer(3);
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

        // Wine bucket interactions: wine_bucket -> bucket (add 3 bottles worth)
        if (held.is(ModItems.WINE_BUCKET.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddWine() && amphoraEntity.getLiquidAmount() <= 6) { // Can fit 3 more
                    amphoraEntity.addWine(3);
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

        // Beer bucket interactions: beer_bucket -> bucket (add 3 bottles worth)
        if (held.is(ModItems.BEER_BUCKET.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddBeer() && amphoraEntity.getLiquidAmount() <= 6) { // Can fit 3 more
                    amphoraEntity.addBeer(3);
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

        // Wine bottle interactions: wine_bottle -> glass_bottle (add 1 bottle worth)
        if (held.is(ModItems.WINE_BOTTLE.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddWine()) {
                    amphoraEntity.addWine(1);
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

        // Beer bottle interactions: beer_bottle -> glass_bottle (add 1 bottle worth)
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

        // Milk cup interactions: milk_cup -> crucible (add milk)
        if (held.is(ModItems.MILK_CUP.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddMilk()) {
                    amphoraEntity.addMilk(1);
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

        // Milk pot interactions: milk_pot block -> pot item (add 3 bottles worth)
        if (held.is(ModBlocks.MILK_POT.get().asItem())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddMilk() && amphoraEntity.getLiquidAmount() <= 6) { // Can fit 3 more
                    amphoraEntity.addMilk(3);
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

        // Milk bucket interactions: vanilla milk_bucket -> bucket (add 3 bottles worth)
        if (held.is(Items.MILK_BUCKET)) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddMilk() && amphoraEntity.getLiquidAmount() <= 6) { // Can fit 3 more
                    amphoraEntity.addMilk(3);
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

        // Milk bottle interactions: milk_bottle -> glass_bottle (add 1 bottle worth)
        if (held.is(ModItems.MILK_BOTTLE.get())) {
            if (!level.isClientSide()) {
                if (amphoraEntity.canAddMilk()) {
                    amphoraEntity.addMilk(1);
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

        return InteractionResult.PASS;
    }
    
    private boolean isLiquidVessel(ItemStack stack) {
        // Check if the item is any kind of liquid vessel
        return stack.is(ModItems.CRUCIBLE.get()) ||
               stack.is(ModItems.WATER_CUP.get()) ||
               stack.is(ModItems.POT.get()) ||
               stack.is(ModItems.WATER_POT.get()) ||
               stack.is(Items.BUCKET) ||
               stack.is(Items.WATER_BUCKET) ||
               stack.is(Items.GLASS_BOTTLE) ||
               stack.is(Items.POTION) ||
               stack.is(ModItems.GRAPE_JUICE.get()) ||
               stack.is(ModItems.GRAPE_JUICE_POT.get()) ||
               stack.is(ModItems.GRAPE_JUICE_BUCKET.get()) ||
               stack.is(ModItems.GRAPE_JUICE_BOTTLE.get()) ||
               stack.is(ModItems.OLIVE_OIL.get()) ||
               stack.is(ModItems.OLIVE_OIL_POT.get()) ||
               stack.is(ModItems.OLIVE_OIL_BUCKET.get()) ||
               stack.is(ModItems.OLIVE_OIL_BOTTLE.get()) ||
               stack.is(ModItems.VINEGAR.get()) ||
               stack.is(ModItems.VINEGAR_POT.get()) ||
               stack.is(ModItems.VINEGAR_BUCKET.get()) ||
               stack.is(ModItems.VINEGAR_BOTTLE.get()) ||
               stack.is(ModItems.WINE_CUP.get()) ||
               stack.is(ModItems.WINE_POT.get()) ||
               stack.is(ModItems.WINE_BUCKET.get()) ||
               stack.is(ModItems.WINE_BOTTLE.get()) ||
               stack.is(ModItems.MILK_CUP.get()) ||
               stack.is(ModBlocks.MILK_POT.get().asItem()) ||
               stack.is(Items.MILK_BUCKET) ||
               stack.is(ModItems.MILK_BOTTLE.get()) ||
               stack.is(ModItems.BEER_CUP.get()) ||
               stack.is(ModItems.BEER_POT.get()) ||
               stack.is(ModItems.BEER_BUCKET.get()) ||
               stack.is(ModItems.BEER_BOTTLE.get());
    }

    private void updateBlockState(Level level, BlockPos pos, AmphoraBlockEntity amphoraEntity) {
        BlockState currentState = level.getBlockState(pos);
        BlockState newState = currentState;
        
        // Update blockstate based on lid status first
        newState = newState.setValue(CLOSED, amphoraEntity.hasLid());
        
        // Update blockstate based on liquid content (only if not closed)
        if (!amphoraEntity.hasLid()) {
            if (amphoraEntity.hasWater()) {
                newState = newState.setValue(LIQUID_TYPE, LiquidType.WATER);
            } else if (amphoraEntity.hasLiquid()) {
                String liquidType = amphoraEntity.getLiquidType();
                switch (liquidType) {
                    case "wine":
                        newState = newState.setValue(LIQUID_TYPE, LiquidType.WINE);
                        break;
                    case "vinegar":
                        newState = newState.setValue(LIQUID_TYPE, LiquidType.VINEGAR);
                        break;
                    case "olive_oil":
                        newState = newState.setValue(LIQUID_TYPE, LiquidType.OLIVE_OIL);
                        break;
                    case "lava":
                        newState = newState.setValue(LIQUID_TYPE, LiquidType.LAVA);
                        break;
                    case "grape_juice":
                        newState = newState.setValue(LIQUID_TYPE, LiquidType.GRAPE_JUICE);
                        break;
                    case "milk":
                        newState = newState.setValue(LIQUID_TYPE, LiquidType.MILK);
                        break;
                    case "beer_mash":
                        newState = newState.setValue(LIQUID_TYPE, LiquidType.BEER_MASH);
                        break;
                    case "beer":
                        newState = newState.setValue(LIQUID_TYPE, LiquidType.BEER);
                        break;
                    default:
                        newState = newState.setValue(LIQUID_TYPE, LiquidType.EMPTY);
                        break;
                }
            } else {
                newState = newState.setValue(LIQUID_TYPE, LiquidType.EMPTY);
            }
        }
        
        if (!currentState.equals(newState)) {
            level.setBlock(pos, newState, 3);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
        // Show fermentation particles if fermenting
        // animateTick only runs on client side, so no need to check
        
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof AmphoraBlockEntity amphoraEntity && amphoraEntity.isFermenting()) {
            if (random.nextInt(4) == 0) {
                double x = pos.getX() + 0.4 + random.nextDouble() * 0.2;
                double y = pos.getY() + 0.9;
                double z = pos.getZ() + 0.4 + random.nextDouble() * 0.2;
                
                double motionX = (random.nextDouble() - 0.5D) * 0.003D;
                double motionY = Math.abs(0.06D + random.nextDouble() * 0.03D);
                double motionZ = (random.nextDouble() - 0.5D) * 0.003D;
                
                level.addParticle(net.minecraft.core.particles.ParticleTypes.CLOUD, x, y, z, motionX, motionY, motionZ);
            }
        }
    }

    private MenuProvider createMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return new TranslatableComponent("container.materia.amphora");
            }

            @Override
            public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int windowId, net.minecraft.world.entity.player.Inventory playerInventory, Player player) {
                return new com.torr.materia.menu.AmphoraMenu(windowId, playerInventory, level.getBlockEntity(pos));
            }
        };
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return getAmphoraWithNBT(level, pos, state);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        // Get the block entity before calling super, as super will remove it
        AmphoraBlockEntity amphoraEntity = null;
        if (blockEntity instanceof AmphoraBlockEntity) {
            amphoraEntity = (AmphoraBlockEntity) blockEntity;
        }
        
        // Don't call super.playerDestroy as it will drop the default item via loot tables
        // Instead, manually drop our custom item with NBT
        if (!level.isClientSide && amphoraEntity != null) {
            ItemStack drop = getAmphoraWithNBTFromEntity(amphoraEntity, state);
            if (!drop.isEmpty()) {
                Block.popResource(level, pos, drop);
            }
        }
        
        // Remove the block
        level.removeBlock(pos, false);
    }
    
    private ItemStack getAmphoraWithNBT(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack stack = new ItemStack(this);
        
        // Save contents to the item's NBT (both liquid and solid storage, plus lid)
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof AmphoraBlockEntity amphoraEntity) {
            boolean hasContents = false;
            
            // Check for liquid contents
            if (amphoraEntity.getStorageMode() == AmphoraBlockEntity.MODE_LIQUID && amphoraEntity.hasLiquid()) {
                hasContents = true;
            }
            
            // Check for solid contents
            if (amphoraEntity.getStorageMode() == AmphoraBlockEntity.MODE_SOLID && !amphoraEntity.isEmpty()) {
                hasContents = true;
            }
            
            // Check for lid
            if (amphoraEntity.hasLid()) {
                hasContents = true;
            }
            
            // Save NBT if there are any contents or lid
            if (hasContents) {
                CompoundTag blockEntityTag = amphoraEntity.saveWithoutMetadata();
                
                CompoundTag itemTag = stack.getOrCreateTag();
                itemTag.put("BlockEntityTag", blockEntityTag);
            }
        }
        
        return stack;
    }
    
    private ItemStack getAmphoraWithNBTFromEntity(AmphoraBlockEntity amphoraEntity, BlockState state) {
        ItemStack stack = new ItemStack(this);
        
        // Save contents to the item's NBT (both liquid and solid storage, plus lid)
        boolean hasContents = false;
        
        // Check for liquid contents
        if (amphoraEntity.getStorageMode() == AmphoraBlockEntity.MODE_LIQUID && amphoraEntity.hasLiquid()) {
            hasContents = true;
        }
        
        // Check for solid contents
        if (amphoraEntity.getStorageMode() == AmphoraBlockEntity.MODE_SOLID && !amphoraEntity.isEmpty()) {
            hasContents = true;
        }
        
        // Check for lid
        if (amphoraEntity.hasLid()) {
            hasContents = true;
        }
        
        // Save NBT if there are any contents or lid
        if (hasContents) {
            CompoundTag blockEntityTag = amphoraEntity.saveWithoutMetadata();
            
            CompoundTag itemTag = stack.getOrCreateTag();
            itemTag.put("BlockEntityTag", blockEntityTag);
        }
        
        return stack;
    }

    // Handle non-player destruction (explosions, etc.)
    @Override
    public void wasExploded(Level level, BlockPos pos, net.minecraft.world.level.Explosion explosion) {
        // Drop contents when destroyed by explosion
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof AmphoraBlockEntity amphoraEntity) {
            if (amphoraEntity.getStorageMode() == AmphoraBlockEntity.MODE_SOLID) {
                Containers.dropContents(level, pos, amphoraEntity);
            } else if (amphoraEntity.getStorageMode() == AmphoraBlockEntity.MODE_LIQUID && amphoraEntity.hasLiquid()) {
                // Drop liquids as items when destroyed by explosion
                dropLiquidsAsItems(level, pos, amphoraEntity);
            }
        }
        super.wasExploded(level, pos, explosion);
    }
    
    // Drop contents when block is broken (only for non-player destruction)
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            // Note: Contents are now preserved in the item's NBT when picked up by players
            // The playerDestroy method handles normal player breaking with NBT preservation
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
    
    private void dropLiquidsAsItems(Level level, BlockPos pos, AmphoraBlockEntity amphoraEntity) {
        // Similar to FallingAmphoraEntity's dropStoredLiquids method
        if (amphoraEntity.hasWater() && amphoraEntity.getLiquidAmount() >= 3) {
            if (level.getBlockState(pos).isAir()) {
                level.setBlock(pos, net.minecraft.world.level.block.Blocks.WATER.defaultBlockState(), 3);
            }
        } else if (amphoraEntity.hasBeerMash()) {
            int wheat = amphoraEntity.getBeerMashWheat();
            int hops = amphoraEntity.getBeerMashHops();
            for (int i = 0; i < wheat; i++) {
                Block.popResource(level, pos, new ItemStack(Items.WHEAT));
            }
            for (int i = 0; i < hops; i++) {
                Block.popResource(level, pos, new ItemStack(ModItems.HOPS.get()));
            }
        } else if (amphoraEntity.hasGrapeJuice()) {
            int liquidAmount = amphoraEntity.getLiquidAmount();
            for (int i = 0; i < liquidAmount; i++) {
                Block.popResource(level, pos, new ItemStack(ModItems.GRAPE_JUICE.get()));
            }
        } else if (amphoraEntity.hasOliveOil()) {
            int liquidAmount = amphoraEntity.getLiquidAmount();
            for (int i = 0; i < liquidAmount; i++) {
                Block.popResource(level, pos, new ItemStack(ModItems.OLIVE_OIL.get()));
            }
        } else if (amphoraEntity.hasVinegar()) {
            int liquidAmount = amphoraEntity.getLiquidAmount();
            for (int i = 0; i < liquidAmount; i++) {
                Block.popResource(level, pos, new ItemStack(ModItems.VINEGAR.get()));
            }
        } else if (amphoraEntity.hasWine()) {
            int liquidAmount = amphoraEntity.getLiquidAmount();
            for (int i = 0; i < liquidAmount; i++) {
                Block.popResource(level, pos, new ItemStack(ModItems.WINE_CUP.get()));
            }
        } else if (amphoraEntity.hasBeer()) {
            int liquidAmount = amphoraEntity.getLiquidAmount();
            for (int i = 0; i < liquidAmount; i++) {
                Block.popResource(level, pos, new ItemStack(ModItems.BEER_CUP.get()));
            }
        }
    }
} 