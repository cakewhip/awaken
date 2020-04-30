package com.kqp.awaken.entity;

import com.kqp.awaken.entity.ai.AbominationMoveSetGoal;
import com.kqp.awaken.init.AwakenEntities;
import com.kqp.awaken.init.AwakenNetworking;
import jdk.internal.jline.internal.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
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

    public AbominationEntity(World world) {
        super(AwakenEntities.ABOMINATION, world);

        this.experiencePoints = 50;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new AbominationMoveSetGoal(this));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 64.0F));
        this.goalSelector.add(4, new LookAroundGoal(this));

        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new FollowTargetGoal(this, MobEntity.class, 0, false, true, CAN_ATTACK_PREDICATE));
    }

    public static DefaultAttributeContainer.Builder createAbominationAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 800D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 18.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D);
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
