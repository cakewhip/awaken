package com.kqp.awaken.world.spawning;

import com.kqp.awaken.world.data.AwakenLevelData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FieryMoonSpawnCondition extends SpawnCondition {
    public static final FieryMoonSpawnCondition INSTANCE = new FieryMoonSpawnCondition();

    @Override
    public boolean test(World spawnWorld, BlockPos pos) {
        return AwakenLevelData.getFor(spawnWorld.getServer()).isFieryMoonActive();
    }
}
