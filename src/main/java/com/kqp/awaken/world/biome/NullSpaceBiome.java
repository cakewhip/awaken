package com.kqp.awaken.world.biome;

import com.kqp.awaken.init.AwakenBiomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;

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
                        .waterColor(0x171C40)
                        .waterFogColor(0x0C0F24)
                        .build())
                .parent(null)
        );
    }
}
