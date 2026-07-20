package com.torr.materia.blockentity;

import com.torr.materia.ModBlockEntities;
import com.torr.materia.mosaic.MosaicFaceData;
import com.torr.materia.mosaic.MosaicHitUtil;
import com.torr.materia.mosaic.MosaicPalette;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MosaicBlockEntity extends BlockEntity {
    private final MosaicFaceData canvas = new MosaicFaceData();

    public MosaicBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MOSAIC_BLOCK_ENTITY.get(), pos, state);
    }

    public MosaicFaceData canvas() {
        return canvas;
    }

    public void cyclePixel(MosaicHitUtil.FacePixel hit, boolean reverse) {
        byte current = canvas.get(hit.x(), hit.y());
        byte next = (byte) (reverse ? MosaicPalette.cycleBackward(current & 0xFF)
                : MosaicPalette.cycleForward(current & 0xFF));
        canvas.set(hit.x(), hit.y(), next);
        setChanged();
    }

    public boolean hasAnyPaint() {
        for (int i = 0; i < MosaicFaceData.SIZE * MosaicFaceData.SIZE; i++) {
            if (canvas.copyPixels()[i] != 0) {
                return true;
            }
        }
        return false;
    }

    public void clearCanvas() {
        canvas.clear();
        setChanged();
    }

    public void copyCanvasFrom(MosaicBlockEntity other) {
        canvas.copyFrom(other.canvas);
        setChanged();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public void onDataPacket(net.minecraft.network.Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(pkt.getTag());
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        canvas.write(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        canvas.read(tag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
