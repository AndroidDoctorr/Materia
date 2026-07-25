package com.torr.materia.entity;



import com.torr.materia.ModEntities;

import com.torr.materia.ModItems;
import com.torr.materia.menu.CartMenu;

import net.minecraft.core.BlockPos;

import net.minecraft.core.Direction;

import net.minecraft.core.NonNullList;

import net.minecraft.core.component.DataComponents;

import net.minecraft.nbt.CompoundTag;

import net.minecraft.network.chat.Component;

import net.minecraft.resources.ResourceLocation;

import net.minecraft.server.level.ServerPlayer;

import net.minecraft.util.Mth;

import net.minecraft.world.InteractionHand;

import net.minecraft.world.InteractionResult;

import net.minecraft.world.damagesource.DamageSource;

import net.minecraft.world.entity.Entity;

import net.minecraft.world.entity.EntityType;

import net.minecraft.world.entity.HasCustomInventoryScreen;

import net.minecraft.world.entity.Leashable;

import net.minecraft.world.entity.SlotAccess;

import net.minecraft.world.entity.player.Inventory;

import net.minecraft.world.entity.player.Player;

import net.minecraft.world.entity.vehicle.Boat;

import net.minecraft.world.entity.vehicle.ContainerEntity;

import net.minecraft.world.inventory.AbstractContainerMenu;

import net.minecraft.world.item.Item;

import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.Items;

import net.minecraft.world.item.component.CustomData;

import net.minecraft.world.level.GameRules;

import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.gameevent.GameEvent;

import net.minecraft.world.level.material.FluidState;

import net.minecraft.world.phys.AABB;

import net.minecraft.world.phys.Vec3;

import net.minecraft.world.phys.shapes.VoxelShape;

import net.minecraftforge.common.capabilities.ForgeCapabilities;

import net.minecraftforge.common.util.LazyOptional;

import net.minecraftforge.fluids.FluidType;

import net.minecraftforge.network.NetworkHooks;

import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;



/**

 * Land cart prototype: boat steering for now, with cart-sized collision and land movement.

 */

public class CartEntity extends Boat implements HasCustomInventoryScreen, ContainerEntity {

    public static final int CHEST_SLOTS = 27;

    /** World-space footprint (blocks). Length runs along entity facing (+Z when yaw = 0). */

    public static final float WIDTH = 1.0F;

    public static final float LENGTH = 2.0F;

    public static final float HEIGHT = 0.75F;



    /** Small lift so the rendered hull clears grass (matches {@link com.torr.materia.client.renderer.entity.CartRenderer}). */

    public static final float RENDER_Y_OFFSET = 0.0625F;

    /** Wheel radius in blocks — template mesh is 1 block across, scaled in {@link com.torr.materia.client.model.CartModel}. */
    public static final float WHEEL_RADIUS = 0.375F;
    public static final float WHEEL_THICKNESS = 0.0625F;

    /** Fraction of the unit render cube used for the floor slab (walls fill the rest). */
    public static final float FLOOR_HEIGHT_FRACTION = 0.15F;
    /** Wall thickness as a fraction of the unit render cube width/length. */
    public static final float WALL_THICKNESS_FRACTION = 0.0625F;

    /** Blocks forward of entity center to the draft hitch (front wall + arms). */
    public static final float DRAFT_HOOK_FORWARD = LENGTH * 0.5F + 6.5F / 16.0F;
    public static final float DRAFT_HOOK_HEIGHT = RENDER_Y_OFFSET + WHEEL_RADIUS + HEIGHT * 0.55F;

    private static final double LEASH_TRANSFER_RANGE = 8.0D;

    private NonNullList<ItemStack> itemStacks = NonNullList.withSize(CHEST_SLOTS, ItemStack.EMPTY);

    @Nullable

    private ResourceLocation lootTable;

    private long lootTableSeed;

    private LazyOptional<?> itemHandler = LazyOptional.of(() -> new InvWrapper(this));

    private static final float STEP_HEIGHT = 1.0F;

    private static final double MAX_GROUND_ALIGN_RISE = 1.05D;



    public CartEntity(EntityType<? extends CartEntity> type, Level level) {

        super(type, level);

        this.setMaxUpStep(STEP_HEIGHT);

    }



