package com.kqp.awaken.entity.ai;

import com.kqp.awaken.entity.AbominationEntity;
import com.kqp.awaken.init.AwakenNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class AbominationMoveSetGoal extends MoveSetGoal<AbominationEntity> {
    public AbominationMoveSetGoal(AbominationEntity mob) {
        super(mob, new int[] { 0, 1, 1, 1 });

        this.addMove(new SpawnSpawnlingsMove());
        this.addMove(new SmashAttackMove());
    }

    class SpawnSpawnlingsMove extends Move<AbominationEntity> {
        public SpawnSpawnlingsMove() {
            super(4 * 20);
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

            for (int i = 0; i < 4 + r.nextInt(3); i++) {
                Entity entity = AbominationEntity.FRIENDLY_TYPES[r.nextInt(AbominationEntity.FRIENDLY_TYPES.length)].create(world);
                entity.refreshPositionAndAngles(mob.getX(), mob.getY(), mob.getZ(), 0F, 0F);
                world.spawnEntity(entity);
            }

            AwakenNetworking.ABOMINATION_SPAWN_SPAWNLINGS_S2C.send(mob);
        }
    }

    class SmashAttackMove extends Move<AbominationEntity> {
        private static final int RANGE = 8;
        private static final int SQUARED_RANGE = RANGE * RANGE;

        public SmashAttackMove() {
            super(5 * 20);
        }

        @Override
        public void start(AbominationEntity mob) {
        }

        @Override
        public void tick(AbominationEntity mob) {
            LivingEntity target = mob.getTarget();

            if (target != null) {
                mob.getLookControl().lookAt(target, 30.0F, 30.0F);

                double speed = mob.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);

                mob.getNavigation().startMovingTo(target, speed);
            }
        }

        @Override
        public void stop(AbominationEntity mob) {
            LivingEntity target = mob.getTarget();

            if (target != null && mob.squaredDistanceTo(target) < SQUARED_RANGE) {
                World world = mob.world;
                Vec3d cent = mob.getPos();

                List<Entity> entities = world.getEntities(null, new Box(
                        cent.add(-RANGE, -6, -RANGE),
                        cent.add(RANGE, 3, RANGE)
                ));

                for (Entity entity : entities) {
                    if (AbominationEntity.CAN_ATTACK_PREDICATE.test(entity) && mob.squaredDistanceTo(entity) < SQUARED_RANGE) {
                        entity.damage(DamageSource.explosion(mob), (float) mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
                    }
                }

                AwakenNetworking.ABOMINATION_SMASH_ATTACK_S2C.send(mob);
            }
        }
    }
}
