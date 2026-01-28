package com.torr.materia.block;

import com.torr.materia.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Random;

public class OliveTreeLeavesBlock extends LeavesBlock {
    public static final BooleanProperty HAS_OLIVES = BooleanProperty.create("has_olives");

    public OliveTreeLeavesBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(DISTANCE, 7)
                .setValue(PERSISTENT, false)
                .setValue(HAS_OLIVES, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HAS_OLIVES);
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return 1;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(HAS_OLIVES)) {
            if (!level.isClientSide) {
                // Drop olives
                ItemStack olives = new ItemStack(ModItems.OLIVES.get(), 1 + level.random.nextInt(3)); // 1-3 olives
                ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, olives);
                itemEntity.setDefaultPickUpDelay();
                level.addFreshEntity(itemEntity);

                // Play sound
                level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);

                // Remove olives from the block
                level.setBlock(pos, state.setValue(HAS_OLIVES, false), 2);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        // First handle olive regrowth before calling super (in case super removes the block)
        if (!state.getValue(HAS_OLIVES) && random.nextFloat() < 0.2f) {
            level.setBlock(pos, state.setValue(HAS_OLIVES, true), 2);
        }
        
        // Then handle normal leaves behavior (decay, etc.)
        super.randomTick(state, level, pos, random);
    }
}
