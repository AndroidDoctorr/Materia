package com.torr.materia.entity;

import com.torr.materia.ModEntities;
import com.torr.materia.ModItems;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;
import com.torr.materia.config.materiaCommonConfig;

public class CannonballEntity extends ThrowableItemProjectile {
    public CannonballEntity(EntityType<? extends CannonballEntity> type, Level level) {
        super(type, level);
    }

    public CannonballEntity(Level level) {
        super(ModEntities.CANNONBALL_PROJECTILE.get(), level);
    }

    public CannonballEntity(Level level, LivingEntity shooter) {
        super(ModEntities.CANNONBALL_PROJECTILE.get(), shooter, level);
    }

    @Override
    protected Item getDefaultItem() {
        // Fallback render item if no stack is set
        return ModItems.STONE_CANNONBALL.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity hit = result.getEntity();
        float dmg = getDamageForAmmo(getItem());
        hit.hurt(this.damageSources().thrown(this, this.getOwner()), dmg);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        Level level = level();
        if (level.isClientSide) return;

        BlockPos pos = result.getBlockPos();
        BlockState state = level.getBlockState(pos);
        handleBlockImpact(level, pos, state, getItem());
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!level().isClientSide) {
            discard();
        }
    }

    private static float getDamageForAmmo(ItemStack ammo) {
        if (ammo.isEmpty()) return 10.0F;
        if (ammo.is(ModItems.IRON_CANNONBALL.get())) return 40.0F;
        if (ammo.is(ModItems.STONE_CANNONBALL.get())) return 22.0F;
        // Fallback: make other "ammo-like" items still strong
        if (ammo.is(Items.TNT)) return 0.0F;
        if (ammo.is(ModItems.CANISTER_SHOT.get())) return 0.0F;
        return 14.0F;
    }

    private static void handleBlockImpact(Level level, BlockPos pos, BlockState state, ItemStack ammo) {
        if (!materiaCommonConfig.CANNONBALL_BLOCK_EFFECTS_ENABLED.get()) return;

        Block block = state.getBlock();
        boolean iron = !ammo.isEmpty() && ammo.is(ModItems.IRON_CANNONBALL.get());

        // Stone -> cobblestone
        if (materiaCommonConfig.CANNONBALL_EFFECT_STONE_TO_COBBLE.get() && block == Blocks.STONE) {
            level.setBlock(pos, Blocks.COBBLESTONE.defaultBlockState(), 3);
            return;
        }

        // Iron cannonballs can smash more base stones (e.g. granite) into cobblestone
        if (materiaCommonConfig.CANNONBALL_EFFECT_STONE_TO_COBBLE.get()
                && materiaCommonConfig.IRON_CANNONBALL_EXTRA_STONE_SMASHING.get()
                && iron
                && block.defaultBlockState().is(BlockTags.BASE_STONE_OVERWORLD)) {
            level.setBlock(pos, Blocks.COBBLESTONE.defaultBlockState(), 3);
            return;
        }

        // Iron cannonballs smash stone bricks immediately (no "cracked" intermediate)
        if (materiaCommonConfig.CANNONBALL_EFFECT_STONE_TO_COBBLE.get() && iron && block == Blocks.STONE_BRICKS) {
            level.setBlock(pos, Blocks.COBBLESTONE.defaultBlockState(), 3);
            return;
        }

        // Cracked stone bricks -> cobblestone
        if (materiaCommonConfig.CANNONBALL_EFFECT_STONE_TO_COBBLE.get() && block == Blocks.CRACKED_STONE_BRICKS) {
            level.setBlock(pos, Blocks.COBBLESTONE.defaultBlockState(), 3);
            return;
        }

        // Cobblestone -> 4 pebbles (no cobble drop)
        if (materiaCommonConfig.CANNONBALL_EFFECT_COBBLE_TO_PEBBLES.get() && block == Blocks.COBBLESTONE) {
            level.levelEvent(2001, pos, Block.getId(state));
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            Block.popResource(level, pos, new ItemStack(ModItems.PEBBLE.get(), 4));
            return;
        }

        // Sand (and red sand) breaks
        if (materiaCommonConfig.CANNONBALL_EFFECT_SAND_BREAKS.get()
                && (block == Blocks.SAND || block == Blocks.RED_SAND)) {
            level.levelEvent(2001, pos, Block.getId(state));
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            Block.popResource(level, pos, new ItemStack(block.asItem(), 1));
            return;
        }

        // Generic: if a "cracked_" variant exists, crack it (e.g. stone_bricks -> cracked_stone_bricks)
        if (!materiaCommonConfig.CANNONBALL_EFFECT_CRACK_VARIANTS.get()) return;

        ResourceLocation key = ForgeRegistries.BLOCKS.getKey(block);
        if (key == null) return;

        String path = key.getPath();
        if (path.startsWith("cracked_")) return;

        ResourceLocation crackedKey = ResourceLocation.fromNamespaceAndPath(key.getNamespace(), "cracked_" + path);
        Block crackedBlock = ForgeRegistries.BLOCKS.getValue(crackedKey);
        if (crackedBlock == null || crackedBlock == Blocks.AIR) return;

        BlockState crackedState = copyCommonProperties(state, crackedBlock.defaultBlockState());
        level.setBlock(pos, crackedState, 3);
    }

    private static BlockState copyCommonProperties(BlockState from, BlockState to) {
        for (Property<?> property : from.getProperties()) {
            if (to.hasProperty(property)) {
                to = copyProperty(from, to, property);
            }
        }
        return to;
    }

    private static <T extends Comparable<T>> BlockState copyProperty(BlockState from, BlockState to, Property<T> property) {
        return to.setValue(property, from.getValue(property));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return super.getAddEntityPacket(serverEntity);
    }
}

