package com.torr.materia.entity;



import com.torr.materia.ModCarts;

import com.torr.materia.ModEntities;

import com.torr.materia.ModItems;
import com.torr.materia.events.CartSleepHandler;
import com.torr.materia.item.CartCoverColor;
import com.torr.materia.item.CartCoverItem;
import com.torr.materia.item.CartWoodType;
import com.torr.materia.menu.CartMenu;

import net.minecraft.core.BlockPos;

import net.minecraft.core.Direction;

import net.minecraft.core.NonNullList;

import net.minecraft.core.registries.Registries;

import net.minecraft.core.component.DataComponents;

import net.minecraft.nbt.CompoundTag;

import net.minecraft.nbt.ListTag;

import net.minecraft.nbt.StringTag;

import net.minecraft.nbt.Tag;

import net.minecraft.network.chat.Component;

import net.minecraft.network.syncher.EntityDataAccessor;

import net.minecraft.network.syncher.EntityDataSerializers;

import net.minecraft.network.syncher.SynchedEntityData;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.server.level.ServerLevel;

import net.minecraft.server.level.ServerPlayer;

import net.minecraft.tags.TagKey;

import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import net.minecraft.world.InteractionHand;

import net.minecraft.world.InteractionResult;

import net.minecraft.world.damagesource.DamageSource;

import net.minecraft.world.entity.Entity;

import net.minecraft.world.entity.EntityDimensions;

import net.minecraft.world.entity.EntityType;

import net.minecraft.world.entity.LivingEntity;

import net.minecraft.world.entity.HasCustomInventoryScreen;

import net.minecraft.world.entity.Leashable;

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

import net.minecraft.world.item.component.CustomData;

import net.minecraft.world.level.GameRules;

import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.Block;

import net.minecraft.world.level.block.Blocks;

import net.minecraft.world.level.block.LightBlock;

import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.gameevent.GameEvent;

import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootTable;

import net.minecraft.world.phys.AABB;

import net.minecraft.world.phys.Vec3;

import net.minecraft.world.phys.shapes.VoxelShape;

import net.minecraftforge.common.capabilities.ForgeCapabilities;

import net.minecraftforge.common.util.LazyOptional;

import net.minecraftforge.fluids.FluidType;

import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;



/**

 * Land cart prototype: boat steering for now, with cart-sized collision and land movement.

 */

public class CartEntity extends Boat implements HasCustomInventoryScreen, ContainerEntity {

    public static final int CHEST_SLOTS = 27;

    public static final float MAX_HEALTH = 60.0F;

