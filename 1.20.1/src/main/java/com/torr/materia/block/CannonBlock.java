package com.torr.materia.block;

import com.torr.materia.blockentity.CannonBlockEntity;
import com.torr.materia.ModBlocks;
import com.torr.materia.materia;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.server.level.ServerPlayer;
import com.torr.materia.network.NetworkHandler;
import com.torr.materia.network.CannonStartAimPacket;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;

public class CannonBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final TagKey<Item> CANNON_AMMO = ItemTags.create(new ResourceLocation(materia.MOD_ID, "cannon_ammo"));

    public CannonBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CannonBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);
        if (level.isClientSide) {
            // Only claim the interaction client-side if we'd actually handle it.
            // Otherwise, return PASS so other mods' tools (e.g. wrenches) can work.
            if ((player.isShiftKeyDown() && held.isEmpty()) || held.isEmpty() || isPowderItem(held) || isAmmoItem(held)) {
                return InteractionResult.sidedSuccess(true);
            }
            return InteractionResult.PASS;
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof CannonBlockEntity cannonBe)) {
            return InteractionResult.PASS;
        }

        // If the cannon is already charged + loaded, interacting with ammo/powder should just tell the player it's ready
        if (cannonBe.hasPowder() && cannonBe.hasAmmo()) {
            if (held.is(Items.GUNPOWDER) || isAmmoItem(held)) {
                player.displayClientMessage(Component.literal("Cannon is loaded!"), true);
                return InteractionResult.SUCCESS;
            }
        }

        // Sneak-right-click resets aim
        if (player.isShiftKeyDown()) {
            // Don't steal sneak-right-click from other mods' tools/items.
            if (!held.isEmpty()) return InteractionResult.PASS;
            cannonBe.setAimDegrees(0f, 0f);
            level.sendBlockUpdated(pos, state, state, 3);
            return InteractionResult.SUCCESS;
        }

        // Charging with gunpowder (or gunpowder trail item) (up to 4)
        if (isPowderItem(held)) {
            if (cannonBe.hasAmmo() && cannonBe.hasPowder()) {
                player.displayClientMessage(Component.literal("Cannon is loaded!"), true);
                return InteractionResult.SUCCESS;
            }
            if (cannonBe.isFullyCharged()) {
                player.displayClientMessage(Component.literal("Cannon is fully charged!"), true);
                return InteractionResult.SUCCESS;
            }
            cannonBe.addPowder(1);
            if (!player.getAbilities().instabuild) {
                held.shrink(1);
            }
            level.sendBlockUpdated(pos, state, state, 3);
            return InteractionResult.SUCCESS;
        }

        // Loading ammo (only allowed if it has powder)
        if (isAmmoItem(held)) {
            if (!cannonBe.hasPowder()) {
                player.displayClientMessage(Component.literal("Cannon has no powder!"), true);
                return InteractionResult.SUCCESS;
            }
            if (cannonBe.hasAmmo()) {
                player.displayClientMessage(Component.literal("Cannon is loaded!"), true);
                return InteractionResult.SUCCESS;
            }

            cannonBe.setAmmo(held);
            if (!player.getAbilities().instabuild) {
                held.shrink(1);
            }
            level.sendBlockUpdated(pos, state, state, 3);
            return InteractionResult.SUCCESS;
        }

        // Empty hand interaction: show missing state, or aim if ready
        if (held.isEmpty()) {
            if (!cannonBe.hasPowder()) {
                player.displayClientMessage(Component.literal("Cannon has no powder!"), true);
                return InteractionResult.SUCCESS;
            }
            if (!cannonBe.hasAmmo()) {
                player.displayClientMessage(Component.literal("Cannon has no ammo!"), true);
                return InteractionResult.SUCCESS;
            }
        }

        // Aiming (only when charged + loaded)
        if (held.isEmpty() && cannonBe.hasPowder() && cannonBe.hasAmmo()) {
            if (player instanceof ServerPlayer sp) {
                // Start each aim session from a known orientation (not permanently stuck)
                cannonBe.setAimDegrees(0f, 85f);
                level.sendBlockUpdated(pos, state, state, 3);
                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> sp), new CannonStartAimPacket(pos));
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    private static boolean isAmmoItem(ItemStack stack) {
        return stack.is(CANNON_AMMO) || stack.is(Items.TNT);
    }

    private static boolean isPowderItem(ItemStack stack) {
        return stack.is(Items.GUNPOWDER) || stack.is(ModBlocks.GUNPOWDER_TRAIL.get().asItem());
    }
}

