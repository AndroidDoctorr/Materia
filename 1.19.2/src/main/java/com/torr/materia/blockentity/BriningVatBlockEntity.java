package com.torr.materia.blockentity;

import com.torr.materia.ModBlockEntities;
import com.torr.materia.ModItems;
import com.torr.materia.block.BriningVatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class BriningVatBlockEntity extends BlockEntity {
    // 4 salt per bucketful of water
    private static final int SALT_PER_WATER = 4;

    // Default evaporation time: 6000 ticks (~5 minutes)
    private static final int EVAPORATION_TICKS = 6000;

    private float evaporationProgress = 0;
    private int storedSalt = 0;

    public BriningVatBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BRINING_VAT_BLOCK_ENTITY.get(), pos, state);
    }

    public void fillWithWater() {
        evaporationProgress = 0;
        storedSalt = 0;
        setChanged();
    }

    public boolean hasSalt() {
        return storedSalt > 0;
    }

    public ItemStack takeAllSalt() {
        if (storedSalt <= 0) return ItemStack.EMPTY;
        int taken = storedSalt;
        storedSalt = 0;
        setChanged();
        return new ItemStack(ModItems.SALT.get(), taken);
    }

    public void drops() {
        if (this.level == null) return;
        if (storedSalt <= 0) return;

        SimpleContainer inventory = new SimpleContainer(1);
        inventory.setItem(0, new ItemStack(ModItems.SALT.get(), storedSalt));
        Containers.dropContents(this.level, this.worldPosition, inventory);
        storedSalt = 0;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BriningVatBlockEntity entity) {
        if (level.isClientSide()) return;

        if (!state.hasProperty(BriningVatBlock.STAGE)) return;

        var stage = state.getValue(BriningVatBlock.STAGE);

        if (stage != BriningVatBlock.Stage.WATER) {
            entity.evaporationProgress = 0;
            return;
        }

        entity.evaporationProgress += getEvaporationRate(level, pos);
        if (entity.evaporationProgress >= EVAPORATION_TICKS) {
            entity.evaporationProgress = 0;
            entity.storedSalt = SALT_PER_WATER;
            level.setBlock(pos, state.setValue(BriningVatBlock.STAGE, BriningVatBlock.Stage.SALT), 3);
            entity.setChanged();
        }
    }

    /**
     * Evaporation speed multiplier in "ticks of progress per tick".
     * 1.0 means EVAPORATION_TICKS behaves like a fixed timer.
     *
     * Faster: hot + dry + day
     * Slower: cold + wet + night + rain
     */
    private static float getEvaporationRate(Level level, BlockPos pos) {
        Biome biome = level.getBiome(pos).value();

        float temp = biome.getBaseTemperature();
        float downfall = biome.getDownfall();

        float heat = Mth.clamp((temp - 0.2f) / 1.5f, 0f, 1f);
        float dryness = Mth.clamp(1f - downfall, 0f, 1f);

        // 0.35..2.0 depending on heat+dryness
        float biomeFactor = 0.35f + 1.65f * heat * (0.35f + 0.65f * dryness);

        // Night is slower
        float dayFactor = level.isDay() ? 1.0f : 0.35f;

        // Rain slows evaporation even more
        float rainFactor = level.isRainingAt(pos.above()) ? 0.6f : 1.0f;

        return biomeFactor * dayFactor * rainFactor;
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag) {
        tag.putFloat("brining_vat.evaporation", evaporationProgress);
        tag.putInt("brining_vat.salt", storedSalt);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        evaporationProgress = tag.getFloat("brining_vat.evaporation");
        storedSalt = tag.getInt("brining_vat.salt");
    }
}

