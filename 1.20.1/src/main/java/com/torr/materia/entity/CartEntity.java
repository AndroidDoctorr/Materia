package com.torr.materia.entity;



import com.torr.materia.ModEntities;

import com.torr.materia.ModItems;

import net.minecraft.core.BlockPos;

import net.minecraft.core.Direction;

import net.minecraft.util.Mth;

import net.minecraft.world.entity.EntityType;

import net.minecraft.world.entity.vehicle.Boat;

import net.minecraft.world.item.Item;

import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.material.FluidState;

import net.minecraft.world.phys.AABB;

import net.minecraft.world.phys.Vec3;

import net.minecraft.world.phys.shapes.VoxelShape;

import net.minecraftforge.fluids.FluidType;



/**

 * Land cart prototype: boat steering for now, with cart-sized collision and land movement.

 */

public class CartEntity extends Boat {



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

    public void tick() {

        super.tick();

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

}


