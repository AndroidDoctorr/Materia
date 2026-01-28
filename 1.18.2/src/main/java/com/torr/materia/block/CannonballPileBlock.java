package com.torr.materia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.function.Supplier;

public class CannonballPileBlock extends Block {
    public static final IntegerProperty COUNT = IntegerProperty.create("count", 1, 14);

    private static final VoxelShape LAYER_1 = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 5.0D, 15.0D);
    private static final VoxelShape LAYER_2 = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 10.0D, 15.0D);
    private static final VoxelShape LAYER_3 = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

    private final Supplier<Item> cannonballItem;

    public CannonballPileBlock(Supplier<Item> cannonballItem, Properties properties) {
        super(properties);
        this.cannonballItem = cannonballItem;
        registerDefaultState(stateDefinition.any().setValue(COUNT, 1));
    }

    public Item getCannonballItem() {
        return cannonballItem.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COUNT);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int count = state.getValue(COUNT);
        if (count >= 14) return LAYER_3;
        if (count >= 10) return LAYER_2;
        return LAYER_1;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);
        Item ammo = cannonballItem.get();

        int count = state.getValue(COUNT);

        // Shift-right-click with cannonballs adds to the pile (any face).
        if (player.isShiftKeyDown()) {
            if (held.isEmpty() || held.getItem() != ammo) {
                return InteractionResult.PASS;
            }
            if (count >= 14) {
                return InteractionResult.PASS;
            }

            if (!level.isClientSide) {
                BlockState next = state.setValue(COUNT, count + 1);
                level.setBlock(pos, next, 2);

                SoundType sound = next.getSoundType(level, pos, player);
                level.playSound(null, pos, sound.getPlaceSound(), SoundSource.BLOCKS, 0.8F, 1.0F);

                if (!player.isCreative()) {
                    held.shrink(1);
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Normal right-click removes one (any face).
        if (!held.isEmpty() && held.getItem() != ammo) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            boolean placedInHand = false;

            if (held.isEmpty()) {
                player.setItemInHand(hand, new ItemStack(ammo, 1));
                placedInHand = true;
            } else if (held.getCount() < held.getMaxStackSize()) {
                held.grow(1);
                placedInHand = true;
            }

            if (!placedInHand) {
                player.getInventory().placeItemBackInInventory(new ItemStack(ammo, 1));
            }

            if (count <= 1) {
                level.removeBlock(pos, false);
            } else {
                level.setBlock(pos, state.setValue(COUNT, count - 1), 2);
            }

            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.6F, 1.2F);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        int count = state.getValue(COUNT);
        return List.of(new ItemStack(cannonballItem.get(), count));
    }
}

