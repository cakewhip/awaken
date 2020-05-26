package com.kqp.awaken.entity.ai;

import com.kqp.awaken.init.AwakenItems;
import com.kqp.awaken.util.TrinketUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;

public class BoneCrownTargetGoal extends TrackTargetGoal {
    protected LivingEntity targetEntity;

    public BoneCrownTargetGoal(MobEntity mob) {
        super(mob, false);
    }

    @Override
    public boolean canStart() {
        this.findClosestTarget();

        return targetEntity != null;
    }

    @Override
    public void start() {
        this.mob.setTarget(this.targetEntity);
        super.start();
    }

    protected void findClosestTarget() {
        this.targetEntity = this.mob.world.getClosestEntityIncludingUngeneratedChunks(
                LivingEntity.class,
                new TargetPredicate().setPredicate(entity -> {
                    DamageSource lastDamageSource = entity.getRecentDamageSource();
                    if (lastDamageSource != null) {
                        Entity lastAttacker = lastDamageSource.getAttacker();

                        return lastAttacker instanceof LivingEntity &&
                                TrinketUtil.hasTrinket((LivingEntity) lastAttacker, AwakenItems.Trinkets.BONE_CROWN);
                    }

                    return false;
                }),
                this.mob,
                this.mob.getX(),
                this.mob.getEyeY(),
                this.mob.getZ(),
                this.mob.getBoundingBox().expand(this.getFollowRange(), 4.0D, this.getFollowRange())
        );
    }
}
