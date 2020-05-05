package com.kqp.awaken.world.gen.feature;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.world.gen.feature.FeatureConfig;

public class NullSpikeFeatureConfig implements FeatureConfig {
    public NullSpikeFeatureConfig() {
    }

    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        return new Dynamic(ops, ops.createMap(ImmutableMap.of()));
    }

    public static <T> NullSpikeFeatureConfig deserialize(Dynamic<T> dynamic) {
        return new NullSpikeFeatureConfig();
    }
}
