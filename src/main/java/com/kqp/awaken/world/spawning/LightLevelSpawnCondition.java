package com.kqp.awaken.world.spawning;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class LightLevelSpawnCondition extends SpawnCondition {
    /**
     * This one is used for vanilla mobs.
     * So it checks both the sky light and the block light.
     */
    public static final LightLevelSpawnCondition INSTANCE_BOTH_7 = new LightLevelSpawnCondition(7, 7);

    /**
     * Used for mobs that can spawn during the day.
     * We specify a max block light level so players
     * can stop spawns using torches.
     */
    public static final LightLevelSpawnCondition INSTANCE_BLOCK_7 = new LightLevelSpawnCondition(16, 7);

    public final int maxSkyLightLevel, maxBlockLightLevel;

    public LightLevelSpawnCondition(int maxSkyLightLevel, int maxBlockLightLevel) {
        this.maxSkyLightLevel = maxSkyLightLevel;
        this.maxBlockLightLevel = maxBlockLightLevel;
    }

    @Override
    public boolean test(World spawnWorld, BlockPos pos) {
        return spawnWorld.getLightLevel(LightType.BLOCK, pos) <= maxBlockLightLevel
                && spawnWorld.getLightLevel(LightType.SKY, pos) <= maxSkyLightLevel;
    }
}
