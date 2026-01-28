package com.torr.materia;

import com.torr.materia.blockentity.WaterPotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import java.util.Random;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;

public class WaterPotBlock extends Block implements EntityBlock {
    protected static final VoxelShape POT_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D);
    public static final BooleanProperty BOILING = BooleanProperty.create("boiling");
    public static final IntegerProperty WATER_LEVEL = IntegerProperty.create("water_level", 0, 3);
    private static final TagKey<Item> EARTH_BLOCKS = ItemTags.create(new ResourceLocation(materia.MOD_ID, "earth_blocks"));

    public WaterPotBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BOILING, false).setValue(WATER_LEVEL, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BOILING, WATER_LEVEL);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
            BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);

        // Check if player is trying to extract an item (empty hand)
        if (held.isEmpty()) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof WaterPotBlockEntity potEntity) {
                    net.minecraftforge.items.IItemHandler handler = potEntity
                            .getCapability(net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                            .orElse(null);
                    if (handler != null && !handler.getStackInSlot(0).isEmpty()) {
                        ItemStack extracted = handler.extractItem(0, 1, false);
                        if (!extracted.isEmpty()) {
                            if (!player.addItem(extracted))
                                player.drop(extracted, false);
                            potEntity.setChanged();
                            level.sendBlockUpdated(pos, state, state, 3);
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Check if player is trying to use a crucible (fill it with water)
        if (held.is(ModItems.CRUCIBLE.get())) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof WaterPotBlockEntity potEntity) {
                    if (potEntity.hasWater()) {
                        // Convert crucible to water cup and reduce water level
                        ItemStack waterCup = new ItemStack(ModItems.WATER_CUP.get());
                        potEntity.setWaterLevel(potEntity.getWaterLevel() - 1);
                        
                        // Play water fill sound
                        level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                        
                        // Replace crucible with water cup in the same slot
                        ItemStack result = ItemUtils.createFilledResult(held, player, waterCup);
                        player.setItemInHand(hand, result);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        
        // Bucket interactions: water bucket fills pot to full, empty bucket takes full pot
        if (held.is(Items.WATER_BUCKET)) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof WaterPotBlockEntity potEntity) {
                    if (potEntity.canAddWater()) {
                        // Fill to full
                        potEntity.setWaterLevel(3);

                        // Sound and replace with empty bucket
                        level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                        ItemStack emptyBucket = new ItemStack(Items.BUCKET);
                        ItemStack result = ItemUtils.createFilledResult(held, player, emptyBucket);
                        player.setItemInHand(hand, result);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (held.is(Items.BUCKET)) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof WaterPotBlockEntity potEntity) {
                    if (potEntity.getWaterLevel() == 3) {
                        // Take a full bucket out
                        potEntity.setWaterLevel(0);

                        level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                        ItemStack waterBucket = new ItemStack(Items.WATER_BUCKET);
                        ItemStack result = ItemUtils.createFilledResult(held, player, waterBucket);
                        player.setItemInHand(hand, result);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Bottle interactions: glass bottle takes 1 level, water bottle adds 1 level
        if (held.is(Items.GLASS_BOTTLE)) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof WaterPotBlockEntity potEntity) {
                    if (potEntity.hasWater()) {
                        potEntity.setWaterLevel(potEntity.getWaterLevel() - 1);

                        level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                        ItemStack waterBottle = new ItemStack(Items.POTION);
                        PotionUtils.setPotion(waterBottle, Potions.WATER);
                        ItemStack result = ItemUtils.createFilledResult(held, player, waterBottle);
                        player.setItemInHand(hand, result);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (held.is(Items.POTION) && PotionUtils.getPotion(held) == Potions.WATER) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof WaterPotBlockEntity potEntity) {
                    if (potEntity.canAddWater()) {
                        potEntity.setWaterLevel(potEntity.getWaterLevel() + 1);

                        level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                        ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);
                        ItemStack result = ItemUtils.createFilledResult(held, player, emptyBottle);
                        player.setItemInHand(hand, result);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Sneak-right-click: transfer water with cauldron above (both directions)
        if (player.isShiftKeyDown()) {
            BlockPos above = pos.above();
            BlockState aboveState = level.getBlockState(above);
            if (aboveState.is(Blocks.CAULDRON) || aboveState.is(Blocks.WATER_CAULDRON)) {
                if (!level.isClientSide) {
                    BlockEntity be = level.getBlockEntity(pos);
                    if (be instanceof WaterPotBlockEntity potEntity) {
                        int pot = potEntity.getWaterLevel();
                        int current = aboveState.is(Blocks.WATER_CAULDRON) ? aboveState.getValue(LayeredCauldronBlock.LEVEL) : 0;
                        int space = 3 - current;

                        // First: pour from pot into cauldron
                        if (pot > 0 && space > 0) {
                            int transfer = Math.min(space, pot);
                            if (transfer > 0) {
                                int newLevel = current + transfer;
                                BlockState newState = Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, newLevel);
                                level.setBlock(above, newState, 3);
                                potEntity.setWaterLevel(pot - transfer);
                                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                                return InteractionResult.SUCCESS;
                            }
                        }

                        // Second: if pot has space and cauldron has water, pull into pot
                        if (pot < 3 && current > 0) {
                            int potSpace = 3 - pot;
                            int transfer = Math.min(potSpace, current);
                            if (transfer > 0) {
                                int newLevel = current - transfer;
                                BlockState newState = newLevel == 0 ? Blocks.CAULDRON.defaultBlockState()
                                        : aboveState.setValue(LayeredCauldronBlock.LEVEL, newLevel);
                                level.setBlock(above, newState, 3);
                                potEntity.setWaterLevel(pot + transfer);
                                level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                                return InteractionResult.SUCCESS;
                            }
                        }
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        // Check if player is trying to use a water pot (transfer water)
        if (held.is(ModItems.WATER_POT.get())) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof WaterPotBlockEntity targetPotEntity) {
                    if (targetPotEntity.canAddWater()) {
                        // Transfer 1 water level from water pot to this pot
                        targetPotEntity.setWaterLevel(targetPotEntity.getWaterLevel() + 1);
                        
                        // Play water pour sound
                        level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                        
                        // Convert water pot to empty pot
                        ItemStack emptyPot = new ItemStack(ModItems.POT.get());
                        ItemStack result = ItemUtils.createFilledResult(held, player, emptyPot);
                        player.setItemInHand(hand, result);
                        return InteractionResult.SUCCESS;
                    } else {
                        // Pot is full, place water source block instead
                        BlockPos placePos = pos.above();
                        if (level.getBlockState(placePos).isAir()) {
                            level.setBlock(placePos, Blocks.WATER.defaultBlockState(), 3);
                            level.playSound(null, placePos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                            
                            // Convert water pot to empty pot
                            ItemStack emptyPot = new ItemStack(ModItems.POT.get());
                            ItemStack result = ItemUtils.createFilledResult(held, player, emptyPot);
                            player.setItemInHand(hand, result);
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Check if player is trying to use a water cup (add water to pot)
        if (held.is(ModItems.WATER_CUP.get())) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof WaterPotBlockEntity potEntity) {
                    if (potEntity.canAddWater()) {
                        // Convert water cup to crucible and increase water level
                        ItemStack crucible = new ItemStack(ModItems.CRUCIBLE.get());
                        potEntity.setWaterLevel(potEntity.getWaterLevel() + 1);
                        
                        // Play water pour sound
                        level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                        
                        // Replace water cup with crucible in the same slot
                        ItemStack result = ItemUtils.createFilledResult(held, player, crucible);
                        player.setItemInHand(hand, result);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Check if player is trying to insert an item (bone, tanned leather, murex glands, bark, or earth-equivalent)
        if (held.is(Items.BONE) || held.is(ModItems.TANNED_LEATHER.get()) || held.is(ModItems.PAPER_MIXTURE.get()) || held.is(ModItems.OAK_BARK.get()) || held.is(EARTH_BLOCKS) ||
            held.is(ModItems.MUREX_GLAND_BRANDARIS.get()) || held.is(ModItems.MUREX_GLAND_TRUNCULUS.get()) || 
            held.is(ModItems.MUREX_GLAND_HAEMASTOMA.get())) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof WaterPotBlockEntity potEntity) {
                    net.minecraftforge.items.IItemHandler handler = potEntity
                            .getCapability(net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                            .orElse(null);
                    if (handler != null && handler.getStackInSlot(0).isEmpty()) {
                        int insertCount = held.is(EARTH_BLOCKS) ? 2 : held.is(ModItems.PAPER_MIXTURE.get()) ? 1 : 1;
                        if (held.getCount() < insertCount) {
                            return InteractionResult.PASS;
                        }
                        ItemStack stackToInsert = held.copy();
                        stackToInsert.setCount(insertCount);
                        handler.insertItem(0, stackToInsert, false);
                        held.shrink(insertCount);
                        potEntity.setChanged();
                        level.sendBlockUpdated(pos, state, state, 3);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(BOILING)) {
            // Water vapor/steam rising from the surface - stronger upward motion
            if (random.nextInt(3) == 0) { // Frequent but not overwhelming
                double x = pos.getX() + 0.25 + random.nextDouble() * 0.5;
                double y = pos.getY() + 0.55; // Just above water surface
                double z = pos.getZ() + 0.25 + random.nextDouble() * 0.5;
                
                double motionX = (random.nextDouble() - 0.5D) * 0.01D;
                double motionY = 0.08D + random.nextDouble() * 0.04D; // Much stronger upward motion
                double motionZ = (random.nextDouble() - 0.5D) * 0.01D;
                
                level.addParticle(ParticleTypes.CLOUD, x, y, z, motionX, motionY, motionZ);
            }

            // Water surface agitation - splash particles for boiling effect
            if (random.nextInt(5) == 0) {
                double x = pos.getX() + 0.3 + random.nextDouble() * 0.4;
                double y = pos.getY() + 0.5; // Exactly at water surface
                double z = pos.getZ() + 0.3 + random.nextDouble() * 0.4;
                
                double motionX = (random.nextDouble() - 0.5D) * 0.02D;
                double motionY = 0.05D + random.nextDouble() * 0.03D; // Stronger upward splash
                double motionZ = (random.nextDouble() - 0.5D) * 0.02D;
                
                level.addParticle(ParticleTypes.SPLASH, x, y, z, motionX, motionY, motionZ);
            }

            // Bubble popping at surface
            if (random.nextInt(10) == 0) {
                double x = pos.getX() + 0.3 + random.nextDouble() * 0.4;
                double y = pos.getY() + 0.52;
                double z = pos.getZ() + 0.3 + random.nextDouble() * 0.4;
                
                level.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, 0, 0, 0);
            }

            // Underwater bubbles rising to surface - stronger upward motion
            for (int i = 0; i < 3; i++) {
                double x = pos.getX() + 0.3 + random.nextDouble() * 0.4;
                double y = pos.getY() + 0.2 + random.nextDouble() * 0.3; // Start from bottom of pot
                double z = pos.getZ() + 0.3 + random.nextDouble() * 0.4;
                
                double motionX = (random.nextDouble() - 0.5D) * 0.01D;
                double motionY = 0.10D + random.nextDouble() * 0.05D; // Much stronger upward motion
                double motionZ = (random.nextDouble() - 0.5D) * 0.01D;
                
                level.addParticle(ParticleTypes.BUBBLE, x, y, z, motionX, motionY, motionZ);
            }

            // Additional steam effect using white ash particles - strong upward motion
            if (random.nextInt(8) == 0) {
                double x = pos.getX() + 0.3 + random.nextDouble() * 0.4;
                double y = pos.getY() + 0.58;
                double z = pos.getZ() + 0.3 + random.nextDouble() * 0.4;
                
                double motionX = (random.nextDouble() - 0.5D) * 0.01D;
                double motionY = 0.07D + random.nextDouble() * 0.03D; // Strong upward motion
                double motionZ = (random.nextDouble() - 0.5D) * 0.01D;
                
                level.addParticle(ParticleTypes.WHITE_ASH, x, y, z, motionX, motionY, motionZ);
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WaterPotBlockEntity(pos, state);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable net.minecraft.world.entity.LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        // When a water pot block is placed, it should start with full water (level 3)
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof WaterPotBlockEntity potEntity) {
                potEntity.setWaterLevel(3);
            }
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
            BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof WaterPotBlockEntity waterPot) {
                waterPot.tick();
            }
        };
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return POT_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return POT_SHAPE;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return POT_SHAPE;
    }
}