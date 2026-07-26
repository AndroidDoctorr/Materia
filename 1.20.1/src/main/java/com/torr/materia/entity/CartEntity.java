package com.torr.materia.entity;



import com.torr.materia.ModEntities;

import com.torr.materia.ModItems;

import com.torr.materia.menu.CartMenu;

import net.minecraft.core.BlockPos;

import net.minecraft.core.Direction;

import net.minecraft.core.NonNullList;

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

import net.minecraft.world.entity.LivingEntity;

import net.minecraft.world.entity.HasCustomInventoryScreen;

import net.minecraft.world.entity.Mob;

import net.minecraft.world.entity.SlotAccess;

import net.minecraft.world.entity.player.Inventory;

import net.minecraft.world.entity.player.Player;

import net.minecraft.world.entity.animal.horse.AbstractHorse;

import net.minecraft.world.entity.animal.horse.Donkey;

import net.minecraft.world.entity.animal.horse.Llama;

import net.minecraft.world.entity.animal.horse.Mule;

import net.minecraft.world.entity.vehicle.Boat;

import net.minecraft.world.entity.vehicle.ContainerEntity;

import net.minecraft.world.inventory.AbstractContainerMenu;

import net.minecraft.world.item.Item;

import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.Items;

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

import java.util.ArrayList;
import java.util.List;



/**

 * Land cart prototype: boat steering for now, with cart-sized collision and land movement.

 */

public class CartEntity extends Boat implements HasCustomInventoryScreen, ContainerEntity {

    public static final int CHEST_SLOTS = 27;

    /** World-space footprint (blocks). Length runs along entity facing (+Z when yaw = 0). */

    public static final float WIDTH = 1.0F;

    public static final float LENGTH = 2.0F;

    public static final float HEIGHT = 0.75F;



    /** Small lift so wheels clear grass (matches {@link com.torr.materia.client.renderer.entity.CartRenderer}). */
    public static final float RENDER_Y_OFFSET = 0.0625F;

    /** Wheel radius in blocks — template mesh is 1 block across, scaled in {@link com.torr.materia.client.model.CartModel}. */
    public static final float WHEEL_RADIUS = 0.375F;
    /** Wheel thickness along the axle in blocks. */
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

    /** Forward acceleration per tick, scaled by draft pull and driver input. */
    private static final double COAST_DRAG = 0.93D;
    /** Target blocks/tick per draft pull unit (~0.55 ≈ brisk trot for one horse). */
    private static final double MAX_SPEED_PER_PULL = 0.55D;
    /** How quickly forward speed catches up to the draft target each tick. */
    private static final double DRAFT_SPEED_CHASE = 0.4D;
    private static final double MIN_DRAFT_PULL = 0.2D;
    /** Degrees per tick of draft heading change at full strafe input. */
    private static final float DRAFT_TURN_RATE = 2.8F;
    /** How quickly cart yaw catches up to the draft team heading. */
    private static final float DRAFT_CART_YAW_CATCHUP = 0.12F;
    /** Blocks ahead of cart center for the lead draft animal. */
    private static final double DRAFT_LEAD_FORWARD = DRAFT_HOOK_FORWARD + 1.25D;
    private static final double DRAFT_ROW_SPACING = 1.4D;
    private static final double DRAFT_SIDE_SPREAD = 0.75D;

    /** Heading of the leashed draft team; steered with A/D, not player look. */
    private float draftHeading;



