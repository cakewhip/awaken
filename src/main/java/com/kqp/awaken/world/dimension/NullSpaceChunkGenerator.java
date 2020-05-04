package com.kqp.awaken.world.dimension;

import com.kqp.awaken.mixin.world.gen.chunk.SurfaceChunkGeneratorAccessor;
import net.minecraft.world.Heightmap;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.CavesChunkGenerator;
import net.minecraft.world.gen.chunk.CavesChunkGeneratorConfig;

public class NullSpaceChunkGenerator extends CavesChunkGenerator {
    public NullSpaceChunkGenerator(IWorld world, BiomeSource biomeSource, CavesChunkGeneratorConfig config) {
        super(world, biomeSource, config);

        ((SurfaceChunkGeneratorAccessor) this).setVerticalNoiseResolution(16);
    }

    @Override
    public int getMaxY() {
        return 255;
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmapType) {
        return 240;
    }

    @Override
    public int getSeaLevel() {
        return 12;
    }
}
