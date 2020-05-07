package com.kqp.awaken.entity.ai.abomination;

import com.kqp.awaken.entity.ai.Move;
import com.kqp.awaken.entity.mob.AbominationEntity;
import com.kqp.awaken.init.AwakenNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class SmashAttackMove extends Move<AbominationEntity> {
    private final int RANGE = 8;
    private final int SQUARED_RANGE = RANGE * RANGE;

    public SmashAttackMove(int duration) {
        super(duration);
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
                if (entity != mob && AbominationEntity.CAN_ATTACK_PREDICATE.test(entity) && mob.squaredDistanceTo(entity) < SQUARED_RANGE) {
                    entity.damage(DamageSource.explosion(mob), (float) mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
                }
            }

            AwakenNetworking.ABOMINATION_SMASH_ATTACK_S2C.send(mob);
        }
    }
}