    public CartEntity(EntityType<? extends CartEntity> type, Level level) {

        super(type, level);

        this.setMaxUpStep(STEP_HEIGHT);

        this.draftHeading = this.getYRot();

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

    /**
     * Disable vanilla boat paddle / land shuffling — the cart only moves from draft pull.
     */
    @Override
    @Nullable
    public LivingEntity getControllingPassenger() {
        return null;
    }

    @Nullable
    private LivingEntity getDriver() {
        for (Entity passenger : this.getPassengers()) {
            if (passenger instanceof Player player) {
                return player;
            }
        }
        return null;
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

        if (!this.level().isClientSide()) {

            this.undoBoatLandMomentumDrain();

            this.applyDraftDrive();

            this.positionDraftTeam();

        }

        if (!this.level().isClientSide() && this.isControlledByLocalInstance()) {

            alignToGroundUnderFootprint();

        }

    }

    private void undoBoatLandMomentumDrain() {

        if (this.isInWater() || this.isUnderWater()) {

            return;

        }

        Vec3 motion = this.getDeltaMovement();

        this.setDeltaMovement(motion.x * 2.0D, motion.y, motion.z * 2.0D);

    }

    private void applyDraftDrive() {

        LivingEntity driver = this.getDriver();

        if (driver == null) {

            this.suppressBoatPropulsion();

            return;

        }

        List<Mob> draft = this.collectDraftMobs();

        if (draft.isEmpty()) {

            this.suppressBoatPropulsion();

            this.applyCoastDrag();

            return;

        }

        if (Math.abs(driver.xxa) > 0.01F) {

            this.draftHeading = Mth.wrapDegrees(this.draftHeading - driver.xxa * DRAFT_TURN_RATE);

        }

        float cartYaw = lerpYaw(DRAFT_CART_YAW_CATCHUP, this.getYRot(), this.draftHeading);

        this.setYRot(cartYaw);

        double pull = this.computeDraftPull();

        float forward = driver.zza;

        if (forward > 0.01F && pull >= MIN_DRAFT_PULL) {

            this.accelerateAlongDraftHeading(pull, forward);

        } else {

            this.suppressBoatPropulsion();

            this.applyCoastDrag();

        }

    }

    private void accelerateAlongDraftHeading(double pull, float forward) {

        float rad = this.draftHeading * ((float) Math.PI / 180F);

        double fwdX = -Math.sin(rad);

        double fwdZ = Math.cos(rad);

        double targetSpeed = MAX_SPEED_PER_PULL * pull * forward;

        Vec3 motion = this.getDeltaMovement();

        double along = motion.x * fwdX + motion.z * fwdZ;

        along = along + (targetSpeed - along) * DRAFT_SPEED_CHASE;

        this.setDeltaMovement(fwdX * along, motion.y, fwdZ * along);

    }

    private void applyCoastDrag() {

        if (this.onGround()) {

            Vec3 motion = this.getDeltaMovement();

            this.setDeltaMovement(motion.x * COAST_DRAG, motion.y, motion.z * COAST_DRAG);

        }

    }

    private static float lerpYaw(float progress, float from, float to) {

        return from + Mth.wrapDegrees(to - from) * progress;

    }

    private void suppressBoatPropulsion() {

        Vec3 motion = this.getDeltaMovement();

        this.setDeltaMovement(0.0D, motion.y, 0.0D);

    }

    private List<Mob> collectDraftMobs() {

        List<Mob> draft = new ArrayList<>();

        for (Mob mob : this.level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(LEASH_TRANSFER_RANGE))) {

            if (mob.isLeashed() && mob.getLeashHolder() == this && isDraftEligible(mob)) {

                draft.add(mob);

            }

        }

        draft.sort((left, right) -> Double.compare(draftPullFor(right), draftPullFor(left)));

        return draft;

    }

    private void positionDraftTeam() {

        List<Mob> draft = this.collectDraftMobs();

        if (draft.isEmpty()) {

            return;

        }

        float rad = this.draftHeading * ((float) Math.PI / 180F);

        double fwdX = -Math.sin(rad);

        double fwdZ = Math.cos(rad);

        double rightX = Math.cos(rad);

        double rightZ = Math.sin(rad);

        for (int i = 0; i < draft.size(); i++) {

            Mob mob = draft.get(i);

            int row = i / 2;

            double forward = DRAFT_LEAD_FORWARD + row * DRAFT_ROW_SPACING;

            double side = 0.0D;

            if (draft.size() > 1) {

                side = (i % 2 == 0 ? -1.0D : 1.0D) * DRAFT_SIDE_SPREAD;

            }

            double tx = this.getX() + fwdX * forward + rightX * side;

            double tz = this.getZ() + fwdZ * forward + rightZ * side;

            double ty = sampleHighestGroundY(this.level(), tx, this.getY(), tz, this.draftHeading, 0.6F, 0.6F);

            if (ty == Double.NEGATIVE_INFINITY) {

                ty = this.getY();

            }

            mob.teleportTo(tx, ty, tz);

            mob.setYRot(this.draftHeading);

            mob.setYBodyRot(this.draftHeading);

            mob.setYHeadRot(this.draftHeading);

            mob.setDeltaMovement(Vec3.ZERO);

            mob.getNavigation().stop();

        }

    }

