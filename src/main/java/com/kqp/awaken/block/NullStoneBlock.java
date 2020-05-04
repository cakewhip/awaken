package com.kqp.awaken.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class NullStoneBlock extends Block {
    public NullStoneBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextFloat() < 0.1F) {
            for (int i = 0; i < 3; ++i) {
                int j = random.nextInt(2) * 2 - 1;
                int k = random.nextInt(2) * 2 - 1;
                double d = pos.getX() + 0.5D + 0.25D * j;
                double e = pos.getY() + random.nextFloat();
                double f = pos.getZ() + 0.5D + 0.25D * k;
                double g = random.nextFloat() * j * 0.125D;
                double h = (random.nextFloat() - 0.5D) * 0.125D;
                double l = random.nextFloat() * k * 0.125D;

                world.addParticle(ParticleTypes.ASH, d, e, f, g, h, l);
            }
        }
    }
}
