package com.kqp.awaken.world.dimension;

import com.kqp.awaken.init.AwakenBiomes;
import com.kqp.awaken.init.AwakenBlocks;
import com.kqp.awaken.init.AwakenDimensions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSourceType;
import net.minecraft.world.biome.source.FixedBiomeSourceConfig;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.CavesChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class NullSpaceDimension extends Dimension {
    private static final Vec3d FOG_COLOR = new Vec3d(0F, 0F, 0F);

    public NullSpaceDimension(World world, DimensionType type) {
        super(world, type, 0.03F);
    }

    @Override
    public ChunkGenerator<?> createChunkGenerator() {
        CavesChunkGeneratorConfig config = AwakenDimensions.NULL_SPACE_CHUNK_GENERATOR.createConfig();
        config.setDefaultBlock(AwakenBlocks.ANCIENT_STONE.getDefaultState());
        config.setDefaultFluid(Blocks.AIR.getDefaultState());

        FixedBiomeSourceConfig biomeConfig = BiomeSourceType.FIXED.getConfig(world.getSeed()).setBiome(AwakenBiomes.NULL_SPACE);

        return AwakenDimensions.NULL_SPACE_CHUNK_GENERATOR.create(world, BiomeSourceType.FIXED.applyConfig(biomeConfig), config);
    }

    @Override
    public BlockPos getSpawningBlockInChunk(ChunkPos chunkPos, boolean checkMobSpawnValidity) {
        return null;
    }

    @Override
    public BlockPos getTopSpawningBlockPosition(int x, int z, boolean checkMobSpawnValidity) {
        return null;
    }

    @Override
    public float getSkyAngle(long timeOfDay, float tickDelta) {
        return 0;
    }

    @Override
    public boolean hasVisibleSky() {
        return false;
    }

    @Override
    public Vec3d modifyFogColor(Vec3d vec3d, float tickDelta) {
        return FOG_COLOR;
    }

    @Override
    public boolean canPlayersSleep() {
        return false;
    }

    @Override
    public boolean isFogThick(int x, int z) {
        return true;
    }

    @Override
    public DimensionType getType() {
        return AwakenDimensions.NULL_SPACE;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public float[] getBackgroundColor(float skyAngle, float tickDelta) {
        return null;
    }
}
