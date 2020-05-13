package com.kqp.awaken.world.gen.decorator;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.decorator.CountDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class NullSpikeDecorator extends Decorator<CountDecoratorConfig> {
    public NullSpikeDecorator(Function<Dynamic<?>, ? extends CountDecoratorConfig> function) {
        super(function);
    }

    public Stream<BlockPos> getPositions(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> chunkGenerator, Random random, CountDecoratorConfig countDecoratorConfig, BlockPos blockPos) {
        int i = random.nextInt(countDecoratorConfig.count);

        return IntStream.range(0, i).mapToObj((ix) -> {
            int x = random.nextInt(16) + blockPos.getX();
            int z = random.nextInt(16) + blockPos.getZ();

            return new BlockPos(x, 32, z);
        });
    }
}
