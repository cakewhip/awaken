package com.kqp.awaken.world.gen.chunk;

import com.kqp.awaken.init.AwakenBlocks;
import com.kqp.awaken.mixin.world.gen.chunk.SurfaceChunkGeneratorAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
        return 0;
    }

    @Override
    protected BlockState getBlockState(double density, int y) {
        BlockState blockState3;
        if (density > 0.0D) {
            if (y < 32) {
                blockState3 = AwakenBlocks.NULL_STONE.getDefaultState();
            } else {
                blockState3 = this.defaultBlock;
            }
        } else {
            blockState3 = Blocks.AIR.getDefaultState();
        }

        return blockState3;
    }
}
