package com.kqp.awaken.init;

import com.kqp.awaken.world.biome.NullSpaceBiome;
import com.kqp.awaken.world.gen.NullSpaceSurfaceBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

public class AwakenBiomes {
    public static final TernarySurfaceConfig NULL_SPACE_CONFIG = new TernarySurfaceConfig(Blocks.STONE.getDefaultState(), Blocks.STONE.getDefaultState(), Blocks.LAVA.getDefaultState());

    public static final NullSpaceSurfaceBuilder NULL_SPACE_SURFACE_BUILDER = new NullSpaceSurfaceBuilder(TernarySurfaceConfig::deserialize);

    public static final Biome NULL_SPACE = new NullSpaceBiome();

    public static void init() {
        Registry.register(
                Registry.SURFACE_BUILDER,
                new Identifier(Awaken.MOD_ID, "null_space"),
                NULL_SPACE_SURFACE_BUILDER
        );

        Registry.register(Registry.BIOME, new Identifier(Awaken.MOD_ID, "null_space"), NULL_SPACE);
    }
}
