package com.kqp.awaken.world.spawning;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CaveSpawnCondition extends SpawnCondition {
    public static final CaveSpawnCondition INSTANCE = new CaveSpawnCondition();

    @Override
    public boolean test(World spawnWorld, BlockPos pos) {
        return pos.getY() < spawnWorld.getSeaLevel() && spawnWorld.getBlockState(pos).getBlock().is(Blocks.CAVE_AIR);
    }
}
