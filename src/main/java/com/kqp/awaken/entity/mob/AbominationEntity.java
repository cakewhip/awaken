package com.kqp.awaken.entity.mob;

import com.kqp.awaken.entity.ai.abomination.AbominationPhase1MoveSetGoal;
import com.kqp.awaken.entity.ai.abomination.AbominationPhase2MoveSetGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class AbominationEntity extends AwakenBossEntity {
    public static final EntityType[] FRIENDLY_TYPES = {
            EntityType.ZOMBIE,
            EntityType.SKELETON,
            EntityType.SPIDER,
            EntityType.CAVE_SPIDER,
            EntityType.CREEPER
    };

    public static final Predicate<Entity> CAN_ATTACK_PREDICATE;

    public AbominationEntity(EntityType type, World world) {
        super(type, world);

        this.experiencePoints = 50;
        this.regenCoolDown = 20;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new AbominationPhase1MoveSetGoal(this));
        this.goalSelector.add(2, new AbominationPhase2MoveSetGoal(this));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 64.0F));
        this.goalSelector.add(5, new LookAroundGoal(this));

        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new FollowTargetGoal(this, MobEntity.class, 0, false, true, CAN_ATTACK_PREDICATE));
    }

    public static DefaultAttributeContainer.Builder createAbominationAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1200D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 72D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 14.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void mobTick() {
        if (aboveHalfHealth()) {
            this.regenAmt = 4;
        } else {
            this.regenAmt = 6;
        }

        super.mobTick();
    }

    static {
        CAN_ATTACK_PREDICATE = (entity) -> {
            for (EntityType type : FRIENDLY_TYPES) {
                if (entity.getType() == type) {
                    return false;
                }
            }

            return true;
        };
    }
}
