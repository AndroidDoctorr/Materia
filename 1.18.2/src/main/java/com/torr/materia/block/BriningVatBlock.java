package com.torr.materia.block;

import com.torr.materia.ModBlockEntities;
import com.torr.materia.ModItems;
import com.torr.materia.blockentity.BriningVatBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Random;

public class BriningVatBlock extends BaseEntityBlock {
    public static final EnumProperty<Stage> STAGE = EnumProperty.create("stage", Stage.class);
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 8, 16);

    public enum Stage implements StringRepresentable {
        EMPTY("empty"),
        WATER("water"),
        SALT("salt");

        private final String name;

        Stage(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    public BriningVatBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, Stage.EMPTY));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BriningVatBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(STAGE);
    }

    @Override
    public VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
        if (state.getValue(STAGE) != Stage.WATER) return;

        // Similar feel to water pot / amphora particles, but lighter.
        if (random.nextInt(3) == 0) {
            double x = pos.getX() + 0.25 + random.nextDouble() * 0.5;
            double y = pos.getY() + 0.55;
            double z = pos.getZ() + 0.25 + random.nextDouble() * 0.5;

            double motionX = (random.nextDouble() - 0.5D) * 0.01D;
            double motionY = 0.06D + random.nextDouble() * 0.03D;
            double motionZ = (random.nextDouble() - 0.5D) * 0.01D;

            level.addParticle(ParticleTypes.CLOUD, x, y, z, motionX, motionY, motionZ);
        }

        if (random.nextInt(10) == 0) {
            double x = pos.getX() + 0.25 + random.nextDouble() * 0.5;
            double y = pos.getY() + 0.58;
            double z = pos.getZ() + 0.25 + random.nextDouble() * 0.5;

            double motionX = (random.nextDouble() - 0.5D) * 0.01D;
            double motionY = 0.05D + random.nextDouble() * 0.02D;
            double motionZ = (random.nextDouble() - 0.5D) * 0.01D;

            level.addParticle(ParticleTypes.WHITE_ASH, x, y, z, motionX, motionY, motionZ);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof BriningVatBlockEntity vatEntity) {
                vatEntity.drops();
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) return InteractionResult.sidedSuccess(true);

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof BriningVatBlockEntity vatEntity)) return InteractionResult.PASS;

        ItemStack held = player.getItemInHand(hand);
        Stage stage = state.getValue(STAGE);

        // Fill with water bucket
        if (stage == Stage.EMPTY && held.getItem() == Items.WATER_BUCKET) {
            if (!player.isCreative()) {
                player.setItemInHand(hand, new ItemStack(Items.BUCKET));
            }
            vatEntity.fillWithWater();
            level.setBlock(pos, state.setValue(STAGE, Stage.WATER), 3);
            return InteractionResult.SUCCESS;
        }

        // Fill with water pot item (pours one "bucketful" then becomes empty pot)
        if (stage == Stage.EMPTY && held.getItem() == ModItems.WATER_POT.get()) {
            if (!player.isCreative()) {
                held.shrink(1);
                ItemStack emptyPot = new ItemStack(ModItems.POT.get());
                if (held.isEmpty()) {
                    player.setItemInHand(hand, emptyPot);
                } else if (!player.getInventory().add(emptyPot)) {
                    player.drop(emptyPot, false);
                }
            }
            vatEntity.fillWithWater();
            level.setBlock(pos, state.setValue(STAGE, Stage.WATER), 3);
            return InteractionResult.SUCCESS;
        }

        // Scrape out salt all at once (resets the vat back to empty)
        if (stage == Stage.SALT && vatEntity.hasSalt()) {
            ItemStack saltStack = vatEntity.takeAllSalt();
            if (!saltStack.isEmpty()) {
                if (!player.getInventory().add(saltStack)) {
                    player.drop(saltStack, false);
                }
                vatEntity.setChanged();
                level.setBlock(pos, state.setValue(STAGE, Stage.EMPTY), 3);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.BRINING_VAT_BLOCK_ENTITY.get(), BriningVatBlockEntity::tick);
    }
}

