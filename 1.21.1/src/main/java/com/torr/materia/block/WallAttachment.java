package com.torr.materia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.Vec3;

/**
 * Shared placement rules for upright wall-mounted panels (ladder-style).
 */
public final class WallAttachment {

    private WallAttachment() {
    }

    public static BlockPos placementPos(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        if (!context.getLevel().getBlockState(pos).canBeReplaced(context)) {
            pos = pos.relative(context.getClickedFace());
        }
        return pos;
    }

    /**
     * Horizontal face the panel mounts on. Side clicks use that face; sill/lintel clicks stay upright
     * on the face toward the player.
     */
    public static Direction uprightFacing(BlockPlaceContext context) {
        Direction clicked = context.getClickedFace();
        if (clicked.getAxis().isHorizontal()) {
            return clicked;
        }
        return context.getHorizontalDirection().getOpposite();
    }

    /**
     * When true, the panel sits on the far side of the block space. Wall-face clicks are always flush
     * (inset). Top/bottom clicks use which half of the face was targeted relative to the player.
     */
    public static boolean isOutsetPlacement(BlockPlaceContext context, Direction facing) {
        if (context.getClickedFace().getAxis().isHorizontal()) {
            return false;
        }
        return !isClickedHalfTowardPlayer(context);
    }

    /**
     * True when the clicked point is on the half of a horizontal (top/bottom) face that is nearer the player.
     */
    private static boolean isClickedHalfTowardPlayer(BlockPlaceContext context) {
        Player player = context.getPlayer();
        if (player == null) {
            return true;
        }
        BlockPos clickedPos = context.getClickedPos();
        Vec3 hit = context.getClickLocation();
        Vec3 eye = player.getEyePosition();
        double dx = hit.x - clickedPos.getX();
        double dz = hit.z - clickedPos.getZ();
        double cx = clickedPos.getX() + 0.5;
        double cz = clickedPos.getZ() + 0.5;

        if (Math.abs(eye.x - cx) > Math.abs(eye.z - cz)) {
            return (dx > 0.5) == (eye.x > cx);
        }
        return (dz > 0.5) == (eye.z > cz);
    }

    public static Direction panelSide(BlockState state, DirectionProperty facingProperty,
                                      BooleanProperty outsetProperty) {
        Direction facing = state.getValue(facingProperty);
        // Inset: flush against the support block (opposite the clicked face). Outset: far side of the space.
        return state.getValue(outsetProperty) ? facing : facing.getOpposite();
    }
}
