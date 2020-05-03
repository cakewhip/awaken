package com.kqp.awaken.init;

import com.kqp.awaken.world.feature.CrackedBedrockFeature;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class AwakenWorld {
    private static final Feature<DefaultFeatureConfig> CRACKED_BEDROCK = Registry.register(
            Registry.FEATURE,
            new Identifier(Awaken.MOD_ID, "cracked_bedrock"),
            new CrackedBedrockFeature(DefaultFeatureConfig::deserialize)
    );

    public static void init() {
        Registry.BIOME.forEach(biome -> biome.addFeature(
                GenerationStep.Feature.UNDERGROUND_DECORATION,
                CRACKED_BEDROCK.configure(new DefaultFeatureConfig())
                .createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(1, 0, 0, 1)))
        ));
    }
}
