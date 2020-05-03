package com.kqp.awaken.world.dimension;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;

public class NullChunkGenerator<T extends ChunkGeneratorConfig> extends ChunkGenerator<T> {
    private static final BlockState DEFAULT_BLOCK = Blocks.STONE.getDefaultState();

    protected final ChunkRandom random;

    public NullChunkGenerator(IWorld world, BiomeSource biomeSource, T config) {
        super(world, biomeSource, config);
        this.random = new ChunkRandom(this.seed);
    }

    @Override
    public void buildSurface(ChunkRegion region, Chunk chunk) {
        chunk.setBlockState(BlockPos.ORIGIN, Blocks.DIAMOND_BLOCK.getDefaultState(), false);
    }

    @Override
    public int getSpawnHeight() {
        return 128;
    }

    @Override
    public void populateNoise(IWorld world, StructureAccessor structureAccessor, Chunk chunk) {
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmapType) {
        return 64;
    }

    @Override
    public BlockView getColumnSample(int x, int z) {
        return new VerticalBlockSample(new BlockState[] { DEFAULT_BLOCK });
    }
}
