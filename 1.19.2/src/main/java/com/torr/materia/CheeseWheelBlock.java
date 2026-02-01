package com.torr.materia;

import com.torr.materia.blockentity.CheeseWheelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class CheeseWheelBlock extends Block implements EntityBlock {
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 7);
    private static final TagKey<Item> CUTTING_TOOLS = ItemTags.create(new ResourceLocation(materia.MOD_ID, "all_cutting_tools"));
    private static final VoxelShape[] SHAPES = new VoxelShape[] {
        Block.box(0, 0, 0, 16, 8, 16),
        Block.box(2, 0, 0, 16, 8, 16),
        Block.box(4, 0, 0, 16, 8, 16),
        Block.box(6, 0, 0, 16, 8, 16),
        Block.box(8, 0, 0, 16, 8, 16),
        Block.box(10, 0, 0, 16, 8, 16),
        Block.box(12, 0, 0, 16, 8, 16),
        Block.box(14, 0, 0, 16, 8, 16)
    };

    public CheeseWheelBlock(Properties props) {
        super(props);
        registerDefaultState(stateDefinition.any().setValue(BITES, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BITES);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        int bites = state.getValue(BITES);

        boolean isAged = state.is(ModBlocks.AGED_CHEESE_WHEEL.get());
        ItemStack held = player.getItemInHand(hand);
        boolean hasCuttingTool = held.is(CUTTING_TOOLS);

        if (!level.isClientSide) {
            if (!isAged && !hasCuttingTool) return InteractionResult.PASS;

            if (hasCuttingTool) {
                ItemStack wedge = new ItemStack(ModItems.CHEESE_WEDGE.get());
                if (!player.addItem(wedge)) player.drop(wedge, false);
                if (!player.getAbilities().instabuild && held.isDamageableItem()) {
                    held.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                }
            } else {
                if (!player.canEat(false)) return InteractionResult.PASS;
                if (isAged) {
                    player.getFoodData().eat(3, 0.4F);
                } else {
                    player.getFoodData().eat(2, 0.2F);
                }
                level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 1.0F);
            }

            int newBites = bites + 1;
            if (newBites >= 8) {
                level.removeBlock(pos, false);
            } else {
                BlockState ns = state.setValue(BITES, newBites);
                level.setBlock(pos, ns, 3);
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof CheeseWheelBlockEntity wheelBe) {
                    wheelBe.setBites(newBites);
                }
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(BITES)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(BITES)];
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CheeseWheelBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : (l, p, s, be) -> {
            if (be instanceof CheeseWheelBlockEntity wheel) wheel.tick();
        };
    }
}

