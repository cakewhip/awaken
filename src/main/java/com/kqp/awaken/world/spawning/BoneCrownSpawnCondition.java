package com.kqp.awaken.world.spawning;

import com.kqp.awaken.init.AwakenItems;
import com.kqp.awaken.util.TrinketUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.List;

public class BoneCrownSpawnCondition extends SpawnCondition {
    private static final int MAX_SPAWN_DISTANCE_SQUARED = 128 * 128;

    public static final BoneCrownSpawnCondition INSTANCE = new BoneCrownSpawnCondition();

    @Override
    public boolean test(World spawnWorld, BlockPos pos) {
        List<? extends PlayerEntity> players = spawnWorld.getPlayers();

        for (PlayerEntity player : players) {
            if (TrinketUtil.hasTrinket(player, AwakenItems.Trinkets.BONE_CROWN)) {
                /** {@link Vec3d#method_24953(Vec3i)} is used to convert block positions to Vec3d objects **/
                if (player.squaredDistanceTo(Vec3d.method_24953(pos)) < MAX_SPAWN_DISTANCE_SQUARED) {
                    return true;
                }
            }
        }

        return false;
    }
}
