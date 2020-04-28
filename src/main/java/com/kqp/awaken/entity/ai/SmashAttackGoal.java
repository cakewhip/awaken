package com.kqp.awaken.entity.ai;

import com.kqp.awaken.entity.AbominationEntity;
import com.kqp.awaken.init.AwakenNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.List;

public class SmashAttackGoal extends Goal {
    private static final int RANGE = 12;
    private static final int COOL_DOWN = 5 * 20;

    private final MobEntity mob;
    private int coolDown;

    public SmashAttackGoal(MobEntity mob) {
        this.mob = mob;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    public boolean canStart() {
        return this.getTarget() != null;
    }

    public boolean shouldContinue() {
        LivingEntity target = this.getTarget();

        if (!target.isAlive()) {
            return false;
        } else if (!this.inSmashingRange()) {
            return false;
        } else {
            return !this.mob.getNavigation().isIdle() || this.canStart();
        }
    }

    public void stop() {
        this.mob.getNavigation().stop();
    }

    public void tick() {
        LivingEntity target = this.getTarget();

        this.mob.getLookControl().lookAt(target, 30.0F, 30.0F);

        double speed = this.mob.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);

        this.mob.getNavigation().startMovingTo(target, speed);

        this.coolDown = Math.max(this.coolDown - 1, 0);

        if (this.coolDown <= 0) {
            if (this.inSmashingRange()) {
                this.coolDown = COOL_DOWN;

                doSmashAttack();
            }
        }
    }

    public void doSmashAttack() {
        World world = this.mob.world;
        Vec3d cent = this.mob.getPos().add(0, this.mob.getHeight() / -2, 0);

        List<Entity> entities = world.getEntities(null, new Box(cent.add(-12, -3, -12), cent.add(12, 3, 12)));

        for (Entity entity : entities) {
            if (this.mob.squaredDistanceTo(entity) < RANGE) {
                entity.damage(DamageSource.explosion(this.mob), (float) this.mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
            }
        }

        AwakenNetworking.ABOMINATION_SMASH_ATTACK_S2C.send((AbominationEntity) this.mob);
    }

    public boolean inSmashingRange() {
        return this.mob.squaredDistanceTo(this.mob.getTarget()) < RANGE;
    }

    private LivingEntity getTarget() {
        return this.mob.getTarget();
    }
}
