package com.kqp.awaken.world.biome;

import com.google.common.collect.ImmutableSet;
import com.kqp.awaken.init.AwakenBiomes;
import com.kqp.awaken.init.AwakenBlocks;
import com.kqp.awaken.init.AwakenEntities;
import com.kqp.awaken.world.biome.NullSpaceBiome.NullSpaceCaveCarver;
import com.kqp.awaken.world.biome.NullSpaceBiome.NullSpaceOreFeature;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CaveCarver;
import net.minecraft.world.gen.decorator.CountDepthDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Random;
import java.util.function.Function;

public class NullSpaceBiome extends Biome {
    private static final NullSpaceOreFeature ORE = new NullSpaceOreFeature(OreFeatureConfig::deserialize);

    public NullSpaceBiome() {
        super(new Biome.Settings()
                .configureSurfaceBuilder(AwakenBiomes.NULL_SPACE_SURFACE_BUILDER, AwakenBiomes.NULL_SPACE_CONFIG)
                .precipitation(Precipitation.NONE)
                .category(Category.NONE)
                .depth(0.1F)
                .scale(0.2F)
                .temperature(4F)
                .downfall(0)
                .effects(new BiomeEffects.Builder()
                        .fogColor(0x000000)
                        .waterColor(0x101010)
                        .waterFogColor(0x101010)
                        .build())
                .parent(null)
        );

        addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(AwakenEntities.VOID_GHOST, 95, 4, 6));

        addCarver(GenerationStep.Carver.AIR, Biome.configureCarver(new NullSpaceCaveCarver(ProbabilityConfig::deserialize, 256), new ProbabilityConfig(0.6F)));
        addCarver(GenerationStep.Carver.AIR, Biome.configureCarver(Carver.CANYON, new ProbabilityConfig(0.02F)));

        addFeature(GenerationStep.Feature.UNDERGROUND_ORES, ORE.configure(new OreFeatureConfig(null, AwakenBlocks.ANCIENT_COAL_ORE.getDefaultState(), 17)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(160, 0, 0, 256))));
        addFeature(GenerationStep.Feature.UNDERGROUND_ORES, ORE.configure(new OreFeatureConfig(null, AwakenBlocks.ANCIENT_IRON_ORE.getDefaultState(), 9)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(160, 0, 0, 256))));
        addFeature(GenerationStep.Feature.UNDERGROUND_ORES, ORE.configure(new OreFeatureConfig(null, AwakenBlocks.ANCIENT_GOLD_ORE.getDefaultState(), 9)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(16, 0, 0, 256))));
        addFeature(GenerationStep.Feature.UNDERGROUND_ORES, ORE.configure(new OreFeatureConfig(null, AwakenBlocks.ANCIENT_REDSTONE_ORE.getDefaultState(), 8)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(64, 0, 0, 256))));
        addFeature(GenerationStep.Feature.UNDERGROUND_ORES, ORE.configure(new OreFeatureConfig(null, AwakenBlocks.ANCIENT_DIAMOND_ORE.getDefaultState(), 8)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(8, 0, 0, 256))));
        addFeature(GenerationStep.Feature.UNDERGROUND_ORES, ORE.configure(new OreFeatureConfig(null, AwakenBlocks.ANCIENT_EMERALD_ORE.getDefaultState(), 1)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(8, 0, 0, 256))));
        addFeature(GenerationStep.Feature.UNDERGROUND_ORES, ORE.configure(new OreFeatureConfig(null, AwakenBlocks.ANCIENT_LAPIS_ORE.getDefaultState(), 7)).createDecoratedFeature(Decorator.COUNT_DEPTH_AVERAGE.configure(new CountDepthDecoratorConfig(8, 16, 256))));
        addFeature(GenerationStep.Feature.UNDERGROUND_ORES, ORE.configure(new OreFeatureConfig(null, AwakenBlocks.ANCIENT_SALVIUM_ORE.getDefaultState(), 9)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(16, 0, 0, 256))));
        addFeature(GenerationStep.Feature.UNDERGROUND_ORES, ORE.configure(new OreFeatureConfig(null, AwakenBlocks.ANCIENT_VALERIUM_ORE.getDefaultState(), 9)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(16, 0, 0, 256))));
    }

    class NullSpaceCaveCarver extends CaveCarver {
        public NullSpaceCaveCarver(Function<Dynamic<?>, ? extends ProbabilityConfig> function, int i) {
            super(function, i);

            HashSet<Block> blocks = new HashSet();
            blocks.addAll(this.alwaysCarvableBlocks);
            blocks.add(AwakenBlocks.ANCIENT_STONE);
            blocks.add(AwakenBlocks.NULL_STONE);

            this.alwaysCarvableBlocks = ImmutableSet.copyOf(blocks);
        }
    }

    /**
     * Overriding generate method to include ancient stone blocks.
     */
    static class NullSpaceOreFeature extends OreFeature {
        public NullSpaceOreFeature(Function<Dynamic<?>, ? extends OreFeatureConfig> function) {
            super(function);
        }

        @Override
        protected boolean generateVeinPart(IWorld world, Random random, OreFeatureConfig config, double startX, double endX, double startZ, double endZ, double startY, double endY, int x, int y, int z, int size, int i) {
            int j = 0;
            BitSet bitSet = new BitSet(size * i * size);
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            double[] ds = new double[config.size * 4];

            int m;
            double o;
            double p;
            double q;
            double r;
            for (m = 0; m < config.size; ++m) {
                float f = (float) m / (float) config.size;
                o = MathHelper.lerp(f, startX, endX);
                p = MathHelper.lerp(f, startY, endY);
                q = MathHelper.lerp(f, startZ, endZ);
                r = random.nextDouble() * (double) config.size / 16.0D;
                double l = ((double) (MathHelper.sin(3.1415927F * f) + 1.0F) * r + 1.0D) / 2.0D;
                ds[m * 4 + 0] = o;
                ds[m * 4 + 1] = p;
                ds[m * 4 + 2] = q;
                ds[m * 4 + 3] = l;
            }

            for (m = 0; m < config.size - 1; ++m) {
                if (ds[m * 4 + 3] > 0.0D) {
                    for (int n = m + 1; n < config.size; ++n) {
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

            for (m = 0; m < config.size; ++m) {
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
                                                if (world.getBlockState(mutable).getBlock() == AwakenBlocks.ANCIENT_STONE) {
                                                    world.setBlockState(mutable, config.state, 2);
                                                    ++j;
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

            return j > 0;
        }
    }
}
