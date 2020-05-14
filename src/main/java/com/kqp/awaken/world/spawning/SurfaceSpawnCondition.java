package com.kqp.awaken.world.spawning;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SurfaceSpawnCondition extends SpawnCondition {
    public static final SurfaceSpawnCondition INSTANCE = new SurfaceSpawnCondition();

    @Override
    public boolean test(World spawnWorld, BlockPos pos) {
        return spawnWorld.isSkyVisible(pos);
    }
}
