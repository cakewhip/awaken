package com.kqp.awaken.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;

import java.util.Random;

public class SpawnSpawnlingsGoal extends Goal {
    private static final EntityType[] FRIENDLY_TYPES = {
            EntityType.ZOMBIE,
            EntityType.SKELETON,
            EntityType.SPIDER,
            EntityType.CAVE_SPIDER,
            EntityType.CREEPER
    };

    private static final int COOL_DOWN = 5 * 20;

    private final MobEntity mob;
    private int coolDown;

    public SpawnSpawnlingsGoal(MobEntity mob) {
        this.mob = mob;
    }

    @Override
    public boolean canStart() {
        return true;
    }

    @Override
    public boolean shouldContinue() {
        return true;
    }

    @Override
    public void tick() {
        this.coolDown = Math.max(this.coolDown - 1, 0);

        if (this.coolDown <= 0) {
            this.coolDown = COOL_DOWN;

            spawnSpawnlings();
        }
    }

    public void spawnSpawnlings() {
        World world = this.mob.world;
        Random r = world.random;

        for (int i = 0; i < 4 + r.nextInt(3); i++) {
            Entity entity = FRIENDLY_TYPES[r.nextInt(FRIENDLY_TYPES.length)].create(world);
            entity.refreshPositionAndAngles(this.mob.getX(), this.mob.getY(), this.mob.getZ(), 0F, 0F);
            world.spawnEntity(entity);
        }
    }
}
