package com.kqp.awaken.entity.mob;

import com.kqp.awaken.init.AwakenEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.AttackGoal;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.PounceAtTargetGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
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
    public DireWolfEntity(EntityType type, World world) {
        super(type, world);
    }

    public static DefaultAttributeContainer.Builder createDireWolfAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12.0D);
    }

    @Override
    protected void initGoals() {
        super.initGoals();

        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(3, new PounceAtTargetGoal(this, 0.4F));
        this.goalSelector.add(4, new AttackGoal(this));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));

        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new FollowTargetGoal(this, PlayerEntity.class, false));
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        MobNavigation navigation = new MobNavigation(this, world);
        navigation.setCanSwim(true);
        navigation.setSpeed(0.6D);
        navigation.setAvoidSunlight(false);
        navigation.setCanPathThroughDoors(false);

        return navigation;
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
}