    private static final EntityDataAccessor<Integer> DATA_COVER_COLOR =
            SynchedEntityData.defineId(CartEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_HAS_LANTERN =
            SynchedEntityData.defineId(CartEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_HEALTH =
            SynchedEntityData.defineId(CartEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_WOOD_TYPE =
            SynchedEntityData.defineId(CartEntity.class, EntityDataSerializers.INT);

    /** World-space footprint (blocks). Length runs along entity facing (+Z when yaw = 0). */

    public static final float WIDTH = 1.0F;

    public static final float LENGTH = 2.0F;

    public static final float HEIGHT = 0.75F;



    /** Small lift so the rendered hull clears grass (matches {@link com.torr.materia.client.renderer.entity.CartRenderer}). */

    public static final float RENDER_Y_OFFSET = 0.0625F;

    /** Wheel radius in blocks — template mesh is 1 block across, scaled in {@link com.torr.materia.client.model.CartModel}. */
    public static final float WHEEL_RADIUS = 0.375F;
    /** Visual-only scale for wheel roll vs travel distance (client renderer). */
    public static final float WHEEL_ROTATION_FACTOR = 0.5F;
    public static final float WHEEL_THICKNESS = 0.0625F;

    /** Fraction of the unit render cube used for the floor slab (walls fill the rest). */
    public static final float FLOOR_HEIGHT_FRACTION = 0.15F;
    /** Wall thickness as a fraction of the unit render cube width/length. */
    public static final float WALL_THICKNESS_FRACTION = 0.0625F;

    /** Blocks forward of entity center to the draft hitch (front wall + arms). */
    public static final float DRAFT_HOOK_FORWARD = LENGTH * 0.5F + 6.5F / 16.0F;
    public static final float DRAFT_HOOK_HEIGHT = RENDER_Y_OFFSET + WHEEL_RADIUS + HEIGHT * 0.55F;

    /** How far forward (entity facing) the rider and sleep point sit from center. */
    public static final float PASSENGER_FORWARD_OFFSET = 0.25F;

    private static final double LEASH_TRANSFER_RANGE = 8.0D;

    private NonNullList<ItemStack> itemStacks = NonNullList.withSize(CHEST_SLOTS, ItemStack.EMPTY);

    @Nullable

    private ResourceKey<LootTable> lootTable;

    private long lootTableSeed;

    private LazyOptional<?> itemHandler = LazyOptional.of(() -> new InvWrapper(this));

    private static final float STEP_HEIGHT = 1.0F;

    private static final double MAX_GROUND_ALIGN_RISE = 1.05D;

    private static final double COAST_DRAG = 0.93D;
    /** Target blocks/tick per draft pull unit at full W input on flat ground. */
    private static final double MAX_SPEED_PER_PULL = 0.40D;
    /** How quickly forward speed catches up to the draft target each tick. */
    private static final double DRAFT_SPEED_CHASE = 0.48D;
    /** Horizontal speed bleed while the cart is airborne. */
    private static final double AIR_DRAG = 0.82D;
    /** Airborne speed cap as a fraction of grounded draft speed. */
    private static final double AIR_SPEED_FACTOR = 0.55D;
    private static final double MIN_DRAFT_PULL = 0.2D;
    /** Speed multiplier on smooth surfaces (paths, planks, paved stone). */
    private static final double FAST_SURFACE_SPEED_FACTOR = 2.0D;
    private static final TagKey<Block> CART_FAST_SURFACES = TagKey.create(
            Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("materia", "cart_fast_surfaces"));
    /** Degrees per tick of draft heading change at full strafe input. */
    private static final float DRAFT_TURN_RATE = 2.8F;
    /** How quickly cart yaw catches up to the draft team heading. */
    private static final float DRAFT_CART_YAW_CATCHUP = 0.12F;
    /** Blocks ahead of cart center for the lead draft animal. */
    private static final double DRAFT_LEAD_FORWARD = DRAFT_HOOK_FORWARD + 1.25D;
    private static final double DRAFT_ROW_SPACING = 2.5D;
    private static final double DRAFT_SIDE_SPREAD = 0.75D;

    /** Heading of the leashed draft team; steered with A/D, not player look. */
    private float draftHeading;
    private boolean draftWasOnGround = true;
    /** Draft mob UUIDs to re-leash after world load (entity IDs are not stable across saves). */
    private final List<UUID> pendingDraftTeamRestore = new ArrayList<>();
    /** Authoritative draft roster persisted in NBT (updated on attach/release, not only at save). */
    private final List<UUID> savedDraftTeam = new ArrayList<>();
    /** Ticks to keep retrying draft restore while nearby entities finish loading. */
    private int draftRestoreGraceTicks;

    /** Client-side wheel roll angle (radians), advanced from travel distance in {@link com.torr.materia.client.renderer.entity.CartRenderer}. */
    public float wheelRotation;

    @Nullable
    private BlockPos lanternLightPos;



    public CartEntity(EntityType<? extends CartEntity> type, Level level) {

        super(type, level);

        this.draftHeading = this.getYRot();

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_COVER_COLOR, 0);
        builder.define(DATA_HAS_LANTERN, false);
        builder.define(DATA_HEALTH, (int) (CartWoodType.OAK.getMaxHealth() * 10.0F));
        builder.define(DATA_WOOD_TYPE, CartWoodType.OAK.networkId());
    }

    public CartWoodType getWoodType() {
        return CartWoodType.fromNetworkId(this.entityData.get(DATA_WOOD_TYPE));
    }

    public void setWoodType(CartWoodType woodType) {
        CartWoodType resolved = woodType == null ? CartWoodType.OAK : woodType;
        this.entityData.set(DATA_WOOD_TYPE, resolved.networkId());
        this.setCartHealth(Mth.clamp(this.getCartHealth(), 0.0F, resolved.getMaxHealth()));
    }

    public float getMaxHealth() {
        return this.getWoodType().getMaxHealth();
    }

    /** Current health as 0–1 for UI; same bar width at full HP regardless of wood type. */
    public float getHealthRatio() {
        float max = this.getMaxHealth();
        if (max <= 0.0F) {
            return 0.0F;
        }
        return Mth.clamp(this.getCartHealth() / max, 0.0F, 1.0F);
    }

    public float getMassFactor() {
        return this.getWoodType().getMassFactor();
    }

    public boolean hasCover() {
        return this.getCoverColor().isPresent();
    }

    public java.util.Optional<CartCoverColor> getCoverColor() {
        return CartCoverColor.fromNetworkId(this.entityData.get(DATA_COVER_COLOR));
    }

    public void setCoverColor(CartCoverColor color) {
        this.entityData.set(DATA_COVER_COLOR, color == null ? 0 : color.networkId());
    }

    public boolean hasLantern() {
        return this.entityData.get(DATA_HAS_LANTERN);
    }

    public void setHasLantern(boolean hasLantern) {
        this.entityData.set(DATA_HAS_LANTERN, hasLantern);
        if (!hasLantern) {
            this.clearLanternLight();
        }
    }

    public float getCartHealth() {
        return this.entityData.get(DATA_HEALTH) / 10.0F;
    }

    public void setCartHealth(float health) {
        int scaled = Mth.clamp((int) (health * 10.0F), 0, (int) (this.getMaxHealth() * 10.0F));
        this.entityData.set(DATA_HEALTH, scaled);
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

        return ModCarts.get(this.getWoodType()).get();

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



    protected Vec3 getPassengerSeatOffset() {
        Vec3 forward = this.getForward();
        return new Vec3(
                forward.x * PASSENGER_FORWARD_OFFSET,
                0.0D,
                forward.z * PASSENGER_FORWARD_OFFSET);
    }

    /** How far the rider drops when lying down in the cart bed. */
    private static final double CART_SLEEP_Y_OFFSET = 0.45D;

    @Override
    public Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float partialTicks) {
        Vec3 seat = this.getPassengerSeatOffset();
        Vec3 base = super.getPassengerAttachmentPoint(passenger, dimensions, partialTicks);
        if (passenger instanceof Player player && CartSleepHandler.shouldSkipPassengerPositioning(player)) {
            return base.add(seat.x, -CART_SLEEP_Y_OFFSET, seat.z);
        }
        return base.add(seat.x, -0.25, seat.z);
    }



    @Override

    public float maxUpStep() {

        return STEP_HEIGHT;

    }



    @Override

    protected float getSinglePassengerXOffset() {

        return 0.0F;

    }

    @Override

    protected boolean canAddPassenger(Entity passenger) {

        return passenger instanceof Player && this.getPassengers().isEmpty() && super.canAddPassenger(passenger);

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
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (!this.level().isClientSide()) {
            this.draftRestoreGraceTicks = 200;
            this.pendingDraftTeamRestore.clear();
            this.pendingDraftTeamRestore.addAll(this.savedDraftTeam);
        }
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

            this.dampenAirborneBoatCarryover();

            this.restoreDraftTeamIfNeeded();

            this.applyDraftDrive();

            this.positionDraftTeam();

            this.draftWasOnGround = this.onGround();

            this.updateLanternLight();

        }

        if (!this.level().isClientSide() && this.isControlledByLocalInstance()) {

            alignToGroundUnderFootprint();

        }

    }

    private void undoBoatLandMomentumDrain() {

        if (this.isInWater() || this.isUnderWater() || this.isCartAirborne()) {

            return;

        }

        Vec3 motion = this.getDeltaMovement();

        if (Math.abs(motion.y) > 0.08D) {

            return;

        }

        this.setDeltaMovement(motion.x * 2.0D, motion.y, motion.z * 2.0D);

    }

    /** True when the cart should not receive draft acceleration (cliffs, jumps, boat float carryover). */
    private boolean isCartAirborne() {

        Vec3 motion = this.getDeltaMovement();

        if (motion.y < -0.03D || motion.y > 0.06D) {

            return true;

        }

        return !this.onGround();

    }

    private void dampenAirborneBoatCarryover() {

        if (!this.isCartAirborne()) {

            return;

        }

        Vec3 motion = this.getDeltaMovement();

        this.setDeltaMovement(motion.x * 0.65D, motion.y, motion.z * 0.65D);

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

        double surfaceFactor = this.getSurfaceSpeedFactor();

        if (forward > 0.01F && pull >= MIN_DRAFT_PULL) {

            if (!this.isCartAirborne()) {

                this.accelerateAlongDraftHeading(pull, forward, surfaceFactor);

                this.clampDraftSpeed(pull, surfaceFactor);

            } else {

                this.applyAirborneDrag(pull);

            }

        } else if (!this.isCartAirborne()) {

            this.suppressBoatPropulsion();

            this.applyCoastDrag();

        } else {

            this.applyAirborneDrag(pull);

        }

    }

    private void accelerateAlongDraftHeading(double pull, float forward, double surfaceFactor) {

        float rad = this.draftHeading * ((float) Math.PI / 180F);

        double fwdX = -Math.sin(rad);

        double fwdZ = Math.cos(rad);

        double mass = this.getMassFactor();

        double targetSpeed = MAX_SPEED_PER_PULL * pull * forward * surfaceFactor / mass;

        Vec3 motion = this.getDeltaMovement();

        double along = motion.x * fwdX + motion.z * fwdZ;

        along = along + (targetSpeed - along) * (DRAFT_SPEED_CHASE / mass);

        this.setDeltaMovement(fwdX * along, motion.y, fwdZ * along);

    }

    private void applyAirborneDrag(double pull) {

        Vec3 motion = this.getDeltaMovement();

        double hx = motion.x * AIR_DRAG;

        double hz = motion.z * AIR_DRAG;

        double coastCap = MAX_SPEED_PER_PULL * Math.max(pull, MIN_DRAFT_PULL) * AIR_SPEED_FACTOR / this.getMassFactor();

        double horizontalSpeed = Math.hypot(hx, hz);

        if (horizontalSpeed > coastCap && horizontalSpeed > 1.0E-4D) {

            double scale = coastCap / horizontalSpeed;

            hx *= scale;

            hz *= scale;

        }

        this.setDeltaMovement(hx, motion.y, hz);

    }

    private void applyCoastDrag() {

        if (!this.isCartAirborne()) {

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

        for (Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate(LEASH_TRANSFER_RANGE))) {

            if (entity instanceof Mob mob && entity instanceof Leashable leashable

                    && leashable.isLeashed() && leashable.getLeashHolder() == this && isDraftEligible(mob)) {

                draft.add(mob);

            }

        }

        draft.sort((left, right) -> {

            int cmp = Double.compare(draftPullFor(right), draftPullFor(left));

            return cmp != 0 ? cmp : Integer.compare(left.getId(), right.getId());

        });

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

        Vec3 cartMotion = this.getDeltaMovement();

        double horizSpeed = Math.hypot(cartMotion.x, cartMotion.z);

        Vec3 draftMotion = new Vec3(cartMotion.x, 0.0D, cartMotion.z);

        boolean cartAirborne = this.isCartAirborne();

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

            double ty;

            if (cartAirborne) {

                ty = this.getY();

            } else {

                ty = sampleHighestGroundY(this.level(), tx, this.getY(), tz, this.draftHeading, 0.6F, 0.6F);

                if (ty == Double.NEGATIVE_INFINITY) {

                    ty = this.getY();

                }

            }

            mob.moveTo(tx, ty, tz, this.draftHeading, mob.getXRot());

            mob.setYBodyRot(this.draftHeading);

            mob.setYHeadRot(this.draftHeading);

            this.syncDraftMobMotion(mob, horizSpeed, draftMotion);

        }

    }

