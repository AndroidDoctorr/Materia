package com.torr.materia.client;

import com.torr.materia.materia;
import com.torr.materia.block.CannonBlock;
import com.torr.materia.client.screen.CannonAimScreen;
import com.torr.materia.util.CannonMath;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Mod.EventBusSubscriber(modid = materia.MOD_ID, value = Dist.CLIENT)
public class CannonAimClientCamera {
    private static boolean active;
    private static BlockPos cannonPos;
    private static float yaw;
    private static float pitch;

    public static void begin(BlockPos pos) {
        active = true;
        cannonPos = pos;
    }

    public static void updateAim(float newYaw, float newPitch) {
        yaw = newYaw;
        pitch = newPitch;
    }

    public static void end() {
        active = false;
        cannonPos = null;
    }

    @SubscribeEvent
    public static void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        if (!active || cannonPos == null) return;

        Minecraft mc = Minecraft.getInstance();
        // Safety: if the aim screen isn't open anymore, stop overriding the camera
        if (!(mc.screen instanceof CannonAimScreen)) {
            end();
            return;
        }
        if (mc.level == null) return;

        BlockState state = mc.level.getBlockState(cannonPos);
        if (!state.hasProperty(CannonBlock.FACING)) return;

        Direction facing = state.getValue(CannonBlock.FACING);
        float baseModelY = CannonMath.facingToModelY(facing);
        float yawWorld = baseModelY + yaw;

        Vec3 dir = directionFromAngles(yawWorld, pitch);

        // Place camera at the cannon's mouth (top-center), slightly nudged along direction
        Vec3 mouth = new Vec3(cannonPos.getX() + 0.5, cannonPos.getY() + 1.0, cannonPos.getZ() + 0.5).add(dir.scale(0.25));

        // Set angles so view points along dir (Minecraft convention: yaw=0 south (+Z), yaw=90 west (-X))
        float camYaw = (float) (Mth.atan2(-dir.x, dir.z) * (180f / Math.PI));
        float camPitch = (float) (Mth.atan2(-dir.y, Math.sqrt(dir.x * dir.x + dir.z * dir.z)) * (180f / Math.PI));
        event.setYaw(camYaw);
        event.setPitch(camPitch);

        // Try to move the camera (Forge's CameraSetup only exposes angles, so we patch Camera position)
        Camera camera = event.getCamera();
        forceSetCameraPosition(camera, mouth);
    }

    private static void forceSetCameraPosition(Camera camera, Vec3 pos) {
        // Prefer a real method if present
        try {
            Method m = Camera.class.getDeclaredMethod("setPosition", double.class, double.class, double.class);
            m.setAccessible(true);
            m.invoke(camera, pos.x, pos.y, pos.z);
            return;
        } catch (Throwable ignored) {
        }

        // Fallback: try common field names across mappings
        String[] candidates = new String[] { "position", "pos", "m_90561_", "f_90552_" };
        for (String name : candidates) {
            try {
                Field f = Camera.class.getDeclaredField(name);
                f.setAccessible(true);
                if (f.getType() == Vec3.class) {
                    f.set(camera, pos);
                    return;
                }
            } catch (Throwable ignored) {
            }
        }
    }

    // Same convention as firing: forward is +Y, apply pitch then yaw
    private static Vec3 directionFromAngles(float yawWorldDeg, float pitchDeg) {
        // Spherical coords with this cannon convention:
        // - pitchDeg: 0 = straight up (+Y), 90 = horizon
        // - yawWorldDeg: 0 = north (-Z), 90 = east (+X), 180 = south (+Z), 270 = west (-X)
        double theta = pitchDeg * Mth.DEG_TO_RAD;
        double phi = yawWorldDeg * Mth.DEG_TO_RAD;

        double sinT = Math.sin(theta);
        double x = sinT * Math.sin(phi);
        double y = Math.cos(theta);
        double z = -sinT * Math.cos(phi);

        return new Vec3(x, y, z).normalize();
    }
}

