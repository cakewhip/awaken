package com.kqp.awaken.world.gen.chunk;

import net.minecraft.world.gen.chunk.CavesChunkGeneratorConfig;

public class NullSpaceChunkGeneratorConfig extends CavesChunkGeneratorConfig {
    @Override
    public int getBedrockCeilingY() {
        return 256;
    }
}
