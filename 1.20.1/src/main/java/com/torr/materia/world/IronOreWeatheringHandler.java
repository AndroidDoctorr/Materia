package com.torr.materia.world;

import com.torr.materia.ModBlocks;
import com.torr.materia.materia;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Runtime weathering of exposed vanilla iron ore into Materia surface iron ore.
 * Runs on the Forge tick bus via {@link TickEvent.ServerTickEvent} (overworld only).
 */
@Mod.EventBusSubscriber(modid = materia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class IronOreWeatheringHandler {

    private static final int TICK_INTERVAL = 10;

    private static final double RAIN_BASE = 0.012;
    private static final double SKY_DRY_BASE = 0.0018;
    private static final double CAVE_VENT_BASE = 0.0004;
    private static final double MAX_APPLY = 0.42;

    private IronOreWeatheringHandler() {}

    @SubscribeEvent
    public static void onServerTickEnd(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !event.side.isServer()) {
            return;
        }
        MinecraftServer minecraftServer = event.getServer();
        if (minecraftServer == null || minecraftServer.overworld().players().isEmpty()) {
            return;
        }
        ServerLevel overworld = minecraftServer.overworld();
        if (overworld.dimensionType().hasCeiling()) {
            return;
        }
        if (overworld.getGameTime() % TICK_INTERVAL != 0) {
            return;
        }

        int tickSpeed = Mth.clamp(overworld.getGameRules().getInt(GameRules.RULE_RANDOMTICKING), 0, 8192);
        int pulls = Mth.clamp(Mth.ceil(6.0 * Math.max(tickSpeed, 1)), 16, 4096);
        double rtsFactor = Math.max(1.0, tickSpeed / 3.0);

        RandomSource rnd = overworld.random;
        Block surfaceIron = ModBlocks.SURFACE_IRON_ORE.get();

        for (int n = 0; n < pulls; n++) {
            ServerPlayer player = overworld.players().get(rnd.nextInt(overworld.players().size()));

            int vd = Math.max(minecraftServer.getPlayerList().getViewDistance(), 3);
            int hr = Mth.clamp(vd * 8, 32, 128);

            BlockPos home = player.blockPosition();
            int bx = home.getX() + rnd.nextInt(hr * 2 + 1) - hr;
            int bz = home.getZ() + rnd.nextInt(hr * 2 + 1) - hr;

            int y = sampleY(overworld, bx, bz, rnd);
            BlockPos pos = new BlockPos(bx, y, bz);
            BlockPos above = pos.above();

            if (!overworld.hasChunkAt(pos)) {
                continue;
            }

            BlockState oreState = overworld.getBlockState(pos);
            if (!(oreState.is(Blocks.IRON_ORE) || oreState.is(Blocks.DEEPSLATE_IRON_ORE))) {
                continue;
            }

            if (!allowsMoistureFromAbove(overworld, above)) {
                continue;
            }

            boolean openSky = overworld.canSeeSky(blockingPosForSkyCheck(overworld, above));
            boolean ventilatedWall = horizontalAir(overworld, pos);
            if (!(openSky || ventilatedWall)) {
                continue;
            }

            boolean wet = openSky && isWetAtColumn(overworld, pos, above);

            double p;
            if (openSky && wet) {
                p = Mth.clamp(RAIN_BASE * rtsFactor, 0.0, MAX_APPLY);
            } else if (openSky) {
                p = Mth.clamp(SKY_DRY_BASE * rtsFactor, 0.0, MAX_APPLY * 0.35);
            } else {
                p = Mth.clamp(CAVE_VENT_BASE * rtsFactor, 0.0, MAX_APPLY * 0.12);
            }

            if (rnd.nextDouble() >= p) {
                continue;
            }

            overworld.setBlock(pos, surfaceIron.defaultBlockState(), Block.UPDATE_ALL);
        }
    }

    /**
     * Sky/light checks should ignore thin snow layers so ore under a dusting still weathers.
     */
    private static BlockPos blockingPosForSkyCheck(ServerLevel level, BlockPos airOrCoverPos) {
        BlockState st = level.getBlockState(airOrCoverPos);
        if (st.getBlock() instanceof SnowLayerBlock && st.getValue(SnowLayerBlock.LAYERS) <= 3) {
            return airOrCoverPos.above();
        }
        return airOrCoverPos;
    }

    /** Air, or thin-enough snow; blocks tall stacks and solid covers. */
    private static boolean allowsMoistureFromAbove(ServerLevel level, BlockPos aboveOre) {
        BlockState st = level.getBlockState(aboveOre);
        if (st.isAir()) {
            return true;
        }
        if (st.getBlock() instanceof SnowLayerBlock) {
            int layers = st.getValue(SnowLayerBlock.LAYERS);
            return layers <= 3;
        }
        return false;
    }

    private static boolean isWetAtColumn(ServerLevel level, BlockPos orePos, BlockPos aboveOre) {
        if (level.isRainingAt(aboveOre)) {
            return true;
        }
        if (level.isRaining() || level.isThundering()) {
            return level.getBiome(orePos).value().getPrecipitationAt(orePos) != Biome.Precipitation.NONE;
        }
        return false;
    }

    private static boolean horizontalAir(ServerLevel level, BlockPos pos) {
        for (Direction d : Direction.Plane.HORIZONTAL) {
            if (level.getBlockState(pos.relative(d)).isAir()) {
                return true;
            }
        }
        return false;
    }

    private static int sampleY(ServerLevel level, int bx, int bz, RandomSource rnd) {
        int minY = level.getMinBuildHeight();
        int maxExclusive = level.getMaxBuildHeight();
        int topSolid = level.getHeight(Heightmap.Types.WORLD_SURFACE, bx, bz) - 1;
        topSolid = Mth.clamp(topSolid, minY, maxExclusive - 1);

        float branch = rnd.nextFloat();
        if (branch < 0.58f) {
            int d = rnd.nextInt(10);
            return Mth.clamp(topSolid - d, minY, maxExclusive - 1);
        }
        if (branch < 0.88f) {
            int span = Math.max(1, topSolid - minY + 1);
            int dy = rnd.nextInt(Math.min(span, 48));
            return topSolid - dy;
        }
        int heightSpan = Math.max(1, maxExclusive - minY);
        return minY + rnd.nextInt(heightSpan);
    }
}
