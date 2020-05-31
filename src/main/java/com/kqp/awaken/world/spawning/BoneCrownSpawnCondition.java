package com.kqp.awaken.world.spawning;

import com.kqp.awaken.init.AwakenAbilities;
import com.kqp.awaken.init.AwakenItems;
import com.kqp.awaken.util.EquipmentUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class BoneCrownSpawnCondition extends SpawnCondition {
    private static final int MAX_SPAWN_DISTANCE_SQUARED = 128 * 128;

    public static final BoneCrownSpawnCondition INSTANCE = new BoneCrownSpawnCondition();

    @Override
    public boolean test(World spawnWorld, BlockPos pos) {
        List<? extends PlayerEntity> players = spawnWorld.getPlayers();

        for (PlayerEntity player : players) {
            if (AwakenAbilities.BONE_CROWN_EFFECT.get(player).flag) {
                if (player.squaredDistanceTo(Vec3d.of(pos)) < MAX_SPAWN_DISTANCE_SQUARED) {
                    return true;
                }
            }
        }

        return false;
    }
}
