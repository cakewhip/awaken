package com.kqp.awaken.world.biome;

import com.google.common.collect.ImmutableSet;
import com.kqp.awaken.init.AwakenBiomes;
import com.kqp.awaken.init.AwakenBlocks;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CaveCarver;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.CountDepthDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;

import java.util.HashSet;
import java.util.function.Function;

public class NullSpaceBiome extends Biome {
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

        addCarver(GenerationStep.Carver.AIR, Biome.configureCarver(new NullSpaceCaveCarver(ProbabilityConfig::deserialize, 256), new ProbabilityConfig(0.6F)));
        addCarver(GenerationStep.Carver.AIR, Biome.configureCarver(Carver.CANYON, new ProbabilityConfig(0.02F)));

        addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, Blocks.COAL_ORE.getDefaultState(), 17)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(160, 0, 0, 256))));
        addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, Blocks.IRON_ORE.getDefaultState(), 9)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(160, 0, 0, 256))));
        addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, Blocks.GOLD_ORE.getDefaultState(), 9)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(16, 0, 0, 256))));
        addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, Blocks.REDSTONE_ORE.getDefaultState(), 8)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(64, 0, 0, 256))));
        addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, Blocks.DIAMOND_ORE.getDefaultState(), 8)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(8, 0, 0, 256))));
        addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, Blocks.LAPIS_ORE.getDefaultState(), 7)).createDecoratedFeature(Decorator.COUNT_DEPTH_AVERAGE.configure(new CountDepthDecoratorConfig(8, 16, 256))));
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
}
