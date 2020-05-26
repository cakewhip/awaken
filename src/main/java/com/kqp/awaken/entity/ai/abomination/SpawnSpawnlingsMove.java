package com.kqp.awaken.entity.ai.abomination;

import com.kqp.awaken.entity.ai.Move;
import com.kqp.awaken.entity.mob.AbominationEntity;
import com.kqp.awaken.init.AwakenNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.world.World;

import java.util.Random;

public class SpawnSpawnlingsMove extends Move<AbominationEntity> {
    public SpawnSpawnlingsMove(int duration) {
        super(duration);
    }

    @Override
    public void start(AbominationEntity mob) {
    }

    @Override
    public void tick(AbominationEntity mob) {
    }

    @Override
    public void stop(AbominationEntity mob) {
        World world = mob.world;
        Random r = world.random;

        EntityType type = AbominationEntity.FRIENDLY_TYPES[r.nextInt(AbominationEntity.FRIENDLY_TYPES.length)];

        for (int i = 0; i < 3 + r.nextInt(2); i++) {

            type.spawn(world, null, null, null,
                    mob.getBlockPos().add(2 * (r.nextDouble() - r.nextDouble()), 0, 2 * (r.nextDouble() - r.nextDouble())),
                    SpawnReason.MOB_SUMMONED, true, false);
        }

        AwakenNetworking.ABOMINATION_SPAWN_SPAWNLINGS_S2C.send(mob);
    }
}