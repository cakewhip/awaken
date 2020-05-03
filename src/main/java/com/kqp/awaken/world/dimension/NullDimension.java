package com.kqp.awaken.world.dimension;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeSourceType;
import net.minecraft.world.biome.source.FixedBiomeSourceConfig;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;

public class NullDimension extends Dimension {
    private static final Vec3d FOG_COLOR = new Vec3d(1F, 1F, 1F);

    public NullDimension(World world, DimensionType type) {
        super(world, type, 0F);
    }

    @Override
    public ChunkGenerator<?> createChunkGenerator() {
        FlatChunkGeneratorConfig generatorConfig = FlatChunkGeneratorConfig.getDefaultConfig();
        // The biome everywhere will be jungle
        FixedBiomeSourceConfig biomeConfig = BiomeSourceType.FIXED.getConfig(world.getSeed()).setBiome(Biomes.JUNGLE);
        return ChunkGeneratorType.FLAT.create(world, BiomeSourceType.FIXED.applyConfig(biomeConfig), generatorConfig);
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
        return AwakenDimensions.NULL;
    }
}