    public CartEntity(Level level, double x, double y, double z) {

        this(ModEntities.CART.get(), level);

        this.setPos(x, y, z);

        this.xo = x;

        this.yo = y;

        this.zo = z;

    }



    @Override

    public Item getDropItem() {

        return ModItems.CART.get();

    }



    @Override

    public boolean canBoatInFluid(FluidState state) {

        return false;

    }



    @Override

    public boolean canBoatInFluid(FluidType type) {

        return false;

    }



    @Override

    public boolean isUnderWater() {

        return false;

    }



    @Override

    protected AABB makeBoundingBox() {

        return orientedBox(getX(), getY(), getZ(), getYRot(), WIDTH, LENGTH, HEIGHT);

    }



    /**

     * Axis-aligned box enclosing the cart footprint rotated by {@code yRotDegrees}.

     */

    public static AABB orientedBox(double x, double y, double z, float yRotDegrees,

            float width, float length, float height) {

        float halfW = width * 0.5F;

        float halfL = length * 0.5F;

        float rad = yRotDegrees * ((float) Math.PI / 180F);

        float sin = Mth.sin(rad);

        float cos = Mth.cos(rad);



        double minX = Double.POSITIVE_INFINITY;

        double minZ = Double.POSITIVE_INFINITY;

        double maxX = Double.NEGATIVE_INFINITY;

        double maxZ = Double.NEGATIVE_INFINITY;



        for (float lx : new float[] { -halfW, halfW }) {

            for (float lz : new float[] { -halfL, halfL }) {

                double wx = x + (double) (lx * cos - lz * sin);

                double wz = z + (double) (lx * sin + lz * cos);

                minX = Math.min(minX, wx);

                maxX = Math.max(maxX, wx);

                minZ = Math.min(minZ, wz);

                maxZ = Math.max(maxZ, wz);

            }

        }



        return new AABB(minX, y, minZ, maxX, y + height, maxZ);

    }



    /** Half the diagonal of the footprint — used for entity shadow radius. */

    public static float shadowRadius() {

        return (float) (Math.hypot(WIDTH, LENGTH) * 0.5D);

    }



    @Override

    public double getPassengersRidingOffset() {

        return (double) (RENDER_Y_OFFSET + WHEEL_RADIUS + HEIGHT - 0.25F);

    }



    @Override

    protected float getSinglePassengerXOffset() {

        return 0.0F;

    }

    @Override

    protected boolean canAddPassenger(Entity passenger) {

        return passenger instanceof Player && super.canAddPassenger(passenger);

    }

    @Override

    public void tick() {

        super.tick();

        ejectNonPlayerPassengers();

        float yaw = getYRot();

        if (Float.isNaN(lastCollisionYaw) || Math.abs(yaw - lastCollisionYaw) > 0.01F) {

            lastCollisionYaw = yaw;

            setBoundingBox(makeBoundingBox());

        }

        if (!this.level().isClientSide && this.isControlledByLocalInstance()) {

            alignToGroundUnderFootprint();

        }

    }



    private float lastCollisionYaw = Float.NaN;

    private void ejectNonPlayerPassengers() {

        if (this.level().isClientSide()) {

            return;

        }

        for (Entity passenger : this.getPassengers()) {

            if (!(passenger instanceof Player)) {

                passenger.stopRiding();

            }

        }

    }

    private void alignToGroundUnderFootprint() {

        double groundY = sampleHighestGroundY(this.level(), getX(), getY(), getZ(), getYRot(), WIDTH, LENGTH);

        if (groundY == Double.NEGATIVE_INFINITY) {

            return;

        }

        double dy = groundY - this.getY();

        if (dy >= -0.25D && dy <= MAX_GROUND_ALIGN_RISE) {

            this.setPos(this.getX(), groundY, this.getZ());

        }

    }



    /**

     * Highest collision surface under the cart footprint (entity feet sit at this Y).

     */

