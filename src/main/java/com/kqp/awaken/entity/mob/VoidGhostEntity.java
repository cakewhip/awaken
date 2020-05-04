package com.kqp.awaken.entity.mob;

import com.kqp.awaken.init.AwakenEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.AttackGoal;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntityWithAi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class VoidGhostEntity extends HostileEntity {
    private WanderGoal wanderGoal;

    public VoidGhostEntity(World world) {
        super(AwakenEntities.VOID_GHOST, world);

        this.noClip = true;
        this.setNoGravity(true);
    }

    public static DefaultAttributeContainer.Builder createVoidGhostAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 60D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 14.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.8D);
    }

    @Override
    protected void initGoals() {
        super.initGoals();

        this.goalSelector.add(4, new AttackGoal(this));
        this.goalSelector.add(5, wanderGoal = new WanderGoal(this));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));

        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new FollowTargetGoal(this, PlayerEntity.class, false));
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        Vec3d targetPos = null;

        LivingEntity target = this.getTarget();

        if (target != null) {
            targetPos = target.getPos();
            this.lookAtEntity(target, 10F, 10F);
        } else if (wanderGoal != null) {
            targetPos = wanderGoal.getTarget();

            if (targetPos != null) {
                this.lookAt(targetPos);
                targetPos = targetPos.add(0D, 1D, 0D);
            }
        }

        if (targetPos != null) {
            Vec3d diff = targetPos.add(this.getPos().negate()).normalize();
            this.setVelocity(this.getVelocity().add(diff.multiply(0.015D)));
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        if (!super.tryAttack(target)) {
            return false;
        } else {
            if (target instanceof LivingEntity) {
                ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 2 * 20, 0));
            }

            return true;
        }
    }

    @Override
    public void slowMovement(BlockState state, Vec3d multiplier) {
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
        return false;
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.UNDEAD;
    }

    private void lookAt(Vec3d target) {
        double d = target.getX() - this.getX();
        double e = target.getZ() - this.getZ();
        double g = target.getY() + this.getY() + 1D;

        double h = MathHelper.sqrt(d * d + e * e);
        float i = (float) (MathHelper.atan2(e, d) * 57.2957763671875D) - 90.0F;
        float j = (float) (-(MathHelper.atan2(g, h) * 57.2957763671875D));
        this.pitch = this.changeAngle(this.pitch, j, 10F);
        this.yaw = this.changeAngle(this.yaw, i, 10F);
    }

    private float changeAngle(float oldAngle, float newAngle, float maxChangeInAngle) {
        float f = MathHelper.wrapDegrees(newAngle - oldAngle);
        if (f > maxChangeInAngle) {
            f = maxChangeInAngle;
        }

        if (f < -maxChangeInAngle) {
            f = -maxChangeInAngle;
        }

        return oldAngle + f;
    }

    class WanderGoal extends WanderAroundFarGoal {
        public WanderGoal(MobEntityWithAi mobEntityWithAi) {
            super(mobEntityWithAi, 0D);
        }

        public Vec3d getTarget() {
            return this.getWanderTarget();
        }
    }
}
