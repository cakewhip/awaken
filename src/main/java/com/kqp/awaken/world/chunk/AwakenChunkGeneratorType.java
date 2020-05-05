package com.kqp.awaken.world.chunk;

import net.minecraft.world.IWorld;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;

import java.util.function.Supplier;

public class AwakenChunkGeneratorType<C extends ChunkGeneratorConfig, T extends ChunkGenerator<C>> extends ChunkGeneratorType<C, T> {
    private final GeneratorFactory<C, T> factory;

    public AwakenChunkGeneratorType(GeneratorFactory<C, T> factory, boolean buffetScreenOption, Supplier<C> configSupplier) {
        super(null, buffetScreenOption, configSupplier);

        this.factory = factory;
    }

    @Override
    public T create(IWorld world, BiomeSource biomeSource, C config) {
        return factory.create(world, biomeSource, config);
    }

    @FunctionalInterface
    public interface GeneratorFactory<C extends ChunkGeneratorConfig, T extends ChunkGenerator<C>> {
        T create(IWorld world, BiomeSource biomeSource, C config);
    }
}
