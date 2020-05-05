package com.kqp.awaken.world.biome;

import com.kqp.awaken.init.AwakenBlocks;
import com.kqp.awaken.util.GenUtil;
import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;
import java.util.function.Function;

public class NullSpikeFeature extends Feature<NullSpikeFeatureConfig> {
    public NullSpikeFeature(Function<Dynamic<?>, ? extends NullSpikeFeatureConfig> function) {
        super(function);
    }

    public boolean generate(IWorld world, StructureAccessor structureAccessor, ChunkGenerator<? extends ChunkGeneratorConfig> chunkGenerator, Random random, BlockPos blockPos, NullSpikeFeatureConfig NullSpikeFeatureConfig) {
        int startRadius = 9 + random.nextInt(3) * 2;
        int radius = startRadius;
        BlockPos cent = blockPos;

        while (radius > 1) {
            for (BlockPos offset : GenUtil.getSphereBlockOffsets(radius)) {
                world.setBlockState(cent.add(offset), AwakenBlocks.NULL_STONE.getDefaultState(), 3);
            }

            double branchPerc = 1D - (double) radius / startRadius;

            Vec3d bVec = getBranchVector(random).multiply(radius);

            int branches = 0;

            while (random.nextFloat() < branchPerc && branches < 4) {
                cent = add(cent, bVec);

                for (BlockPos offset : GenUtil.getSphereBlockOffsets(radius)) {
                    world.setBlockState(cent.add(offset), AwakenBlocks.NULL_STONE.getDefaultState(), 3);
                }

                branches++;
                branchPerc *= 0.9D;
                bVec = bVec.add(getBranchVector(random)).normalize().add(new Vec3d(0D, (double) radius / startRadius * 4D, 0D)).normalize().multiply(radius);
            }

            bVec = getBranchVector(random).multiply(radius);
            cent = add(cent, bVec.multiply(0.75D));

            radius -= 2;
        }

        return true;
    }

    private static Vec3d getBranchVector(Random random) {
        return new Vec3d(
                (random.nextFloat() - random.nextFloat()) * 2,
                1.5D,
                (random.nextFloat() - random.nextFloat()) * 2
        ).normalize();
    }

    private static BlockPos add(BlockPos pos, Vec3d vec) {
        return pos.add(Math.round(vec.x), Math.round(vec.y), Math.round(vec.z));
    }
}