    private void syncDraftMobMotion(Mob mob, double horizSpeed, Vec3 draftMotion) {

        mob.setDeltaMovement(draftMotion);

        mob.setSpeed((float) Mth.clamp(horizSpeed * 4.0D, 0.0D, 1.0D));

        mob.setSprinting(horizSpeed > 0.12D);

        if (mob instanceof AbstractHorse horse) {

            horse.setEating(false);

            if (horizSpeed > 0.05D) {

                horse.setStanding(false);

            }

        }

        mob.getNavigation().stop();

    }

    private void clampDraftSpeed(double pull, double surfaceFactor) {

        double maxSpeed = MAX_SPEED_PER_PULL * pull * surfaceFactor / this.getMassFactor();

        Vec3 motion = this.getDeltaMovement();

        double horizontalSpeed = Math.hypot(motion.x, motion.z);

        if (horizontalSpeed > maxSpeed && horizontalSpeed > 1.0E-4D) {

            double scale = maxSpeed / horizontalSpeed;

            this.setDeltaMovement(motion.x * scale, motion.y, motion.z * scale);

        }

    }

    /** Blend speed from 1x on rough ground up to {@link #FAST_SURFACE_SPEED_FACTOR} on smooth surfaces. */
    private double getSurfaceSpeedFactor() {

        if (this.level().isClientSide()) {

            return 1.0D;

        }

        double x = this.getX();

        double y = this.getY();

        double z = this.getZ();

        float halfW = WIDTH * 0.5F;

        float halfL = LENGTH * 0.5F;

        float rad = this.getYRot() * ((float) Math.PI / 180F);

        float sin = Mth.sin(rad);

        float cos = Mth.cos(rad);

        float[][] samples = {

                { -halfW, -halfL }, { halfW, -halfL }, { halfW, halfL }, { -halfW, halfL }, { 0.0F, 0.0F }

        };

        int smooth = 0;

        int probeY = Mth.floor(y - 0.0625D);

        for (float[] sample : samples) {

            double wx = x + (double) (sample[0] * cos - sample[1] * sin);

            double wz = z + (double) (sample[0] * sin + sample[1] * cos);

            BlockPos pos = BlockPos.containing(wx, probeY, wz);

            if (this.level().getBlockState(pos).is(CART_FAST_SURFACES)) {

                smooth++;

            }

        }

        if (smooth == 0) {

            return 1.0D;

        }

        double blend = (double) smooth / samples.length;

        return 1.0D + blend * (FAST_SURFACE_SPEED_FACTOR - 1.0D);

    }

