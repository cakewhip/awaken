package com.kqp.awaken.world.chunk;

import net.minecraft.world.gen.chunk.CavesChunkGeneratorConfig;

public class NullSpaceChunkGeneratorConfig extends CavesChunkGeneratorConfig {
    @Override
    public int getBedrockCeilingY() {
        return 256;
    }
}
