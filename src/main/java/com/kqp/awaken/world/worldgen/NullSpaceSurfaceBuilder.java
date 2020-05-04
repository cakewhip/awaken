package com.kqp.awaken.world.worldgen;

import com.kqp.awaken.init.AwakenBlocks;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

import java.util.Random;
import java.util.function.Function;

public class NullSpaceSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {
    public NullSpaceSurfaceBuilder(Function<Dynamic<?>, ? extends TernarySurfaceConfig> factory) {
        super(factory);
    }

    @Override
    public void generate(Random random, Chunk chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, TernarySurfaceConfig surfaceBlocks) {
        BlockPos pos = new BlockPos(x & 15, height, z & 15);
        chunk.setBlockState(pos, AwakenBlocks.CRACKED_BEDROCK.getDefaultState(), false);
    }
}
