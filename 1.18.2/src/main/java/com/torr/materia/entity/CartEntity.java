package com.torr.materia.entity;

import com.torr.materia.ModEntities;
import com.torr.materia.ModItems;
import com.torr.materia.menu.CartMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * Land cart prototype: boat steering for now, with cart-sized collision and land movement.
 */
public class CartEntity extends Boat implements Container, MenuProvider {

    public static final int CHEST_SLOTS = 27;
    /** World-space footprint (blocks). Length runs along entity facing (+Z when yaw = 0). */
    public static final float WIDTH = 1.0F;
    public static final float LENGTH = 2.0F;
    public static final float HEIGHT = 0.75F;

    /** Small lift so the rendered hull clears grass (matches {@link com.torr.materia.client.renderer.entity.CartRenderer}). */
    public static final float RENDER_Y_OFFSET = 0.0625F;

    public static final float WHEEL_RADIUS = 0.375F;
    public static final float WHEEL_THICKNESS = 0.0625F;

    public static final float FLOOR_HEIGHT_FRACTION = 0.15F;
    public static final float WALL_THICKNESS_FRACTION = 0.0625F;

    /** Blocks forward of entity center to the draft hitch (front wall + arms). */
    public static final float DRAFT_HOOK_FORWARD = LENGTH * 0.5F + 6.5F / 16.0F;
    public static final float DRAFT_HOOK_HEIGHT = RENDER_Y_OFFSET + WHEEL_RADIUS + HEIGHT * 0.55F;

    private static final double LEASH_TRANSFER_RANGE = 8.0D;
    private static final double MAX_GROUND_ALIGN_RISE = 1.05D;

    private NonNullList<ItemStack> itemStacks = NonNullList.withSize(CHEST_SLOTS, ItemStack.EMPTY);
    private LazyOptional<?> itemHandler = LazyOptional.of(() -> new InvWrapper(this));

    public CartEntity(EntityType<? extends CartEntity> type, Level level) {
        super(type, level);
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
        if (!this.level.isClientSide && this.isControlledByLocalInstance()) {
            alignToGroundUnderFootprint();
        }
    }

    private float lastCollisionYaw = Float.NaN;

    private void ejectNonPlayerPassengers() {
        if (this.level.isClientSide) {
            return;
        }
        for (Entity passenger : this.getPassengers()) {
            if (!(passenger instanceof Player)) {
                passenger.stopRiding();
            }
        }
    }

    private void alignToGroundUnderFootprint() {
        double groundY = sampleHighestGroundY(this.level, getX(), getY(), getZ(), getYRot(), WIDTH, LENGTH);
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
    public Vec3 getLeashOffset() {
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
            if (!this.level.isClientSide) {
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
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        if (player.isSecondaryUseActive()) {
            if (!this.level.isClientSide && player instanceof ServerPlayer serverPlayer) {
                NetworkHooks.openGui(serverPlayer, this, buf -> buf.writeVarInt(this.getId()));
                this.gameEvent(GameEvent.CONTAINER_OPEN, player);
                return InteractionResult.CONSUME;
            }
            return InteractionResult.SUCCESS;
        }
        return super.interact(player, hand);
    }

    private void releaseLeashedMobs(Player player) {
        for (Mob mob : this.level.getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(LEASH_TRANSFER_RANGE))) {
            if (mob.getLeashHolder() == this) {
                mob.dropLeash(true, !player.getAbilities().instabuild);
            }
        }
    }

    private boolean transferPlayerLeashedMob(Player player) {
        for (Mob mob : this.level.getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(LEASH_TRANSFER_RANGE))) {
            if (mob.isLeashed() && mob.getLeashHolder() == player) {
                mob.setLeashedTo(this, true);
                return true;
            }
        }
        return false;
    }

    private boolean attachNearbyMob(Player player) {
        Mob closest = null;
        double closestDistSq = Double.MAX_VALUE;
        for (Mob mob : this.level.getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(LEASH_TRANSFER_RANGE))) {
            if (!mob.canBeLeashed(player)) {
                continue;
            }
            double distSq = mob.distanceToSqr(this);
            if (distSq < closestDistSq) {
                closestDistSq = distSq;
                closest = mob;
            }
        }
        if (closest != null) {
            closest.setLeashedTo(this, true);
            return true;
        }
        return false;
    }

    public void loadInventoryFromItem(ItemStack stack) {
        if (stack.hasTag()) {
            this.itemStacks = NonNullList.withSize(CHEST_SLOTS, ItemStack.EMPTY);
            ContainerHelper.loadAllItems(stack.getTag(), this.itemStacks);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        if (!this.level.isClientSide && !this.isRemoved()) {
            this.setHurtDir(-this.getHurtDir());
            this.setHurtTime(10);
            this.setDamage(this.getDamage() + amount * 10.0F);
            this.markHurt();
            this.gameEvent(GameEvent.ENTITY_DAMAGED, source.getEntity());
            boolean creative = source.getEntity() instanceof Player player && player.getAbilities().instabuild;
            if (creative || this.getDamage() > 40.0F) {
                if (!creative && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                    this.spawnCartDrop();
                }
                this.discard();
            }
            return true;
        }
        return true;
    }

    private void spawnCartDrop() {
        ItemStack drop = new ItemStack(this.getDropItem());
        if (this.hasCustomName()) {
            drop.setHoverName(this.getCustomName());
        }
        if (!this.isEmpty()) {
            ContainerHelper.saveAllItems(drop.getOrCreateTag(), this.itemStacks);
        }
        this.spawnAtLocation(drop);
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        super.remove(reason);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        ContainerHelper.saveAllItems(tag, this.itemStacks);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.itemStacks = NonNullList.withSize(CHEST_SLOTS, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.itemStacks);
    }

    @Override
    public int getContainerSize() {
        return CHEST_SLOTS;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.itemStacks) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.itemStacks.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(this.itemStacks, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = this.itemStacks.get(slot);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        this.itemStacks.set(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.itemStacks.set(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
    }

    @Override
    public SlotAccess getSlot(final int slot) {
        if (slot < 0 || slot >= this.getContainerSize()) {
            return SlotAccess.NULL;
        }
        return new SlotAccess() {
            @Override
            public ItemStack get() {
                return CartEntity.this.getItem(slot);
            }

            @Override
            public boolean set(ItemStack stack) {
                CartEntity.this.setItem(slot, stack);
                return true;
            }
        };
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player) {
        return !this.isRemoved() && player.distanceToSqr(this) <= 64.0D;
    }

    @Override
    public void clearContent() {
        this.itemStacks.clear();
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new CartMenu(containerId, playerInventory, this);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container.materia.cart");
    }

    @Override
    public <T> LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability,
            @Nullable Direction facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this.isAlive()) {
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