    private double computeDraftPull() {

        double pull = 0.0D;

        for (Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate(LEASH_TRANSFER_RANGE))) {

            if (entity instanceof Mob mob && entity instanceof Leashable leashable

                    && leashable.isLeashed() && leashable.getLeashHolder() == this && isDraftEligible(mob)) {

                pull += draftPullFor(mob);

            }

        }

        return normalizeDraftPull(pull);

    }

    /** Extra draft animals help, but with diminishing returns so teams stay realistic. */
    private static double normalizeDraftPull(double rawPull) {

        if (rawPull <= 1.0D) {

            return rawPull;

        }

        return 1.0D + (rawPull - 1.0D) * 0.55D;

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

        boolean keptPlayer = false;

        for (Entity passenger : this.getPassengers()) {

            if (!(passenger instanceof Player)) {

                passenger.stopRiding();

            } else if (keptPlayer) {

                passenger.stopRiding();

            } else {

                keptPlayer = true;

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

    public Vec3 getLeashOffset() {

        float rad = getYRot() * ((float) Math.PI / 180F);

        return new Vec3(-Mth.sin(rad) * DRAFT_HOOK_FORWARD, DRAFT_HOOK_HEIGHT, Mth.cos(rad) * DRAFT_HOOK_FORWARD);

    }

    @Override

    public Vec3 getRopeHoldPosition(float partialTick) {

        return this.getPosition(partialTick).add(this.getLeashOffset());

    }

    public static boolean canSleepAt(Level level) {

        if (level.dimensionType().hasFixedTime()) {

            return false;

        }

        long time = level.getDayTime() % 24000L;

        return level.isThundering() || (time >= 12541L && time <= 23992L);

    }

    private void updateLanternLight() {
        if (this.level().isClientSide()) {
            return;
        }
        if (!this.hasLantern() || !canSleepAt(this.level())) {
            this.clearLanternLight();
            return;
        }
        Vec3 forward = this.getForward();
        BlockPos target = BlockPos.containing(
                this.getX() + forward.x * DRAFT_HOOK_FORWARD * 0.85D,
                this.getY() + 0.85D,
                this.getZ() + forward.z * DRAFT_HOOK_FORWARD * 0.85D);
        if (this.lanternLightPos != null && this.lanternLightPos.equals(target)
                && this.level().getBlockState(this.lanternLightPos).is(Blocks.LIGHT)) {
            return;
        }
        this.clearLanternLight();
        BlockState lightState = Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, 12);
        if (this.level().getBlockState(target).isAir() && this.level().setBlock(target, lightState, 3)) {
            this.lanternLightPos = target;
        }
    }

    private void clearLanternLight() {
        if (this.lanternLightPos != null && this.level().getBlockState(this.lanternLightPos).is(Blocks.LIGHT)) {
            this.level().removeBlock(this.lanternLightPos, false);
        }
        this.lanternLightPos = null;
    }

    public void trySleep(Player player) {

        if (player.level().isClientSide()) {

            return;

        }

        if (CartSleepHandler.isCartSleeping(player) || !canSleepAt(player.level())) {

            return;

        }

        if (!this.isChestVehicleStillValid(player)) {

            return;

        }

        if (player.getVehicle() != this) {

            return;

        }

        player.closeContainer();

        CartSleepHandler.beginCartSleep(player, this);

    }

    @Override

    public InteractionResult interact(Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (!player.isSecondaryUseActive() && !stack.is(Items.LEAD)) {
            CartCoverColor coverColor = CartCoverItem.getColor(stack);
            if (coverColor != null && !this.hasCover()) {
                if (!this.level().isClientSide()) {
                    this.setCoverColor(coverColor);
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    this.gameEvent(GameEvent.ENTITY_INTERACT, player);
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide());
            }
            if (stack.is(Items.LANTERN) && !this.hasLantern()) {
                if (!this.level().isClientSide()) {
                    this.setHasLantern(true);
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    this.gameEvent(GameEvent.ENTITY_INTERACT, player);
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide());
            }
            if (this.getWoodType().isRepairItem(stack)
                    && this.getCartHealth() < this.getMaxHealth() - 0.05F) {
                if (!this.level().isClientSide()) {
                    this.setCartHealth(this.getCartHealth() + CartWoodType.REPAIR_AMOUNT);
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    this.gameEvent(GameEvent.ENTITY_INTERACT, player);
                    this.level().playSound(null, this.blockPosition(), SoundEvents.WOOD_PLACE, SoundSource.PLAYERS,
                            0.8F, 1.0F);
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide());
            }
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

        if (player.isShiftKeyDown() && player.getVehicle() != this) {
            InteractionResult dismantle = this.tryDismantleInteraction(player);
            if (dismantle != InteractionResult.PASS) {
                return InteractionResult.sidedSuccess(this.level().isClientSide());
            }
        }

        if (player.getVehicle() == this && !player.isShiftKeyDown() && stack.isEmpty()) {
            if (!this.level().isClientSide()) {
                this.openCustomInventoryScreen(player);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }

        return super.interact(player, hand);

    }

    private InteractionResult tryDismantleInteraction(Player player) {
        if (this.level().isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        if (this.hasLeashedDraftTeam()) {
            this.releaseLeashedMobs(player);
            this.level().playSound(null, this.blockPosition(), SoundEvents.LEASH_KNOT_BREAK, SoundSource.PLAYERS,
                    1.0F, 1.0F);
            this.gameEvent(GameEvent.ENTITY_INTERACT, player);
            return InteractionResult.SUCCESS;
        }
        if (this.hasCover()) {
            CartCoverColor color = this.getCoverColor().orElse(CartCoverColor.WHITE);
            this.setCoverColor(null);
            this.giveOrDropItem(player, new ItemStack(ModItems.getCartCover(color).get()));
            this.level().playSound(null, this.blockPosition(), SoundEvents.WOOL_PLACE, SoundSource.PLAYERS, 0.8F,
                    1.0F);
            this.gameEvent(GameEvent.ENTITY_INTERACT, player);
            return InteractionResult.SUCCESS;
        }
        if (this.hasLantern()) {
            this.setHasLantern(false);
            this.giveOrDropItem(player, new ItemStack(Items.LANTERN));
            this.level().playSound(null, this.blockPosition(), SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 1.0F,
                    1.0F);
            this.gameEvent(GameEvent.ENTITY_INTERACT, player);
            return InteractionResult.SUCCESS;
        }
        if (this.tryPickUp(player)) {
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private void giveOrDropItem(Player player, ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            this.spawnAtLocation(stack);
        }
    }

    private boolean tryPickUp(Player player) {
        if (!this.getPassengers().isEmpty()) {
            return false;
        }
        ItemStack stack = new ItemStack(this.getDropItem());
        if (this.hasCustomName()) {
            stack.setHoverName(this.getCustomName());
        }
        CompoundTag tag = stack.getOrCreateTag();
        if (!this.isChestVehicleEmpty()) {
            this.addChestVehicleSaveData(tag);
        }
        tag.putFloat("CartHealth", this.getCartHealth());
        this.giveOrDropItem(player, stack);
        this.gameEvent(GameEvent.ENTITY_INTERACT, player);
        this.level().playSound(null, this.blockPosition(), SoundEvents.WOOD_PLACE, SoundSource.PLAYERS, 0.9F, 1.0F);
        this.discard();
        return true;
    }

    private boolean hasLeashedDraftTeam() {

        for (Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate(LEASH_TRANSFER_RANGE))) {

            if (entity instanceof Leashable leashable && leashable.getLeashHolder() == this) {

                return true;

            }

        }

        return false;

    }

    private void releaseLeashedMobs(Player player) {

        for (Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate(LEASH_TRANSFER_RANGE))) {

            if (entity instanceof Leashable leashable && leashable.getLeashHolder() == this) {

                leashable.dropLeash(true, !player.getAbilities().instabuild);

                this.unregisterDraftMob(entity.getUUID());

            }

        }

    }

    private void registerDraftMob(Entity entity) {

        if (!this.savedDraftTeam.contains(entity.getUUID())) {

            this.savedDraftTeam.add(entity.getUUID());

        }

    }

    private void unregisterDraftMob(UUID uuid) {

        this.savedDraftTeam.remove(uuid);

        this.pendingDraftTeamRestore.remove(uuid);

    }

    private void leashDraftMob(Entity entity) {

        if (entity instanceof Leashable leashable) {

            leashable.setLeashedTo(this, true);

            this.registerDraftMob(entity);

        }

    }

    private boolean transferPlayerLeashedMob(Player player) {

        for (Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate(LEASH_TRANSFER_RANGE))) {

            if (entity instanceof Leashable leashable && leashable.isLeashed()

                    && leashable.getLeashHolder() == player && entity instanceof Mob mob

                    && isDraftEligible(mob)) {

                this.leashDraftMob(entity);

                return true;

            }

        }

        return false;

    }

    private boolean attachNearbyMob(Player player) {

        Entity closest = null;

        double closestDistSq = Double.MAX_VALUE;

        for (Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate(LEASH_TRANSFER_RANGE))) {

            if (!(entity instanceof Mob mob) || !(entity instanceof Leashable leashable)

                    || !leashable.canHaveALeashAttachedToIt() || !isDraftEligible(mob)) {

                continue;

            }

            double distSq = entity.distanceToSqr(this);

            if (distSq < closestDistSq) {

                closestDistSq = distSq;

                closest = entity;

            }

        }

        if (closest != null) {

            this.leashDraftMob(closest);

            return true;

        }

        return false;

    }

    public void loadInventoryFromItem(ItemStack stack) {

        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();

        if (!tag.isEmpty()) {

            this.readChestVehicleSaveData(tag, this.registryAccess());

        }

    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source) || this.isRemoved()) {
            return false;
        }
        if (this.level().isClientSide()) {
            return false;
        }
        amount = this.getWoodType().applyDamageAmount(source, amount);
        if (amount <= 0.0F) {
            return false;
        }
        this.setCartHealth(this.getCartHealth() - amount);
        this.setHurtTime(10);
        this.markHurt();
        if (this.getCartHealth() <= 0.0F) {
            this.destroy(source);
            this.discard();
        }
        return true;
    }

    private void dropAttachmentItems() {
        this.getCoverColor().ifPresent(color ->
                this.spawnAtLocation(new ItemStack(ModItems.getCartCover(color).get())));
        if (this.hasLantern()) {
            this.spawnAtLocation(new ItemStack(Items.LANTERN));
        }
    }

    @Override
    protected void destroy(DamageSource source) {

        if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {

            this.dropAttachmentItems();

            ItemStack drop = new ItemStack(this.getDropItem());

            if (this.hasCustomName()) {

                drop.set(DataComponents.CUSTOM_NAME, this.getCustomName());

            }

            if (!this.isChestVehicleEmpty()) {

                CompoundTag tag = new CompoundTag();

                this.addChestVehicleSaveData(tag, this.registryAccess());

                drop.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

            }

            this.spawnAtLocation(drop);

        }

    }

    @Override

    public void remove(Entity.RemovalReason reason) {

        this.clearLanternLight();

        super.remove(reason);

    }

    @Override

    protected void addAdditionalSaveData(CompoundTag tag) {

        super.addAdditionalSaveData(tag);

        tag.putFloat("DraftHeading", this.draftHeading);

        this.getCoverColor().ifPresent(color -> tag.putString("CoverColor", color.getId()));

        tag.putBoolean("HasLantern", this.hasLantern());

        tag.putFloat("CartHealth", this.getCartHealth());

        tag.putString("WoodType", this.getWoodType().getId());

        this.addDraftTeamSaveData(tag);

        this.addChestVehicleSaveData(tag, this.registryAccess());

    }

    @Override

    protected void readAdditionalSaveData(CompoundTag tag) {

        super.readAdditionalSaveData(tag);

        this.draftHeading = tag.contains("DraftHeading") ? tag.getFloat("DraftHeading") : this.getYRot();

        if (tag.contains("CoverColor")) {
            CartCoverColor.fromId(tag.getString("CoverColor")).ifPresent(this::setCoverColor);
        } else if (tag.contains("HasCover") && tag.getBoolean("HasCover")) {
            this.setCoverColor(CartCoverColor.TAUPE);
        }
        if (tag.contains("HasLantern")) {
            this.setHasLantern(tag.getBoolean("HasLantern"));
        }
        if (tag.contains("WoodType")) {
            CartWoodType.fromId(tag.getString("WoodType")).ifPresent(this::setWoodType);
        }
        if (tag.contains("CartHealth")) {
            this.setCartHealth(tag.getFloat("CartHealth"));
        }

        this.readDraftTeamSaveData(tag);

        this.readChestVehicleSaveData(tag, this.registryAccess());

    }

    private void addDraftTeamSaveData(CompoundTag tag) {

        this.syncSavedDraftTeamFromWorld();

        ListTag list = new ListTag();

        for (UUID uuid : this.savedDraftTeam) {

            list.add(StringTag.valueOf(uuid.toString()));

        }

        tag.put("DraftTeam", list);

    }

    private void syncSavedDraftTeamFromWorld() {

        for (Mob mob : this.collectDraftMobs()) {

            this.registerDraftMob(mob);

        }

    }

    private void readDraftTeamSaveData(CompoundTag tag) {

        this.savedDraftTeam.clear();

        this.pendingDraftTeamRestore.clear();

        if (tag.contains("DraftTeam", Tag.TAG_LIST)) {

            ListTag list = tag.getList("DraftTeam", Tag.TAG_STRING);

            for (int i = 0; i < list.size(); i++) {

                UUID uuid = UUID.fromString(list.getString(i));

                this.savedDraftTeam.add(uuid);

                this.pendingDraftTeamRestore.add(uuid);

            }

        }

        this.draftRestoreGraceTicks = 200;

    }

    @Nullable
    private Entity findLoadedDraftEntity(ServerLevel level, UUID uuid) {

        Entity entity = level.getEntity(uuid);

        if (entity != null) {

            return entity;

        }

        for (Mob mob : level.getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(64.0D))) {

            if (mob.getUUID().equals(uuid)) {

                return mob;

            }

        }

        return null;

    }

    private void restoreDraftTeamIfNeeded() {

        if (this.pendingDraftTeamRestore.isEmpty() || !(this.level() instanceof ServerLevel serverLevel)) {

            return;

        }

        if (this.draftRestoreGraceTicks > 0) {

            this.draftRestoreGraceTicks--;

        }

        Iterator<UUID> iterator = this.pendingDraftTeamRestore.iterator();

        while (iterator.hasNext()) {

            UUID uuid = iterator.next();

            Entity entity = this.findLoadedDraftEntity(serverLevel, uuid);

            if (entity == null) {

                if (this.draftRestoreGraceTicks <= 0) {

                    iterator.remove();

                    this.savedDraftTeam.remove(uuid);

                }

                continue;

            }

            if (!(entity instanceof Mob mob) || !(entity instanceof Leashable leashable)

                    || !mob.isAlive() || !isDraftEligible(mob)) {

                iterator.remove();

                this.savedDraftTeam.remove(uuid);

                continue;

            }

            if (leashable.getLeashHolder() != this) {

                leashable.setLeashedTo(this, true);

            }

            if (leashable.getLeashHolder() == this) {

                iterator.remove();

            }

        }

    }

    @Override

    public void openCustomInventoryScreen(Player player) {

        if (player instanceof ServerPlayer serverPlayer) {

            serverPlayer.openMenu(this, buf -> buf.writeVarInt(this.getId()));

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

    public ResourceKey<LootTable> getLootTable() {

        return this.lootTable;

    }

    @Override

    public void setLootTable(@Nullable ResourceKey<LootTable> lootTable) {

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


