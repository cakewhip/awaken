package com.kqp.awaken.entity.mob;

import com.kqp.awaken.entity.ai.BetterMeleeAttackGoal;
import com.kqp.awaken.init.AwakenEntities;
import com.kqp.awaken.init.AwakenItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
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
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class VagabondEntity extends HostileEntity {
    public VagabondEntity(EntityType type, World world) {
        super(type, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new FleeEntityGoal(this, RenegadeEntity.class, 24.0F, 1.0D, 1.05D));
        this.goalSelector.add(3, new FleeEntityGoal(this, PlayerEntity.class, 6.0F, 1.0D, 1.05D));
        this.goalSelector.add(5, new BetterMeleeAttackGoal(this, 1.8F));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0D));

        this.targetSelector.add(1, new FollowTargetGoal(this, AnimalEntity.class, true));
    }

    @Override
    public EntityData initialize(WorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CompoundTag entityTag) {
        this.initEquipment(difficulty);

        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }

    public static DefaultAttributeContainer.Builder createVagabondAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.305D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0.25D);
    }
}
