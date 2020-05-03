package com.kqp.awaken.world.dimension;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.CavesChunkGenerator;
import net.minecraft.world.gen.chunk.CavesChunkGeneratorConfig;

public class NullSpaceChunkGenerator extends CavesChunkGenerator {
    private static final BlockState DEFAULT_BLOCK = Blocks.STONE.getDefaultState();

    public NullSpaceChunkGenerator(IWorld world, BiomeSource biomeSource, CavesChunkGeneratorConfig config) {
        super(world, biomeSource, config);
    }

    @Override
    public int getMaxY() {
        return 255;
    }

    @Override
    public int getSeaLevel() {
        return 255;
    }
}
