package com.kqp.awaken.world.spawning;

import com.kqp.awaken.data.AwakenLevelData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PostAwakeningSpawnCondition extends SpawnCondition {
    public static final PostAwakeningSpawnCondition INSTANCE = new PostAwakeningSpawnCondition();

    @Override
    public boolean test(World spawnWorld, BlockPos pos) {
        System.out.println(AwakenLevelData.getFor(spawnWorld.getServer()).isWorldAwakened());
        return AwakenLevelData.getFor(spawnWorld.getServer()).isWorldAwakened();
    }
}