    public static double sampleHighestGroundY(Level level, double x, double y, double z, float yRotDegrees,

            float width, float length) {

        float halfW = width * 0.5F;

        float halfL = length * 0.5F;

        float rad = yRotDegrees * ((float) Math.PI / 180F);

        float sin = Mth.sin(rad);

        float cos = Mth.cos(rad);



        double maxTop = Double.NEGATIVE_INFINITY;

        float[][] samples = {

                { -halfW, -halfL },

                { halfW, -halfL },

                { halfW, halfL },

                { -halfW, halfL },

                { 0.0F, 0.0F }

        };



        int baseY = Mth.floor(y + 0.25D);

        for (float[] sample : samples) {

            double wx = x + (double) (sample[0] * cos - sample[1] * sin);

            double wz = z + (double) (sample[0] * sin + sample[1] * cos);

            int px = Mth.floor(wx);

            int pz = Mth.floor(wz);



            for (int dy = 2; dy >= -4; --dy) {

                BlockPos pos = new BlockPos(px, baseY + dy, pz);

                BlockState state = level.getBlockState(pos);

                VoxelShape shape = state.getCollisionShape(level, pos);

                if (shape.isEmpty()) {

                    continue;

                }

                maxTop = Math.max(maxTop, (double) pos.getY() + shape.max(Direction.Axis.Y));

                break;

            }

        }



        return maxTop;

    }



    @Override

    public Vec3 getDismountLocationForPassenger(net.minecraft.world.entity.LivingEntity passenger) {

        float rad = getYRot() * ((float) Math.PI / 180F);

        double sideX = -Mth.cos(rad) * (WIDTH * 0.5F + 0.5F);

        double sideZ = -Mth.sin(rad) * (WIDTH * 0.5F + 0.5F);

        return new Vec3(getX() + sideX, getY() + HEIGHT, getZ() + sideZ);

    }

    @Override

    protected Vec3 getLeashOffset() {

        float rad = getYRot() * ((float) Math.PI / 180F);

        return new Vec3(-Mth.sin(rad) * DRAFT_HOOK_FORWARD, DRAFT_HOOK_HEIGHT, Mth.cos(rad) * DRAFT_HOOK_FORWARD);

    }

    @Override

    public Vec3 getRopeHoldPosition(float partialTick) {

        return this.getPosition(partialTick).add(this.getLeashOffset());

    }

    @Override

    public InteractionResult interact(Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(Items.LEAD)) {

            if (!this.level().isClientSide()) {

                if (player.isShiftKeyDown()) {

                    releaseLeashedMobs(player);

                    return InteractionResult.SUCCESS;

                }

                if (transferPlayerLeashedMob(player)) {

                    return InteractionResult.SUCCESS;

                }

                if (attachNearbyMob(player)) {

                    return InteractionResult.SUCCESS;

                }

            }

            return InteractionResult.sidedSuccess(this.level().isClientSide());

        }

        if (player.isSecondaryUseActive()) {

            return this.interactWithContainerVehicle(player);

        }

