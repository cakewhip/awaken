package com.kqp.awaken.world.spawning;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LightLevelSpawnCondition extends SpawnCondition {
    public static final LightLevelSpawnCondition INSTANCE_7 = new LightLevelSpawnCondition(7);

    public final int minimumLightLevel;

    public LightLevelSpawnCondition(int minimumLightLevel) {
        this.minimumLightLevel = minimumLightLevel;
    }

    @Override
    public boolean test(World spawnWorld, BlockPos pos) {
        return spawnWorld.getLightLevel(pos) <= minimumLightLevel;
    }
}
