package com.kqp.awaken.entity.mob;

import com.kqp.awaken.entity.ai.BetterMeleeAttackGoal;
import com.kqp.awaken.init.AwakenEntities;
import com.kqp.awaken.init.AwakenItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class RenegadeEntity extends HostileEntity {
    public RenegadeEntity(World world) {
        super(AwakenEntities.RENEGADE, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new BetterMeleeAttackGoal(this, 1.8F));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(5, new LookAroundGoal(this));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0D));

        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]));
        this.targetSelector.add(2, new FollowTargetGoal(this, VagabondEntity.class, true));
        this.targetSelector.add(3, new FollowTargetGoal(this, PlayerEntity.class, true));
    }

    @Override
    public EntityData initialize(WorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CompoundTag entityTag) {
        this.initEquipment(difficulty);

        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }

    public static DefaultAttributeContainer.Builder createRenegadeAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.305D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0D);
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean success = super.tryAttack(target);

        if (success) {
            if (target instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) target;

                living.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.POISON,
                        80,
                        1
                ));
            }
        }

        return success;
    }

    @Override
    protected void initEquipment(LocalDifficulty difficulty) {
        super.initEquipment(difficulty);

        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(AwakenItems.Swords.RUSTY_SHANK));
    }
}