        return super.interact(player, hand);

    }

    private void releaseLeashedMobs(Player player) {

        for (Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate(LEASH_TRANSFER_RANGE))) {

            if (entity instanceof Leashable leashable && leashable.getLeashHolder() == this) {

                leashable.dropLeash(true, !player.getAbilities().instabuild);

            }

        }

    }

    private boolean transferPlayerLeashedMob(Player player) {

        for (Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate(LEASH_TRANSFER_RANGE))) {

            if (entity instanceof Leashable leashable && leashable.isLeashed()

                    && leashable.getLeashHolder() == player) {

                leashable.setLeashedTo(this, true);

                return true;

            }

        }

        return false;

    }

    private boolean attachNearbyMob(Player player) {

        Entity closest = null;

        double closestDistSq = Double.MAX_VALUE;

        for (Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate(LEASH_TRANSFER_RANGE))) {

            if (!(entity instanceof Leashable leashable) || !leashable.canHaveALeashAttachedToIt()) {

                continue;

            }

            double distSq = entity.distanceToSqr(this);

            if (distSq < closestDistSq) {

                closestDistSq = distSq;

                closest = entity;

            }

        }

        if (closest instanceof Leashable leashable) {

            leashable.setLeashedTo(this, true);

            return true;

        }

        return false;

    }

    public void loadInventoryFromItem(ItemStack stack) {

        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();

        if (!tag.isEmpty()) {

            this.readChestVehicleSaveData(tag);

        }

    }

    @Override

    protected void destroy(DamageSource source) {

        if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {

            ItemStack drop = new ItemStack(this.getDropItem());

            if (this.hasCustomName()) {

                drop.set(DataComponents.CUSTOM_NAME, this.getCustomName());

            }

            if (!this.isChestVehicleEmpty()) {

                CompoundTag tag = new CompoundTag();

                this.addChestVehicleSaveData(tag);

                drop.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

            }

            this.spawnAtLocation(drop);

        }

    }

    @Override

    public void remove(Entity.RemovalReason reason) {

        super.remove(reason);

    }

    @Override

    protected void addAdditionalSaveData(CompoundTag tag) {

        super.addAdditionalSaveData(tag);

        this.addChestVehicleSaveData(tag);

    }

    @Override

    protected void readAdditionalSaveData(CompoundTag tag) {

        super.readAdditionalSaveData(tag);

        this.readChestVehicleSaveData(tag);

    }

    @Override

    public void openCustomInventoryScreen(Player player) {

        if (player instanceof ServerPlayer serverPlayer) {

            NetworkHooks.openScreen(serverPlayer, this, buf -> buf.writeVarInt(this.getId()));

        }

        if (!player.level().isClientSide()) {

            this.gameEvent(GameEvent.CONTAINER_OPEN, player);

        }

    }

    @Override

    public void stopOpen(Player player) {

        this.level().gameEvent(GameEvent.CONTAINER_CLOSE, this.position(), GameEvent.Context.of(player));

    }

    @Override

    public int getContainerSize() {

        return CHEST_SLOTS;

    }

    @Override

    public boolean isEmpty() {

        return this.isChestVehicleEmpty();

    }

    @Override

    public ItemStack getItem(int slot) {

        return this.getChestVehicleItem(slot);

    }

    @Override

    public ItemStack removeItem(int slot, int amount) {

        return this.removeChestVehicleItem(slot, amount);

    }

    @Override

    public ItemStack removeItemNoUpdate(int slot) {

        return this.removeChestVehicleItemNoUpdate(slot);

    }

    @Override

    public void setItem(int slot, ItemStack stack) {

        this.setChestVehicleItem(slot, stack);

    }

    @Override

    public SlotAccess getSlot(int slot) {

        return this.getChestVehicleSlot(slot);

    }

    @Override

    public void setChanged() {

    }

    @Override

    public boolean stillValid(Player player) {

        return this.isChestVehicleStillValid(player);

    }

    @Override

    public void clearContent() {

        this.clearChestVehicleContent();

    }

    @Override

    @Nullable

    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {

        if (this.lootTable != null && player.isSpectator()) {

            return null;

        }

        this.unpackChestVehicleLootTable(playerInventory.player);

        return new CartMenu(containerId, playerInventory, this);

    }

    @Override

    public Component getDisplayName() {

        return Component.translatable("container.materia.cart");

    }

    @Override

    @Nullable

    public ResourceLocation getLootTable() {

        return this.lootTable;

    }

    @Override

    public void setLootTable(@Nullable ResourceLocation lootTable) {

        this.lootTable = lootTable;

    }

    @Override

    public long getLootTableSeed() {

        return this.lootTableSeed;

    }

    @Override

    public void setLootTableSeed(long seed) {

        this.lootTableSeed = seed;

    }

    @Override

    public NonNullList<ItemStack> getItemStacks() {

        return this.itemStacks;

    }

    @Override

    public void clearItemStacks() {

        this.itemStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);

    }

    @Override

    public <T> LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability,

            @Nullable Direction facing) {

        if (capability == ForgeCapabilities.ITEM_HANDLER && this.isAlive()) {

            return this.itemHandler.cast();

        }

        return super.getCapability(capability, facing);

    }

    @Override

    public void invalidateCaps() {

        super.invalidateCaps();

        this.itemHandler.invalidate();

    }

    @Override

    public void reviveCaps() {

        super.reviveCaps();

        this.itemHandler = LazyOptional.of(() -> new InvWrapper(this));

    }

}


