package com.torr.materia.entity;

import com.torr.materia.ModBlocks;
import com.torr.materia.ModEntities;
import com.torr.materia.ModItems;
import com.torr.materia.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class FallingAmphoraEntity extends net.minecraft.world.entity.Entity {
    private static final EntityDataAccessor<BlockPos> DATA_START_POS = SynchedEntityData.defineId(FallingAmphoraEntity.class, EntityDataSerializers.BLOCK_POS);
    
    private BlockState blockState;
    private int time;
    private boolean dropItem = true;
    private boolean cancelDrop = false;
    private boolean hurtEntities = false;
    private int fallDamageMax = 40;
    private float fallDamagePerDistance = 2.0F;
    
    // Storage data from the block entity
    private CompoundTag storedInventory = null;
    private int storageMode = 0;

    public FallingAmphoraEntity(EntityType<? extends FallingAmphoraEntity> entityType, Level level) {
        super(entityType, level);
        this.blocksBuilding = true;
        // Initialize with a default block state to prevent null pointer exceptions
        this.blockState = ModBlocks.AMPHORA.get().defaultBlockState();
    }

    public FallingAmphoraEntity(Level level, double x, double y, double z, BlockState blockState) {
        this(ModEntities.FALLING_AMPHORA.get(), level);
        this.blockState = blockState;
        this.blocksBuilding = true;
        this.setPos(x, y + (double)((1.0F - this.getBbHeight()) / 2.0F), z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.setStartPos(this.blockPosition());
    }

    // Constructor that takes block entity data
    public FallingAmphoraEntity(Level level, double x, double y, double z, BlockState blockState, CompoundTag blockEntityData) {
        this(level, x, y, z, blockState);
        if (blockEntityData != null) {
            this.storedInventory = blockEntityData.copy();
            this.storageMode = blockEntityData.getInt("StorageMode");
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_START_POS, BlockPos.ZERO);
    }

    public void setStartPos(BlockPos pos) {
        this.entityData.set(DATA_START_POS, pos);
    }

    public BlockPos getStartPos() {
        return this.entityData.get(DATA_START_POS);
    }

    @Override
    public void tick() {
        // Safety check for null blockState
        if (this.blockState == null) {
            this.blockState = ModBlocks.AMPHORA.get().defaultBlockState();
        }
        
        if (this.blockState.isAir()) {
            this.discard();
            return;
        }

        Block block = this.blockState.getBlock();
        ++this.time;
        
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        
        if (!this.level().isClientSide) {
            BlockPos blockpos = this.blockPosition();
            boolean flag = this.blockState.getBlock() instanceof net.minecraft.world.level.block.ConcretePowderBlock;
            boolean flag1 = flag && this.level().getFluidState(blockpos).is(net.minecraft.tags.FluidTags.WATER);
            double d0 = this.getDeltaMovement().lengthSqr();
            
            if (flag && d0 > 1.0D) {
                net.minecraft.world.phys.BlockHitResult blockhitresult = this.level().clip(new net.minecraft.world.level.ClipContext(new Vec3(this.xo, this.yo, this.zo), this.position(), net.minecraft.world.level.ClipContext.Block.COLLIDER, net.minecraft.world.level.ClipContext.Fluid.SOURCE_ONLY, this));
                if (blockhitresult.getType() != net.minecraft.world.phys.HitResult.Type.MISS && this.level().getFluidState(blockhitresult.getBlockPos()).is(net.minecraft.tags.FluidTags.WATER)) {
                    flag1 = true;
                }
            }

            if (!this.onGround() && !flag1) {
                if (!this.level().isClientSide && (this.time > 100 && (blockpos.getY() <= this.level().getMinBuildHeight() || blockpos.getY() > this.level().getMaxBuildHeight()) || this.time > 600)) {
                    if (this.dropItem && this.level().getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_DOENTITYDROPS)) {
                        this.spawnAtLocation(block);
                    }
                    this.discard();
                }
            } else {
                BlockState blockstate = this.level().getBlockState(blockpos);
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
                
                if (!blockstate.is(net.minecraft.world.level.block.Blocks.MOVING_PISTON)) {
                    if (!this.cancelDrop) {
                        boolean flag2 = blockstate.canBeReplaced(new net.minecraft.world.item.context.DirectionalPlaceContext(this.level(), blockpos, net.minecraft.core.Direction.DOWN, ItemStack.EMPTY, net.minecraft.core.Direction.UP));
                        boolean flag3 = net.minecraft.world.level.block.FallingBlock.isFree(this.level().getBlockState(blockpos.below())) && blockpos.getY() >= this.level().getMinBuildHeight();
                        boolean flag4 = this.blockState.canSurvive(this.level(), blockpos) && !flag3;
                        
                        // Calculate fall distance to determine if amphora should break
                        BlockPos startPos = this.getStartPos();
                        double fallDistance = startPos.getY() - blockpos.getY();
                        
                        if (flag2 && flag4) {
                            // Check if amphora should break (fell more than 1 block)
                            if (fallDistance > 1.0) {
                                // Break the amphora - play sound and drop ceramic pieces
                                this.level().playSound(null, blockpos, ModSounds.POTTERY_BREAK.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                                
               // Check if amphora had a lid and handle lid survival/destruction
               boolean hadLid = false;
               boolean lidSurvives = false;
               ItemStack lidItem = ItemStack.EMPTY;
               
               if (this.storedInventory != null) {
                   // Create temporary entity to check lid status
                   com.torr.materia.blockentity.AmphoraBlockEntity tempEntity = 
                       new com.torr.materia.blockentity.AmphoraBlockEntity(blockpos, this.blockState);
                   tempEntity.load(this.storedInventory);
                   
                   hadLid = tempEntity.hasLid();
                   if (hadLid) {
                       lidSurvives = this.random.nextDouble() < 0.1; // 10% chance
                       if (lidSurvives) {
                           lidItem = tempEntity.getLidItem();
                       }
                   }
               }
               
               // Drop ceramic pieces (2-4 base, +1 if lid was destroyed)
               int ceramicCount = 2 + this.random.nextInt(3); // 2-4
               if (hadLid && !lidSurvives) {
                   ceramicCount++; // +1 for destroyed lid
               }
               
               for (int i = 0; i < ceramicCount; i++) {
                   ItemStack ceramicStack = new ItemStack(ModItems.CRUSHED_CERAMIC.get());
                   ItemEntity itemEntity = new ItemEntity(this.level(),
                       blockpos.getX() + 0.5 + (this.random.nextDouble() - 0.5) * 0.5,
                       blockpos.getY() + 0.5,
                       blockpos.getZ() + 0.5 + (this.random.nextDouble() - 0.5) * 0.5,
                       ceramicStack);
                   itemEntity.setDefaultPickUpDelay();
                   this.level().addFreshEntity(itemEntity);
               }
               
               // Drop surviving lid
               if (lidSurvives && !lidItem.isEmpty()) {
                   ItemEntity lidEntity = new ItemEntity(this.level(),
                       blockpos.getX() + 0.5 + (this.random.nextDouble() - 0.5) * 0.5,
                       blockpos.getY() + 0.5,
                       blockpos.getZ() + 0.5 + (this.random.nextDouble() - 0.5) * 0.5,
                       lidItem);
                   lidEntity.setDefaultPickUpDelay();
                   this.level().addFreshEntity(lidEntity);
               }

               // Drop the stored items if any
               if (this.storedInventory != null) {
                   if (this.storageMode == 1) { // MODE_SOLID
                       this.dropStoredItems(blockpos);
                   } else if (this.storageMode == 2) { // MODE_LIQUID
                       this.dropStoredLiquids(blockpos);
                   }
               }
                                
                                // TODO: In the future, drop inventory contents here
                                // for (ItemStack stack : inventory) {
                                //     if (!stack.isEmpty()) {
                                //         ItemEntity itemEntity = new ItemEntity(this.level, blockpos.getX() + 0.5, blockpos.getY() + 0.5, blockpos.getZ() + 0.5, stack);
                                //         itemEntity.setDefaultPickUpDelay();
                                //         this.level.addFreshEntity(itemEntity);
                                //     }
                                // }
                                
                                // Discard the entity after breaking - don't place the block
                                this.discard();
                                
                            } else {
                                // Place the amphora block normally if fall distance is small
                                if (this.level().setBlock(blockpos, this.blockState, 3)) {
                                    // Restore the block entity data if we have it
                                    if (this.storedInventory != null) {
                                        net.minecraft.world.level.block.entity.BlockEntity blockEntity = this.level().getBlockEntity(blockpos);
                                        if (blockEntity instanceof com.torr.materia.blockentity.AmphoraBlockEntity amphoraEntity) {
                                            amphoraEntity.load(this.storedInventory);
                                        }
                                    }
                                    // Successfully placed the block - discard the entity
                                    this.discard();
                                } else if (this.dropItem && this.level().getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_DOENTITYDROPS)) {
                                    // Failed to place block - drop as item and discard entity
                                    this.spawnAtLocation(block);
                   // Also drop the stored items if any
                   if (this.storedInventory != null) {
                       if (this.storageMode == 1) { // MODE_SOLID
                           this.dropStoredItems(blockpos);
                       } else if (this.storageMode == 2) { // MODE_LIQUID
                           this.dropStoredLiquids(blockpos);
                       }
                   }
                                    this.discard();
                                }
                            }
                        } else if (this.dropItem && this.level().getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_DOENTITYDROPS)) {
                            this.discard();
                            this.spawnAtLocation(block);
                        }
                    } else {
                        this.discard();
                        if (this.dropItem && this.level().getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_DOENTITYDROPS)) {
                            this.spawnAtLocation(block);
                        }
                    }
                }
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
    }

    private CompoundTag blockData;

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("BlockState", 10)) {
            this.blockState = NbtUtils.readBlockState(
                    this.level().registryAccess().asGetterLookup().lookupOrThrow(Registries.BLOCK),
                    compound.getCompound("BlockState")
            );
        } else {
            // Default to amphora block if no block state is saved
            this.blockState = ModBlocks.AMPHORA.get().defaultBlockState();
        }
        
        this.time = compound.getInt("Time");
        this.dropItem = compound.getBoolean("DropItem");
        this.cancelDrop = compound.getBoolean("CancelDrop");
        this.hurtEntities = compound.getBoolean("HurtEntities");
        this.fallDamageMax = compound.getInt("FallHurtMax");
        this.fallDamagePerDistance = compound.getFloat("FallHurtAmount");
        
        if (compound.contains("TileEntityData", 10)) {
            this.blockData = compound.getCompound("TileEntityData");
        }
        
        // Load amphora-specific data
        if (compound.contains("StoredInventory", 10)) {
            this.storedInventory = compound.getCompound("StoredInventory");
        }
        this.storageMode = compound.getInt("StorageMode");
        
        // Safety check - ensure we always have a valid block state
        if (this.blockState == null || this.blockState.isAir()) {
            this.blockState = ModBlocks.AMPHORA.get().defaultBlockState();
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        // Ensure we have a valid block state before saving
        if (this.blockState == null) {
            this.blockState = ModBlocks.AMPHORA.get().defaultBlockState();
        }
        
        compound.put("BlockState", net.minecraft.nbt.NbtUtils.writeBlockState(this.blockState));
        compound.putInt("Time", this.time);
        compound.putBoolean("DropItem", this.dropItem);
        compound.putBoolean("CancelDrop", this.cancelDrop);
        compound.putBoolean("HurtEntities", this.hurtEntities);
        compound.putInt("FallHurtMax", this.fallDamageMax);
        compound.putFloat("FallHurtAmount", this.fallDamagePerDistance);
        
        if (this.blockData != null) {
            compound.put("TileEntityData", this.blockData);
        }
        
        // Save amphora-specific data
        if (this.storedInventory != null) {
            compound.put("StoredInventory", this.storedInventory);
        }
        compound.putInt("StorageMode", this.storageMode);
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public BlockState getBlockState() {
        // Safety check to prevent null returns
        if (this.blockState == null) {
            this.blockState = ModBlocks.AMPHORA.get().defaultBlockState();
        }
        return this.blockState;
    }

    // Helper method to drop stored items when amphora breaks
    private void dropStoredItems(BlockPos pos) {
        if (this.storedInventory == null) return;
        
        // Create a temporary amphora block entity to load the inventory
        com.torr.materia.blockentity.AmphoraBlockEntity tempEntity = new com.torr.materia.blockentity.AmphoraBlockEntity(pos, this.blockState);
        tempEntity.load(this.storedInventory);
        
        // Drop all items from the temporary entity
        for (int i = 0; i < tempEntity.getContainerSize(); i++) {
            ItemStack stack = tempEntity.getItem(i);
            if (!stack.isEmpty()) {
                ItemEntity itemEntity = new ItemEntity(this.level(),
                    pos.getX() + 0.5 + (this.random.nextDouble() - 0.5) * 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5 + (this.random.nextDouble() - 0.5) * 0.5,
                    stack);
                itemEntity.setDefaultPickUpDelay();
                this.level().addFreshEntity(itemEntity);
            }
        }
    }

    // Helper method to drop stored liquids when amphora breaks
    private void dropStoredLiquids(BlockPos pos) {
        if (this.storedInventory == null) return;
        
        // Create a temporary amphora block entity to load the liquid data
        com.torr.materia.blockentity.AmphoraBlockEntity tempEntity = new com.torr.materia.blockentity.AmphoraBlockEntity(pos, this.blockState);
        tempEntity.load(this.storedInventory);
        
        // Drop one water source block if there was at least 1 bucket's worth (3 bottles)
        if (tempEntity.hasWater()) {
            if (tempEntity.getLiquidAmount() >= 3) {
                if (this.level().getBlockState(pos).isAir()) {
                    this.level().setBlock(pos, net.minecraft.world.level.block.Blocks.WATER.defaultBlockState(), 3);
                }
            }
        } else if (tempEntity.hasGrapeJuice()) {
            // Drop grape juice as items (1 grape_juice item per bottle worth)
            int liquidAmount = tempEntity.getLiquidAmount();
            for (int i = 0; i < liquidAmount; i++) {
                ItemEntity itemEntity = new ItemEntity(this.level(),
                    pos.getX() + 0.5 + (this.random.nextDouble() - 0.5) * 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5 + (this.random.nextDouble() - 0.5) * 0.5,
                    new ItemStack(ModItems.GRAPE_JUICE.get()));
                itemEntity.setDefaultPickUpDelay();
                this.level().addFreshEntity(itemEntity);
            }
        } else if (tempEntity.hasOliveOil()) {
            // Drop olive oil as items (1 olive_oil item per bottle worth)
            int liquidAmount = tempEntity.getLiquidAmount();
            for (int i = 0; i < liquidAmount; i++) {
                ItemEntity itemEntity = new ItemEntity(this.level(),
                    pos.getX() + 0.5 + (this.random.nextDouble() - 0.5) * 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5 + (this.random.nextDouble() - 0.5) * 0.5,
                    new ItemStack(ModItems.OLIVE_OIL.get()));
                itemEntity.setDefaultPickUpDelay();
                this.level().addFreshEntity(itemEntity);
            }
        } else if (tempEntity.hasVinegar()) {
            // Drop vinegar as items (1 vinegar item per bottle worth)
            int liquidAmount = tempEntity.getLiquidAmount();
            for (int i = 0; i < liquidAmount; i++) {
                ItemEntity itemEntity = new ItemEntity(this.level(),
                    pos.getX() + 0.5 + (this.random.nextDouble() - 0.5) * 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5 + (this.random.nextDouble() - 0.5) * 0.5,
                    new ItemStack(ModItems.VINEGAR.get()));
                itemEntity.setDefaultPickUpDelay();
                this.level().addFreshEntity(itemEntity);
            }
        } else if (tempEntity.hasWine()) {
            // Drop wine as items (1 wine_cup item per bottle worth)
            int liquidAmount = tempEntity.getLiquidAmount();
            for (int i = 0; i < liquidAmount; i++) {
                ItemEntity itemEntity = new ItemEntity(this.level(),
                    pos.getX() + 0.5 + (this.random.nextDouble() - 0.5) * 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5 + (this.random.nextDouble() - 0.5) * 0.5,
                    new ItemStack(ModItems.WINE_CUP.get()));
                itemEntity.setDefaultPickUpDelay();
                this.level().addFreshEntity(itemEntity);
            }
        }
    }
}
