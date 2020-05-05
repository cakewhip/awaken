package com.kqp.awaken.world.gen.decorator;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class VoidTowerDecorator extends Decorator<ChanceDecoratorConfig> {
    public VoidTowerDecorator(Function<Dynamic<?>, ? extends ChanceDecoratorConfig> function) {
        super(function);
    }

    public Stream<BlockPos> getPositions(IWorld iWorld, ChunkGenerator<? extends ChunkGeneratorConfig> chunkGenerator, Random random, ChanceDecoratorConfig chanceDecoratorConfig, BlockPos blockPos) {
        int i = random.nextFloat() < 0.1F ? chanceDecoratorConfig.chance : 0;

        return IntStream.range(0, i).mapToObj((ix) -> {
            int j = random.nextInt(16) + blockPos.getX();
            int k = random.nextInt(16) + blockPos.getZ();
            int l = 0;

            return new BlockPos(j, l, k);
        });
    }
}
