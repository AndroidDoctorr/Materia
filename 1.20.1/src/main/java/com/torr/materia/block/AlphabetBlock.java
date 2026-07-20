package com.torr.materia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Cube block with six glyphs (A–F on model up/down/north/south/east/west).
 * <p>
 * {@link #FACING} is the outward direction (away from the surface you clicked).
 * {@link #ROTATION} spins 90° steps around that axis so side letters stay upright.
 * {@link #TILT} chooses which letter sits on the outward face on walls/ceiling:
 * <ul>
 *   <li>0 — standard (C/D/E/F on walls, A/B on floor/ceiling)</li>
 *   <li>1 — A on the outward face (sneak while placing, or right-click to cycle)</li>
 *   <li>2 — B on the outward face (right-click to cycle)</li>
 * </ul>
 * Right-click with Mosaic Stylus cycles tilt (standard → top letter on face → bottom letter);
 * shift + click rotates (flip on walls). Sneak while placing puts the top letter on the outward face.
 */
public class AlphabetBlock extends Block {
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.values());
    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0, 3);
    public static final IntegerProperty TILT = IntegerProperty.create("tilt", 0, 2);

    public static final int TILT_STANDARD = 0;
    public static final int TILT_CAP_A = 1;
    public static final int TILT_CAP_B = 2;

    public AlphabetBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(ROTATION, 0)
                .setValue(TILT, TILT_STANDARD));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ROTATION, TILT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clicked = context.getClickedFace();
        Direction facing = clicked.getOpposite();
        int rotation = clicked.getAxis().isVertical()
                ? rotationFromYaw(context.getHorizontalDirection().toYRot())
                : wallRotation(facing, context.getHorizontalDirection());
        int tilt = tiltForPlacement(context);
        return defaultBlockState()
                .setValue(FACING, facing)
                .setValue(ROTATION, rotation)
                .setValue(TILT, tilt);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        // Character-block tilt/rotation is handled by MosaicStylusItem.useOn (works while sneaking too).
        return InteractionResult.PASS;
    }

    public static int nextTilt(BlockState state) {
        return (state.getValue(TILT) + 1) % 3;
    }

    public static int nextRotation(BlockState state) {
        int rotation = state.getValue(ROTATION);
        if (state.getValue(FACING).getAxis().isHorizontal()) {
            // Wall blocks only support upright vs upside-down (0/2) with model x/y rotations.
            return rotation < 2 ? rotation + 2 : rotation - 2;
        }
        return (rotation + 1) & 3;
    }

    private static int tiltForPlacement(BlockPlaceContext context) {
        if (context.isSecondaryUseActive()) {
            return TILT_CAP_A;
        }
        return TILT_STANDARD;
    }

    private static int rotationFromYaw(float yaw) {
        return Math.floorMod(Mth.floor((yaw + 180.0F) * 4.0F / 360.0F + 0.5F), 4);
    }

    private static int wallRotation(Direction outward, Direction playerHorizontal) {
        Direction spin = switch (outward) {
            case NORTH -> Direction.SOUTH;
            case SOUTH -> Direction.NORTH;
            case WEST -> Direction.EAST;
            case EAST -> Direction.WEST;
            default -> playerHorizontal;
        };
        int base = rotationFromYaw(spin.toYRot());
        return Math.floorMod(base - rotationFromYaw(outward.toYRot()), 4);
    }
}
