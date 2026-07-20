package com.torr.materia.blockentity;

import com.torr.materia.ModBlockEntities;
import com.torr.materia.block.PlanterBlock;
import com.torr.materia.block.PottedPlantRules;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class PlanterBlockEntity extends BlockEntity {
    private Block leftPlant = Blocks.AIR;
    private Block rightPlant = Blocks.AIR;

    public PlanterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PLANTER_BLOCK_ENTITY.get(), pos, state);
    }

    public Block getLeftPlant() {
        return leftPlant;
    }

    public Block getRightPlant() {
        return rightPlant;
    }

    public void setLeftPlant(Block plant) {
        this.leftPlant = plant;
        setChanged();
    }

    public void setRightPlant(Block plant) {
        this.rightPlant = plant;
        setChanged();
    }

    public boolean isLeftSlot(BlockPos hitPos, net.minecraft.world.phys.Vec3 hit) {
        BlockState state = getBlockState();
        Direction facing = state.getBlock() instanceof PlanterBlock
                ? state.getValue(PlanterBlock.FACING)
                : Direction.NORTH;
        float localX = (float) (hit.x - hitPos.getX());
        float localZ = (float) (hit.z - hitPos.getZ());
        float[] unmapped = unmapPlanterPoint(facing, localX, localZ);
        return unmapped[0] < 0.5F;
    }

    private static float[] unmapPlanterPoint(Direction facing, float x, float z) {
        return switch (facing) {
            case SOUTH -> new float[]{1f - x, 1f - z};
            case EAST -> new float[]{z, 1f - x};
            case WEST -> new float[]{1f - z, x};
            default -> new float[]{x, z};
        };
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        writePlant(tag, "LeftPlant", leftPlant);
        writePlant(tag, "RightPlant", rightPlant);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        leftPlant = readPlant(tag, "LeftPlant");
        rightPlant = readPlant(tag, "RightPlant");
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    private static void writePlant(CompoundTag tag, String key, Block block) {
        if (block != Blocks.AIR) {
            tag.putString(key, ForgeRegistries.BLOCKS.getKey(block).toString());
        } else {
            tag.remove(key);
        }
    }

    private static Block readPlant(CompoundTag tag, String key) {
        if (!tag.contains(key)) {
            return Blocks.AIR;
        }
        ResourceLocation id = new ResourceLocation(tag.getString(key));
        Block block = ForgeRegistries.BLOCKS.getValue(id);
        return block != null ? block : Blocks.AIR;
    }

    public static boolean accepts(Block block) {
        return PottedPlantRules.canPlantInPlanter(block) && !PottedPlantRules.isTallPlant(block);
    }
}
