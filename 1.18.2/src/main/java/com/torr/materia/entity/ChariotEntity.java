package com.torr.materia.entity;

import com.torr.materia.ModChariots;
import com.torr.materia.ModEntities;
import com.torr.materia.item.ChariotType;
import com.torr.materia.materia;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.animal.horse.Mule;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ChariotEntity extends Boat {
    private static final EntityDataAccessor<Integer> DATA_CHARIOT_TYPE =
            SynchedEntityData.defineId(ChariotEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_HEALTH =
            SynchedEntityData.defineId(ChariotEntity.class, EntityDataSerializers.INT);

    public static final float WIDTH = 1.0F;
    public static final float LENGTH = 1.0F;
    public static final float HEIGHT = 1.0F;
    public static final float RENDER_Y_OFFSET = 0.0625F;
    public static final float WHEEL_RADIUS = 0.375F;
    public static final float WHEEL_ROTATION_FACTOR = 0.5F;
    public static final float FLOOR_HEIGHT_FRACTION = 0.125F;
    public static final float DRIVER_FORWARD_OFFSET = LENGTH * 0.5F - 4.0F / 16.0F;
    public static final float ARCHER_FORWARD_OFFSET = -(LENGTH * 0.5F - 4.0F / 16.0F);
    public static final float DRAFT_HOOK_FORWARD = LENGTH * 0.5F + 1.0F;
    public static final float DRAFT_HOOK_HEIGHT = RENDER_Y_OFFSET + WHEEL_RADIUS;

    private static final int MAX_PLAYER_PASSENGERS = 2;
    private static final double LEASH_TRANSFER_RANGE = 8.0D;
    private static final float STEP_HEIGHT = 1.0F;
    private static final double MAX_GROUND_ALIGN_RISE = 1.05D;
    private static final double COAST_DRAG = 0.93D;
    private static final double MAX_SPEED_PER_PULL = 0.40D;
    private static final double DRAFT_SPEED_CHASE = 0.48D;
    private static final double AIR_DRAG = 0.82D;
    private static final double AIR_SPEED_FACTOR = 0.55D;
    private static final double MIN_DRAFT_PULL = 0.2D;
    private static final double FAST_SURFACE_SPEED_FACTOR = 2.0D;
    private static final TagKey<Block> CART_FAST_SURFACES = TagKey.create(
            Registry.BLOCK_REGISTRY, new ResourceLocation("materia", "cart_fast_surfaces"));
    private static final TagKey<Block> CART_SURFACE_SNOW = TagKey.create(
            Registry.BLOCK_REGISTRY, new ResourceLocation("materia", "cart_surface_snow"));
    private static final TagKey<Block> CART_SURFACE_SAND = TagKey.create(
            Registry.BLOCK_REGISTRY, new ResourceLocation("materia", "cart_surface_sand"));
    private static final TagKey<Block> CART_SURFACE_GRAVEL = TagKey.create(
            Registry.BLOCK_REGISTRY, new ResourceLocation("materia", "cart_surface_gravel"));
    private static final TagKey<Block> CART_SURFACE_GRASS = TagKey.create(
            Registry.BLOCK_REGISTRY, new ResourceLocation("materia", "cart_surface_grass"));
    private static final TagKey<Block> CART_SURFACE_DIRT = TagKey.create(
            Registry.BLOCK_REGISTRY, new ResourceLocation("materia", "cart_surface_dirt"));
    private static final TagKey<Block> CART_SURFACE_COBBLE = TagKey.create(
            Registry.BLOCK_REGISTRY, new ResourceLocation("materia", "cart_surface_cobble"));
    private static final TagKey<Block> CART_SURFACE_WOOD = TagKey.create(
            Registry.BLOCK_REGISTRY, new ResourceLocation("materia", "cart_surface_wood"));
    private static final TagKey<Block> CART_SURFACE_STONE = TagKey.create(
            Registry.BLOCK_REGISTRY, new ResourceLocation("materia", "cart_surface_stone"));
    private static final int MOVE_SOUND_SEGMENT_COUNT = 3;
    private static final int MOVE_SOUND_SEGMENT_INTERVAL = 20;
    private static final int DRAFT_SOUND_MIN_INTERVAL = 8;
    private static final int DRAFT_SOUND_MAX_INTERVAL = 18;
    private static final double MOVE_SOUND_MIN_SPEED = 0.03D;
    private static final double DRAFT_GALLOP_MIN_SPEED = 0.095D;
    private static final float DRAFT_TURN_RATE = 2.8F;
    private static final float DRAFT_CART_YAW_CATCHUP = 0.12F;
    private static final double DRAFT_LEAD_FORWARD = DRAFT_HOOK_FORWARD + 1.25D;
    private static final double DRAFT_ROW_SPACING = 2.5D;
    private static final double DRAFT_SIDE_SPREAD = 0.75D;

    private float draftHeading;
    private boolean draftWasOnGround = true;
    private final List<UUID> pendingDraftTeamRestore = new ArrayList<>();
    private final List<UUID> savedDraftTeam = new ArrayList<>();
    private int draftRestoreGraceTicks;
    public float wheelRotation;
    private int moveSoundTicks;
    private int moveSoundSegment;
    private int draftSoundTicks;
    @Nullable
    private ChariotMoveSurface lastMoveSurface;
    private float lastCollisionYaw = Float.NaN;

    private enum ChariotMoveSurface {
        STONE, COBBLE, GRAVEL, WOOD, GRASS, SAND, DIRT, SNOW, WATER
    }

    public ChariotEntity(EntityType<? extends ChariotEntity> type, Level level) {
        super(type, level);
        this.draftHeading = this.getYRot();
    }

    public ChariotEntity(Level level, double x, double y, double z) {
        this(ModEntities.CHARIOT.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_CHARIOT_TYPE, ChariotType.BRONZE.networkId());
        this.entityData.define(DATA_HEALTH, (int) (ChariotType.BRONZE.getMaxHealth() * 10.0F));
    }

    public ChariotType getChariotType() {
        return ChariotType.fromNetworkId(this.entityData.get(DATA_CHARIOT_TYPE));
    }

    public void setChariotType(ChariotType type) {
        ChariotType resolved = type == null ? ChariotType.BRONZE : type;
        this.entityData.set(DATA_CHARIOT_TYPE, resolved.networkId());
        this.setChariotHealth(Mth.clamp(this.getChariotHealth(), 0.0F, resolved.getMaxHealth()));
    }

    public float getMaxHealth() {
        return this.getChariotType().getMaxHealth();
    }

    public float getHealthRatio() {
        float max = this.getMaxHealth();
        if (max <= 0.0F) {
            return 0.0F;
        }
        return Mth.clamp(this.getChariotHealth() / max, 0.0F, 1.0F);
    }

    public float getMassFactor() {
        return this.getChariotType().getMassFactor();
    }

    public float getChariotHealth() {
        return this.entityData.get(DATA_HEALTH) / 10.0F;
    }

    public void setChariotHealth(float health) {
        int scaled = Mth.clamp((int) (health * 10.0F), 0, (int) (this.getMaxHealth() * 10.0F));
        this.entityData.set(DATA_HEALTH, scaled);
    }

    @Override
    public Item getDropItem() {
        return ModChariots.get(this.getChariotType()).get();
    }

    @Override
    public boolean isUnderWater() {
        return false;
    }

    @Override
    protected AABB makeBoundingBox() {
        return orientedBox(getX(), getY(), getZ(), getYRot(), WIDTH, LENGTH, HEIGHT);
    }

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

    public static float shadowRadius() {
        return (float) (Math.hypot(WIDTH, LENGTH) * 0.5D);
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double) (RENDER_Y_OFFSET + WHEEL_RADIUS + HEIGHT - 0.50F);
    }

    protected float getSinglePassengerXOffset() {
        return 0.0F;
    }

    protected float getPassengerForwardOffset(Entity passenger) {
        if (!(passenger instanceof Player)) {
            return 0.0F;
        }
        int playerIndex = 0;
        for (Entity entity : this.getPassengers()) {
            if (entity == passenger) {
                break;
            }
            if (entity instanceof Player) {
                playerIndex++;
            }
        }
        return playerIndex == 0 ? DRIVER_FORWARD_OFFSET : ARCHER_FORWARD_OFFSET;
    }

    @Override
    public void positionRider(Entity passenger) {
        if (!(passenger instanceof Player)) {
            return;
        }
        this.clampRotation(passenger);
        Vec3 forward = this.getForward();
        float along = this.getPassengerForwardOffset(passenger);
        passenger.setPos(
                this.getX() + forward.x * along,
                this.getY() + this.getPassengersRidingOffset() + passenger.getMyRidingOffset(),
                this.getZ() + forward.z * along);
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        if (!(passenger instanceof Player)) {
            return false;
        }
        int playerCount = 0;
        for (Entity entity : this.getPassengers()) {
            if (entity instanceof Player) {
                playerCount++;
            }
        }
        return playerCount < MAX_PLAYER_PASSENGERS && super.canAddPassenger(passenger);
    }

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
        if (!this.level.isClientSide()) {
            this.draftRestoreGraceTicks = 200;
            this.pendingDraftTeamRestore.clear();
            this.pendingDraftTeamRestore.addAll(this.savedDraftTeam);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.enforcePassengerLimits();
        float yaw = getYRot();
        if (Float.isNaN(lastCollisionYaw) || Math.abs(yaw - lastCollisionYaw) > 0.01F) {
            lastCollisionYaw = yaw;
            setBoundingBox(makeBoundingBox());
        }
        if (!this.level.isClientSide()) {
            this.undoBoatLandMomentumDrain();
            this.dampenAirborneBoatCarryover();
            this.restoreDraftTeamIfNeeded();
            this.applyDraftDrive();
            this.positionDraftTeam();
            this.draftWasOnGround = this.onGround;
            this.tickMovementSounds();
            this.tickDraftSounds();
        }
        if (!this.level.isClientSide() && this.isControlledByLocalInstance()) {
            this.alignToGroundUnderFootprint();
        }
    }

    private void undoBoatLandMomentumDrain() {
        if (this.isInWater() || this.isUnderWater() || this.isChariotAirborne()) {
            return;
        }
        Vec3 motion = this.getDeltaMovement();
        if (Math.abs(motion.y) > 0.08D) {
            return;
        }
        this.setDeltaMovement(motion.x * 2.0D, motion.y, motion.z * 2.0D);
    }

    private boolean isChariotAirborne() {
        Vec3 motion = this.getDeltaMovement();
        if (motion.y < -0.03D || motion.y > 0.06D) {
            return true;
        }
        return !this.onGround;
    }

    private void dampenAirborneBoatCarryover() {
        if (!this.isChariotAirborne()) {
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
            if (!this.isChariotAirborne()) {
                this.accelerateAlongDraftHeading(pull, forward, surfaceFactor);
                this.clampDraftSpeed(pull, surfaceFactor);
            } else {
                this.applyAirborneDrag(pull);
            }
        } else if (!this.isChariotAirborne()) {
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
        if (!this.isChariotAirborne()) {
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
        for (Mob mob : this.level.getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(LEASH_TRANSFER_RANGE))) {
            if (mob.isLeashed() && mob.getLeashHolder() == this && isDraftEligible(mob)) {
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
        boolean cartAirborne = this.isChariotAirborne();
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
                ty = sampleHighestGroundY(this.level, tx, this.getY(), tz, this.draftHeading, 0.6F, 0.6F);
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

    private double getSurfaceSpeedFactor() {
        if (this.level.isClientSide()) {
            return 1.0D;
        }
        int smooth = this.countFootprintBlocksInTag(CART_FAST_SURFACES);
        if (smooth == 0) {
            return 1.0D;
        }
        double blend = (double) smooth / 5.0D;
        return 1.0D + blend * (FAST_SURFACE_SPEED_FACTOR - 1.0D);
    }

    private int countFootprintBlocksInTag(TagKey<Block> tag) {
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
        int hits = 0;
        int probeY = Mth.floor(y - 0.0625D);
        for (float[] sample : samples) {
            double wx = x + (double) (sample[0] * cos - sample[1] * sin);
            double wz = z + (double) (sample[0] * sin + sample[1] * cos);
            BlockPos pos = new BlockPos(Mth.floor(wx), probeY, Mth.floor(wz));
            if (this.level.getBlockState(pos).is(tag)) {
                hits++;
            }
        }
        return hits;
    }

    private ChariotMoveSurface classifyMoveSurface(BlockState state) {
        if (state.is(CART_SURFACE_SNOW)) {
            return ChariotMoveSurface.SNOW;
        }
        if (state.is(CART_SURFACE_SAND)) {
            return ChariotMoveSurface.SAND;
        }
        if (state.is(CART_SURFACE_GRAVEL)) {
            return ChariotMoveSurface.GRAVEL;
        }
        if (state.is(CART_SURFACE_GRASS)) {
            return ChariotMoveSurface.GRASS;
        }
        if (state.is(CART_SURFACE_DIRT)) {
            return ChariotMoveSurface.DIRT;
        }
        if (state.is(CART_SURFACE_COBBLE)) {
            return ChariotMoveSurface.COBBLE;
        }
        if (state.is(CART_SURFACE_WOOD)) {
            return ChariotMoveSurface.WOOD;
        }
        if (state.is(CART_SURFACE_STONE)) {
            return ChariotMoveSurface.STONE;
        }
        return ChariotMoveSurface.DIRT;
    }

    private ChariotMoveSurface sampleDominantMoveSurface() {
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
        int[] counts = new int[ChariotMoveSurface.values().length];
        int probeY = Mth.floor(y - 0.0625D);
        for (float[] sample : samples) {
            double wx = x + (double) (sample[0] * cos - sample[1] * sin);
            double wz = z + (double) (sample[0] * sin + sample[1] * cos);
            BlockPos pos = new BlockPos(Mth.floor(wx), probeY, Mth.floor(wz));
            ChariotMoveSurface surface = this.classifyMoveSurface(this.level.getBlockState(pos));
            counts[surface.ordinal()]++;
        }
        int bestIndex = 0;
        int bestCount = 0;
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] > bestCount) {
                bestCount = counts[i];
                bestIndex = i;
            }
        }
        return bestCount > 0 ? ChariotMoveSurface.values()[bestIndex] : ChariotMoveSurface.DIRT;
    }

    private static String moveSurfaceSoundId(ChariotMoveSurface surface) {
        return switch (surface) {
            case STONE -> "stone";
            case COBBLE -> "cobble";
            case GRAVEL -> "gravel";
            case WOOD -> "wood";
            case GRASS -> "grass";
            case SAND -> "sand";
            case DIRT -> "dirt";
            case SNOW -> "snow";
            case WATER -> "water";
        };
    }

    private SoundEvent resolveMoveSound(ChariotMoveSurface surface, int segment) {
        int index = Mth.clamp(segment, 0, MOVE_SOUND_SEGMENT_COUNT - 1);
        return new SoundEvent(new ResourceLocation(materia.MOD_ID,
                "entity.cart.move_" + moveSurfaceSoundId(surface) + "." + index));
    }

    private void playMoveSound(ChariotMoveSurface surface, double speed, int segment) {
        SoundEvent sound = resolveMoveSound(surface, segment);
        float volume = Mth.clamp((float) (speed * 2.5D), 0.15F, 0.9F);
        float pitch = 0.9F + Mth.clamp((float) (speed * 0.5D), 0.0F, 0.2F);
        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), sound, SoundSource.NEUTRAL, volume, pitch);
    }

    private void tickMovementSounds() {
        Vec3 motion = this.getDeltaMovement();
        double speed = Math.hypot(motion.x, motion.z);
        if (speed < MOVE_SOUND_MIN_SPEED) {
            this.moveSoundTicks = 0;
            this.moveSoundSegment = 0;
            this.lastMoveSurface = null;
            return;
        }
        boolean inWater = this.isInWater();
        if (!inWater && (!this.onGround || this.isUnderWater())) {
            this.moveSoundTicks = 0;
            this.moveSoundSegment = 0;
            this.lastMoveSurface = null;
            return;
        }
        ChariotMoveSurface surface = inWater ? ChariotMoveSurface.WATER : this.sampleDominantMoveSurface();
        boolean playNow = this.lastMoveSurface == null || this.lastMoveSurface != surface;
        if (playNow) {
            this.moveSoundSegment = 0;
        }
        this.lastMoveSurface = surface;
        this.moveSoundTicks++;
        if (!playNow && this.moveSoundTicks < MOVE_SOUND_SEGMENT_INTERVAL) {
            return;
        }
        this.moveSoundTicks = 0;
        this.playMoveSound(surface, speed, this.moveSoundSegment);
        this.moveSoundSegment = (this.moveSoundSegment + 1) % MOVE_SOUND_SEGMENT_COUNT;
    }

    private int computeDraftSoundInterval(double speed) {
        return Mth.clamp((int) Math.round(20.0D - speed * 80.0D), DRAFT_SOUND_MIN_INTERVAL, DRAFT_SOUND_MAX_INTERVAL);
    }

    private void tickDraftSounds() {
        if (this.isInWater() || this.isUnderWater() || !this.onGround) {
            this.draftSoundTicks = 0;
            return;
        }
        List<Mob> draft = this.collectDraftMobs();
        int equines = 0;
        for (Mob mob : draft) {
            if (mob instanceof AbstractHorse) {
                equines++;
            }
        }
        if (equines == 0) {
            this.draftSoundTicks = 0;
            return;
        }
        Vec3 motion = this.getDeltaMovement();
        double speed = Math.hypot(motion.x, motion.z);
        if (speed < MOVE_SOUND_MIN_SPEED) {
            this.draftSoundTicks = 0;
            return;
        }
        int interval = this.computeDraftSoundInterval(speed);
        this.draftSoundTicks++;
        if (this.draftSoundTicks < interval) {
            return;
        }
        this.draftSoundTicks = 0;
        SoundEvent sound = speed >= DRAFT_GALLOP_MIN_SPEED
                ? SoundEvents.HORSE_GALLOP
                : SoundEvents.HORSE_STEP;
        float volume = Mth.clamp(0.22F + equines * 0.12F, 0.22F, 0.85F);
        float pitch = 0.88F + Mth.clamp((float) (speed * 0.4D), 0.0F, 0.18F)
                + (this.random.nextFloat() - 0.5F) * 0.06F;
        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), sound, SoundSource.NEUTRAL, volume, pitch);
    }

    private double computeDraftPull() {
        double pull = 0.0D;
        for (Mob mob : this.level.getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(LEASH_TRANSFER_RANGE))) {
            if (mob.isLeashed() && mob.getLeashHolder() == this && isDraftEligible(mob)) {
                pull += draftPullFor(mob);
            }
        }
        return normalizeDraftPull(pull);
    }

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

    private void enforcePassengerLimits() {
        if (this.level.isClientSide()) {
            return;
        }
        int keptPlayers = 0;
        for (Entity passenger : this.getPassengers()) {
            if (passenger instanceof Player) {
                if (keptPlayers >= MAX_PLAYER_PASSENGERS) {
                    passenger.stopRiding();
                } else {
                    keptPlayers++;
                }
            } else {
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
    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
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
            if (!this.level.isClientSide()) {
                if (player.isShiftKeyDown()) {
                    this.releaseLeashedMobs(player);
                    return InteractionResult.SUCCESS;
                }
                if (this.transferPlayerLeashedMob(player)) {
                    return InteractionResult.SUCCESS;
                }
                if (this.attachNearbyMob(player)) {
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide());
        }
        if (player.isShiftKeyDown() && player.getVehicle() != this) {
            if (this.tryPickUp(player)) {
                return InteractionResult.sidedSuccess(this.level.isClientSide());
            }
        }
        return super.interact(player, hand);
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
        stack.getOrCreateTag().putFloat("ChariotHealth", this.getChariotHealth());
        this.giveOrDropItem(player, stack);
        this.level.playSound(null, this.blockPosition(), SoundEvents.WOOD_PLACE, SoundSource.PLAYERS, 0.9F, 1.0F);
        this.discard();
        return true;
    }

    private void releaseLeashedMobs(Player player) {
        for (Mob mob : this.level.getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(LEASH_TRANSFER_RANGE))) {
            if (mob.getLeashHolder() == this) {
                mob.dropLeash(true, !player.getAbilities().instabuild);
                this.unregisterDraftMob(mob.getUUID());
            }
        }
    }

    private void registerDraftMob(Mob mob) {
        if (!this.savedDraftTeam.contains(mob.getUUID())) {
            this.savedDraftTeam.add(mob.getUUID());
        }
    }

    private void unregisterDraftMob(UUID uuid) {
        this.savedDraftTeam.remove(uuid);
        this.pendingDraftTeamRestore.remove(uuid);
    }

    private void leashDraftMob(Mob mob) {
        mob.setLeashedTo(this, true);
        this.registerDraftMob(mob);
    }

    private boolean transferPlayerLeashedMob(Player player) {
        for (Mob mob : this.level.getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(LEASH_TRANSFER_RANGE))) {
            if (mob.isLeashed() && mob.getLeashHolder() == player && isDraftEligible(mob)) {
                this.leashDraftMob(mob);
                return true;
            }
        }
        return false;
    }

    private boolean attachNearbyMob(Player player) {
        Mob closest = null;
        double closestDistSq = Double.MAX_VALUE;
        for (Mob mob : this.level.getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(LEASH_TRANSFER_RANGE))) {
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
            this.leashDraftMob(closest);
            return true;
        }
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source) || this.isRemoved()) {
            return false;
        }
        if (this.level.isClientSide()) {
            return false;
        }
        amount = this.getChariotType().applyDamageAmount(source, amount);
        if (amount <= 0.0F) {
            return false;
        }
        this.setChariotHealth(this.getChariotHealth() - amount);
        this.setHurtTime(10);
        this.markHurt();
        if (this.getChariotHealth() <= 0.0F) {
            if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                ItemStack stack = new ItemStack(this.getDropItem());
                if (this.hasCustomName()) {
                    stack.setHoverName(this.getCustomName());
                }
                stack.getOrCreateTag().putFloat("ChariotHealth", this.getChariotHealth());
                this.spawnAtLocation(stack);
            }
            this.discard();
        }
        return true;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("DraftHeading", this.draftHeading);
        tag.putFloat("ChariotHealth", this.getChariotHealth());
        tag.putString("ChariotType", this.getChariotType().getId());
        this.addDraftTeamSaveData(tag);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.draftHeading = tag.contains("DraftHeading") ? tag.getFloat("DraftHeading") : this.getYRot();
        if (tag.contains("ChariotType")) {
            ChariotType.fromId(tag.getString("ChariotType")).ifPresent(this::setChariotType);
        }
        if (tag.contains("ChariotHealth")) {
            this.setChariotHealth(tag.getFloat("ChariotHealth"));
        }
        this.readDraftTeamSaveData(tag);
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
        if (this.pendingDraftTeamRestore.isEmpty() || !(this.level instanceof ServerLevel serverLevel)) {
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
            if (!(entity instanceof Mob mob) || !mob.isAlive() || !isDraftEligible(mob)) {
                iterator.remove();
                this.savedDraftTeam.remove(uuid);
                continue;
            }
            if (mob.getLeashHolder() != this) {
                mob.setLeashedTo(this, true);
            }
            if (mob.getLeashHolder() == this) {
                iterator.remove();
            }
        }
    }
}