    private void clampDraftSpeed(double pull) {

        double maxSpeed = MAX_SPEED_PER_PULL * pull;

        Vec3 motion = this.getDeltaMovement();

        double horizontalSpeed = Math.hypot(motion.x, motion.z);

        if (horizontalSpeed > maxSpeed && horizontalSpeed > 1.0E-4D) {

            double scale = maxSpeed / horizontalSpeed;

            this.setDeltaMovement(motion.x * scale, motion.y, motion.z * scale);

        }

    }

    private double computeDraftPull() {

        double pull = 0.0D;

        for (Mob mob : this.level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(LEASH_TRANSFER_RANGE))) {

            if (mob.isLeashed() && mob.getLeashHolder() == this && isDraftEligible(mob)) {

                pull += draftPullFor(mob);

            }

        }

        return pull;

    }

    private static boolean isDraftEligible(Mob mob) {

        if (mob instanceof AbstractHorse horse) {

            return horse.isTamed();

        }

        return true;

    }

    private static double draftPullFor(Mob mob) {

        if (mob instanceof AbstractHorse) {

            if (mob instanceof Donkey || mob instanceof Mule) {

                return 0.85D;

            }

            return 1.0D;

        }

        if (mob instanceof Llama) {

            return 0.65D;

        }

        if (mob instanceof net.minecraft.world.entity.animal.Cow

                || mob instanceof net.minecraft.world.entity.animal.Sheep

                || mob instanceof net.minecraft.world.entity.animal.Pig

                || mob instanceof net.minecraft.world.entity.animal.goat.Goat) {

            return 0.35D;

        }

        return 0.2D;

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

        if (player.isShiftKeyDown() && this.hasLeashedDraftTeam()) {

            if (!this.level().isClientSide()) {

                this.releaseLeashedMobs(player);

            }

            return InteractionResult.sidedSuccess(this.level().isClientSide());

        }

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

    private boolean hasLeashedDraftTeam() {

        for (Mob mob : this.level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(LEASH_TRANSFER_RANGE))) {

            if (mob.getLeashHolder() == this) {

                return true;

            }

        }

        return false;

    }

    private void releaseLeashedMobs(Player player) {

        for (Mob mob : this.level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(LEASH_TRANSFER_RANGE))) {

            if (mob.getLeashHolder() == this) {

                mob.dropLeash(true, !player.getAbilities().instabuild);

            }

        }

    }

    private boolean transferPlayerLeashedMob(Player player) {

        for (Mob mob : this.level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(LEASH_TRANSFER_RANGE))) {

            if (mob.isLeashed() && mob.getLeashHolder() == player && isDraftEligible(mob)) {

                mob.setLeashedTo(this, true);

                return true;

            }

        }

        return false;

    }

    private boolean attachNearbyMob(Player player) {

        Mob closest = null;

        double closestDistSq = Double.MAX_VALUE;

        for (Mob mob : this.level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(LEASH_TRANSFER_RANGE))) {

            if (!mob.canBeLeashed(player) || !isDraftEligible(mob)) {

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

            this.readChestVehicleSaveData(stack.getTag());

        }

    }

    @Override

    protected void destroy(DamageSource source) {

        if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {

            ItemStack drop = new ItemStack(this.getDropItem());

            if (this.hasCustomName()) {

                drop.setHoverName(this.getCustomName());

            }

            if (!this.isChestVehicleEmpty()) {

                this.addChestVehicleSaveData(drop.getOrCreateTag());

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

        tag.putFloat("DraftHeading", this.draftHeading);

        this.addChestVehicleSaveData(tag);

    }

    @Override

    protected void readAdditionalSaveData(CompoundTag tag) {

        super.readAdditionalSaveData(tag);

        this.draftHeading = tag.contains("DraftHeading") ? tag.getFloat("DraftHeading") : this.getYRot();

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


