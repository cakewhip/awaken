package com.kqp.awaken.entity;

import com.kqp.awaken.Awaken;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

/**
 * Dire wolf entity class.
 */
public class DireWolfEntity extends HostileEntity {
    public DireWolfEntity(World world) {
        super(Awaken.TEntities.DIRE_WOLF, world);

        this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
    }

    @Override
    protected void initGoals() {
        super.initGoals();

        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(3, new PounceAtTargetGoal(this, 0.4F));
        this.goalSelector.add(4, new AttackGoal(this));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]));
        this.targetSelector.add(2, new FollowTargetGoal(this, PlayerEntity.class, false));
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).setBaseValue(12.0D);
        this.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getAttributeInstance(EntityAttributes.FOLLOW_RANGE).setBaseValue(48.0D);
        this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
    }

    @Override
    public boolean tryAttack(Entity target) {
        if (!super.tryAttack(target)) {
            return false;
        } else {
            if (target instanceof LivingEntity) {
                ((LivingEntity)target).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 1));
            }

            return true;
        }
    }
}
