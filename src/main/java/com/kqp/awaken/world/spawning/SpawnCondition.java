package com.kqp.awaken.world.spawning;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class SpawnCondition {
    public abstract boolean test(World spawnWorld, BlockPos pos);
}
