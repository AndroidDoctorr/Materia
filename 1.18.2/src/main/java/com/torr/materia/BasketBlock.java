package com.torr.materia;

import com.torr.materia.blockentity.BasketBlockEntity;
import com.torr.materia.entity.DynamiteEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class BasketBlock extends BaseEntityBlock {
    public static final BooleanProperty OPEN = BooleanProperty.create("open");

    public BasketBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(OPEN, false));
    }

    // Make basket flammable like wool
    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 30; // Same as wool
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 60; // Same as wool
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OPEN);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(OPEN, false);
    }

    // Block Entity Methods
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BasketBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    // GUI Interaction
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof BasketBlockEntity) {
                // Open the basket (set OPEN to true)
                level.setBlock(pos, state.setValue(OPEN, true), 3);
                
                // Play basket opening sound
                level.playSound(null, pos, ModSounds.WICKER_BASKET.get(), SoundSource.BLOCKS, 0.5F, 1.0F);
                
                // Open the GUI
                MenuProvider containerProvider = createMenuProvider(state, level, pos);
                NetworkHooks.openGui((ServerPlayer) player, containerProvider, pos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    private MenuProvider createMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return new TranslatableComponent("container.materia.basket");
            }

            @Override
            public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int windowId, net.minecraft.world.entity.player.Inventory playerInventory, Player player) {
                return new com.torr.materia.menu.BasketMenu(windowId, playerInventory, level.getBlockEntity(pos));
            }
        };
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return getBasketWithNBT(level, pos, state);
    }
    
    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        // Play basket sound when broken by player
        if (!level.isClientSide) {
            level.playSound(null, pos, ModSounds.WICKER_BASKET.get(), SoundSource.BLOCKS, 1.0F, 0.8F);
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        // Get the block entity before calling super, as super will remove it
        BasketBlockEntity basketEntity = null;
        if (blockEntity instanceof BasketBlockEntity) {
            basketEntity = (BasketBlockEntity) blockEntity;
        }
        
        // Don't call super.playerDestroy as it will drop the default item via loot tables
        // Instead, manually drop our custom item with NBT
        if (!level.isClientSide && basketEntity != null) {
            ItemStack drop = getBasketWithNBTFromEntity(basketEntity, state);
            if (!drop.isEmpty()) {
                Block.popResource(level, pos, drop);
            }
        }
        
        // Remove the block
        level.removeBlock(pos, false);
    }
    
    private ItemStack getBasketWithNBT(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack stack = new ItemStack(this);
        
        // Save contents to the item's NBT
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BasketBlockEntity basketEntity) {
            // Check for contents
            if (!basketEntity.isEmpty()) {
                CompoundTag blockEntityTag = basketEntity.saveWithoutMetadata();
                
                CompoundTag itemTag = stack.getOrCreateTag();
                itemTag.put("BlockEntityTag", blockEntityTag);
            }
        }
        
        return stack;
    }
    
    private ItemStack getBasketWithNBTFromEntity(BasketBlockEntity basketEntity, BlockState state) {
        ItemStack stack = new ItemStack(this);
        
        // Save contents to the item's NBT
        if (!basketEntity.isEmpty()) {
            CompoundTag blockEntityTag = basketEntity.saveWithoutMetadata();
            
            CompoundTag itemTag = stack.getOrCreateTag();
            itemTag.put("BlockEntityTag", blockEntityTag);
        }
        
        return stack;
    }

    // Handle non-player destruction (explosions, etc.)
    @Override
    public void wasExploded(Level level, BlockPos pos, net.minecraft.world.level.Explosion explosion) {
        // Check for explosives before dropping contents
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BasketBlockEntity basketEntity) {
            checkAndExplodeBasket(level, pos, basketEntity);
            Containers.dropContents(level, pos, basketEntity);
        }
        super.wasExploded(level, pos, explosion);
    }
    
    // Drop contents when block is broken (only for non-player destruction)
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            // Check if the basket is being destroyed by fire and contains explosives
            if (!level.isClientSide) {
                // Check if there's fire nearby or if the block is being replaced with air (burned away)
                boolean burnedByFire = newState.getBlock() == Blocks.AIR && 
                    (level.getBlockState(pos.above()).getBlock() == Blocks.FIRE ||
                     level.getBlockState(pos.north()).getBlock() == Blocks.FIRE ||
                     level.getBlockState(pos.south()).getBlock() == Blocks.FIRE ||
                     level.getBlockState(pos.east()).getBlock() == Blocks.FIRE ||
                     level.getBlockState(pos.west()).getBlock() == Blocks.FIRE);
                
                if (burnedByFire) {
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity instanceof BasketBlockEntity basketEntity) {
                        checkAndExplodeBasket(level, pos, basketEntity);
                    }
                }
            }
            // Note: Contents are now preserved in the item's NBT when picked up by players
            // The playerDestroy method handles normal player breaking with NBT preservation
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    // Close the basket when no players are viewing it
    public void closeBasket(Level level, BlockPos pos, BlockState state) {
        if (!level.isClientSide() && state.getValue(OPEN)) {
            level.setBlock(pos, state.setValue(OPEN, false), 3);
            level.playSound(null, pos, ModSounds.WICKER_BASKET.get(), SoundSource.BLOCKS, 0.3F, 0.8F);
        }
    }

    private void checkAndExplodeBasket(Level level, BlockPos pos, BasketBlockEntity basketEntity) {
        if (level.isClientSide) return;
        
        int dynamiteCount = 0;
        int tntCount = 0;
        int fireworkCount = 0;
        java.util.List<ItemStack> fireworks = new java.util.ArrayList<>();
        
        // Check all 4 slots for explosives
        for (int i = 0; i < basketEntity.getContainerSize(); i++) {
            ItemStack stack = basketEntity.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() == ModItems.DYNAMITE.get()) {
                    dynamiteCount += stack.getCount();
                } else if (stack.getItem() == Items.TNT) {
                    tntCount += stack.getCount();
                } else if (stack.getItem() == Items.FIREWORK_ROCKET) {
                    fireworkCount += stack.getCount();
                    // Store each firework individually for launching
                    for (int j = 0; j < stack.getCount(); j++) {
                        fireworks.add(new ItemStack(Items.FIREWORK_ROCKET, 1, stack.getTag()));
                    }
                }
            }
        }
        
        // Handle explosives and fireworks separately
        boolean hasExplosives = dynamiteCount > 0 || tntCount > 0;
        
        if (hasExplosives) {
            // Calculate explosion power (max 4.0F like TNT) - only from actual explosives
            float explosionPower = Math.min(4.0F, 
                (dynamiteCount * 0.4F) + (tntCount * 4.0F));
            
            // Create the explosion
            level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 
                explosionPower, Explosion.BlockInteraction.BREAK);
            
            // Play explosion sound
            level.playSound(null, pos, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, 
                (1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F) * 0.7F);
        }
        
        // Launch fireworks in rapid succession (regardless of explosives)
        if (!fireworks.isEmpty() && level instanceof ServerLevel serverLevel) {
            launchFireworksFromBasket(serverLevel, pos, fireworks);
        }
    }
    
    private void launchFireworksFromBasket(ServerLevel level, BlockPos pos, java.util.List<ItemStack> fireworks) {
        // Launch fireworks with staggered timing using a simple approach
        for (int i = 0; i < fireworks.size(); i++) {
            ItemStack firework = fireworks.get(i);
            int delay = i * 3 + level.random.nextInt(5); // 3-7 ticks between launches
            
            // Use a simple delayed execution
            scheduleFireworkLaunch(level, pos, firework, delay);
        }
    }
    
    private void scheduleFireworkLaunch(ServerLevel level, BlockPos pos, ItemStack firework, int delay) {
        // Create a simple task that will be executed after the delay
        class FireworkTask implements Runnable {
            private int ticksRemaining;
            
            public FireworkTask(int delay) {
                this.ticksRemaining = delay;
            }
            
            @Override
            public void run() {
                ticksRemaining--;
                if (ticksRemaining <= 0) {
                    if (!level.isLoaded(pos)) return;
                    
                    // Create and launch the firework
                    FireworkRocketEntity fireworkEntity = new FireworkRocketEntity(level, 
                        pos.getX() + 0.5 + (level.random.nextDouble() - 0.5) * 2.0,
                        pos.getY() + 1.0,
                        pos.getZ() + 0.5 + (level.random.nextDouble() - 0.5) * 2.0,
                        firework);
                    
                    // Add some random velocity
                    double velX = (level.random.nextDouble() - 0.5) * 0.4;
                    double velY = level.random.nextDouble() * 0.3 + 0.1;
                    double velZ = (level.random.nextDouble() - 0.5) * 0.4;
                    fireworkEntity.setDeltaMovement(velX, velY, velZ);
                    
                    level.addFreshEntity(fireworkEntity);
                } else {
                    // Schedule next tick
                    level.getServer().execute(this);
                }
            }
        }
        
        // Start the delayed task
        level.getServer().execute(new FireworkTask(delay));
    }
}
