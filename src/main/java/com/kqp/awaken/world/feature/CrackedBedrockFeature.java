package com.kqp.awaken.world.feature;

import com.kqp.awaken.init.AwakenBlocks;
import com.kqp.awaken.util.SphereUtil;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;
import java.util.function.Function;

public class CrackedBedrockFeature extends Feature<DefaultFeatureConfig> {
    public CrackedBedrockFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> configDeserializer) {
        super(configDeserializer);
    }

    @Override
    public boolean generate(IWorld world, StructureAccessor accessor, ChunkGenerator<? extends ChunkGeneratorConfig> generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
        if (random.nextFloat() < 0.05F) {

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    for (int l = 0; l < 8; l++) {
                        BlockPos queryPos = pos.add(i, l, j);

                        if (world.getBlockState(queryPos).getBlock() == Blocks.BEDROCK) {
                            world.setBlockState(queryPos, AwakenBlocks.CRACKED_BEDROCK.getDefaultState(), 3);
                        }
                    }
                }
            }

            BlockPos cent = pos.add(0, 128, 0);
            for (BlockPos offset : SphereUtil.getSphereBlockOffsets(5)) {
                world.setBlockState(cent.add(offset), Blocks.DIAMOND_BLOCK.getDefaultState(), 3);
            }

            for (int i = 0; i < 128; i++) {
                world.setBlockState(pos.add(0, i, 0), Blocks.AIR.getDefaultState(), 3);
            }

            return true;
        }

        return false;
    }
}
