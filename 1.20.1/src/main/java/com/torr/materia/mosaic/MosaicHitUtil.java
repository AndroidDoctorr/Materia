package com.torr.materia.mosaic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

/** Maps block-face hits to 16×16 mosaic pixel coordinates (origin bottom-left of face texture). */
public final class MosaicHitUtil {
    private MosaicHitUtil() {
    }

    public record FacePixel(Direction face, int x, int y) {
    }

    public static FacePixel fromHit(BlockPos pos, BlockHitResult hit) {
        Direction face = hit.getDirection();
        Vec3 local = hit.getLocation().subtract(pos.getX(), pos.getY(), pos.getZ());
        int[] pixel = MosaicFaceGeometry.localToPixel(face, local.x, local.y, local.z);
        return new FacePixel(face, pixel[0], pixel[1]);
    }
}
