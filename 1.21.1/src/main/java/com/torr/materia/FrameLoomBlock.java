package com.torr.materia;

import com.torr.materia.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import javax.annotation.Nullable;
import com.torr.materia.blockentity.FrameLoomBlockEntity;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Random;

public class FrameLoomBlock extends Block implements SimpleWaterloggedBlock, EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 8);

    // Define shapes for different orientations
    protected static final VoxelShape SHAPE_EAST_WEST = Block.box(5.0D, 0.0D, 0.0D, 11.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_NORTH_SOUTH = Block.box(0.0D, 0.0D, 5.0D, 16.0D, 16.0D, 11.0D);

    // Linen fibers tag
    private static final TagKey<Item> LINEN_FIBERS = ForgeRegistries.ITEMS.tags().createTagKey(ResourceLocation.fromNamespaceAndPath("materia", "strings"));

    public FrameLoomBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false)
                .setValue(STAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, STAGE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        return defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected net.minecraft.world.ItemInteractionResult useItemOn(ItemStack held, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        InteractionResult result = handleUse(state, level, pos, player, held);
        if (result == InteractionResult.PASS) return net.minecraft.world.ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (result == InteractionResult.FAIL) return net.minecraft.world.ItemInteractionResult.FAIL;
        return net.minecraft.world.ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return handleUse(state, level, pos, player, player.getItemInHand(InteractionHand.MAIN_HAND));
    }

    private InteractionResult handleUse(BlockState state, Level level, BlockPos pos, Player player, ItemStack held) {
        int currentStage = state.getValue(STAGE);
        
        if (currentStage == 8) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                ItemStack carpet = new ItemStack(Items.WHITE_CARPET);
                if (be instanceof FrameLoomBlockEntity loomBe) {
                    Item selected = loomBe.getSelectedOutputItem();
                    if (selected != null) {
                        carpet = new ItemStack(selected);
                    }
                }
                if (!player.addItem(carpet)) {
                    player.drop(carpet, false);
                }
                level.setBlock(pos, state.setValue(STAGE, 0), 3);
                if (be instanceof FrameLoomBlockEntity loomBe) {
                    loomBe.clearSelection();
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        
        // Check if the item is an allowed string (only for stages 0-7)
        if (!held.is(LINEN_FIBERS)) {
            return InteractionResult.PASS;
        }
        
        if (currentStage < 8) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof FrameLoomBlockEntity loomBe) {
                    // If no selection yet (stage 0), set selection based on first string used
                    Item existing = loomBe.getSelectedOutputItem();
                    if (existing == null) {
                        Item out = mapStringToCarpet(held.getItem());
                        loomBe.setSelectedOutputItem(out);
                    } else {
                        // Enforce same color: reject if the incoming string maps to different carpet item
                        Item incomingOut = mapStringToCarpet(held.getItem());
                        if (incomingOut != existing) {
                            return InteractionResult.PASS;
                        }
                    }
                }
                level.setBlock(pos, state.setValue(STAGE, currentStage + 1), 3);
                held.shrink(1);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        
        return InteractionResult.PASS;
    }

    private Item mapStringToCarpet(Item stringItem) {
        // Natural/vanilla string should produce taupe carpet (white is harder)
        if (stringItem == Items.STRING) return ModBlocks.TAUPE_CARPET.get().asItem();

        // Vanilla colors
        if (stringItem == ModItems.WHITE_STRING.get()) return Items.WHITE_CARPET;
        if (stringItem == ModItems.LIGHT_GRAY_STRING.get()) return Items.LIGHT_GRAY_CARPET;
        if (stringItem == ModItems.GRAY_STRING.get()) return Items.GRAY_CARPET;
        if (stringItem == ModItems.BLACK_STRING.get()) return Items.BLACK_CARPET;
        if (stringItem == ModItems.BROWN_STRING.get()) return Items.BROWN_CARPET;
        if (stringItem == ModItems.RED_STRING.get()) return Items.RED_CARPET;
        if (stringItem == ModItems.ORANGE_STRING.get()) return Items.ORANGE_CARPET;
        if (stringItem == ModItems.YELLOW_STRING.get()) return Items.YELLOW_CARPET;
        if (stringItem == ModItems.LIME_STRING.get()) return Items.LIME_CARPET;
        if (stringItem == ModItems.GREEN_STRING.get()) return Items.GREEN_CARPET;
        if (stringItem == ModItems.CYAN_STRING.get()) return Items.CYAN_CARPET;
        if (stringItem == ModItems.LIGHT_BLUE_STRING.get()) return Items.LIGHT_BLUE_CARPET;
        if (stringItem == ModItems.BLUE_STRING.get()) return Items.BLUE_CARPET;
        if (stringItem == ModItems.PURPLE_STRING.get()) return Items.PURPLE_CARPET;
        if (stringItem == ModItems.MAGENTA_STRING.get()) return Items.MAGENTA_CARPET;
        if (stringItem == ModItems.PINK_STRING.get()) return Items.PINK_CARPET;

        // Custom colors
        if (stringItem == ModItems.OCHRE_STRING.get()) return ModBlocks.OCHRE_CARPET.get().asItem();
        if (stringItem == ModItems.RED_OCHRE_STRING.get()) return ModBlocks.RED_OCHRE_CARPET.get().asItem();
        if (stringItem == ModItems.OLIVE_STRING.get()) return ModBlocks.OLIVE_CARPET.get().asItem();
        if (stringItem == ModItems.INDIGO_STRING.get()) return ModBlocks.INDIGO_CARPET.get().asItem();
        if (stringItem == ModItems.LAVENDER_STRING.get()) return ModBlocks.LAVENDER_CARPET.get().asItem();
        if (stringItem == ModItems.TYRIAN_PURPLE_STRING.get()) return ModBlocks.TYRIAN_PURPLE_CARPET.get().asItem();
        if (stringItem == ModItems.CHARCOAL_GRAY_STRING.get()) return ModBlocks.CHARCOAL_GRAY_CARPET.get().asItem();

        // Fallback = Taupe
        return ModBlocks.TAUPE_CARPET.get().asItem();
    }

    private boolean isStringNamed(Item item, String path) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
        return id != null && id.getNamespace().equals("materia") && id.getPath().equals(path);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FrameLoomBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return facing.getAxis() == Direction.Axis.X ? SHAPE_EAST_WEST : SHAPE_NORTH_SOUTH;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return facing.getAxis() == Direction.Axis.X ? SHAPE_EAST_WEST : SHAPE_NORTH_SOUTH;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return facing.getAxis() == Direction.Axis.X ? SHAPE_EAST_WEST : SHAPE_NORTH_SOUTH;
    }
    
    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return super.getDrops(state, builder);
    }
}
