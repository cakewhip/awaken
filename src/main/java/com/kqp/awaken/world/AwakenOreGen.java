package com.kqp.awaken.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.WorldChunk;

import java.util.BitSet;
import java.util.Random;

/**
 * Used to generate ore veins.
 */
public class AwakenOreGen {
    public static void generate(WorldChunk chunk, int maxY, int size, int count, Block block) {
        Random random = chunk.getWorld().random;
        BlockState oreBlock = block.getDefaultState();

        for (int c = 0; c < count; c++) {
            BlockPos blockPos = new BlockPos(random.nextInt(16), 4 + random.nextInt(maxY - 4), random.nextInt(16));

            float f = random.nextFloat() * 3.1415927F;
            float g = size / 8.0F;
            int i = MathHelper.ceil((size / 16.0F * 2.0F + 1.0F) / 2.0F);
            double d = (float) blockPos.getX() + MathHelper.sin(f) * g;
            double e = (float) blockPos.getX() - MathHelper.sin(f) * g;
            double h = (float) blockPos.getZ() + MathHelper.cos(f) * g;
            double j = (float) blockPos.getZ() - MathHelper.cos(f) * g;
            double l = blockPos.getY() + random.nextInt(3) - 2;
            double m = blockPos.getY() + random.nextInt(3) - 2;
            int n = blockPos.getX() - MathHelper.ceil(g) - i;
            int o = blockPos.getY() - 2 - i;
            int p = blockPos.getZ() - MathHelper.ceil(g) - i;
            int q = 2 * (MathHelper.ceil(g) + i);
            int r = 2 * (2 + i);

            for (int s = n; s <= n + q; ++s) {
                for (int t = p; t <= p + q; ++t) {
                    generateVeinPart(chunk, random, d, e, h, j, l, m, n, o, p, q, r, size, oreBlock);
                }
            }
        }
    }

    protected static void generateVeinPart(WorldChunk chunk, Random random, double startX, double endX, double startZ, double endZ, double startY, double endY, int x, int y, int z, int size, int i, int cSize, BlockState oreBlock) {
        BitSet bitSet = new BitSet(size * i * size);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        double[] ds = new double[cSize * 4];

        int m;
        double o;
        double p;
        double q;
        double r;
        for (m = 0; m < cSize; ++m) {
            float f = (float) m / (float) cSize;
            o = MathHelper.lerp(f, startX, endX);
            p = MathHelper.lerp(f, startY, endY);
            q = MathHelper.lerp(f, startZ, endZ);
            r = random.nextDouble() * (double) cSize / 16.0D;
            double l = ((double) (MathHelper.sin(3.1415927F * f) + 1.0F) * r + 1.0D) / 2.0D;
            ds[m * 4 + 0] = o;
            ds[m * 4 + 1] = p;
            ds[m * 4 + 2] = q;
            ds[m * 4 + 3] = l;
        }

        for (m = 0; m < cSize - 1; ++m) {
            if (ds[m * 4 + 3] > 0.0D) {
                for (int n = m + 1; n < cSize; ++n) {
                    if (ds[n * 4 + 3] > 0.0D) {
                        o = ds[m * 4 + 0] - ds[n * 4 + 0];
                        p = ds[m * 4 + 1] - ds[n * 4 + 1];
                        q = ds[m * 4 + 2] - ds[n * 4 + 2];
                        r = ds[m * 4 + 3] - ds[n * 4 + 3];
                        if (r * r > o * o + p * p + q * q) {
                            if (r > 0.0D) {
                                ds[n * 4 + 3] = -1.0D;
                            } else {
                                ds[m * 4 + 3] = -1.0D;
                            }
                        }
                    }
                }
            }
        }

        for (m = 0; m < size; ++m) {
            double t = ds[m * 4 + 3];
            if (t >= 0.0D) {
                double u = ds[m * 4 + 0];
                double v = ds[m * 4 + 1];
                double w = ds[m * 4 + 2];
                int aa = Math.max(MathHelper.floor(u - t), x);
                int ab = Math.max(MathHelper.floor(v - t), y);
                int ac = Math.max(MathHelper.floor(w - t), z);
                int ad = Math.max(MathHelper.floor(u + t), aa);
                int ae = Math.max(MathHelper.floor(v + t), ab);
                int af = Math.max(MathHelper.floor(w + t), ac);

                for (int ag = aa; ag <= ad; ++ag) {
                    double ah = ((double) ag + 0.5D - u) / t;
                    if (ah * ah < 1.0D) {
                        for (int ai = ab; ai <= ae; ++ai) {
                            double aj = ((double) ai + 0.5D - v) / t;
                            if (ah * ah + aj * aj < 1.0D) {
                                for (int ak = ac; ak <= af; ++ak) {
                                    double al = ((double) ak + 0.5D - w) / t;
                                    if (ah * ah + aj * aj + al * al < 1.0D) {
                                        int am = ag - x + (ai - y) * size + (ak - z) * size * i;
                                        if (!bitSet.get(am)) {
                                            bitSet.set(am);
                                            mutable.set(ag, ai, ak);
                                            if (chunk.getBlockState(mutable).getBlock() == Blocks.STONE) {
                                                chunk.setBlockState(mutable, oreBlock, false);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
