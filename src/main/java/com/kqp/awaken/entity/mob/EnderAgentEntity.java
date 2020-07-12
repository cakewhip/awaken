package com.kqp.awaken.entity.mob;

import com.kqp.awaken.entity.ai.BetterMeleeAttackGoal;
import com.kqp.awaken.init.AwakenItems;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.List;

public class EnderAgentEntity extends HostileEntity {
    private static final Item[] HELD_ITEMS = {
            AwakenItems.Reagents.ANCIENT_DAGGER,
            AwakenItems.Reagents.ANCIENT_FORK,
            AwakenItems.Reagents.ANCIENT_SICKLE
    };

    private static final float DROP_CHANCE = 0.33F;

    public EnderAgentEntity(EntityType type, World world) {
        super(type, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new BetterMeleeAttackGoal(this, 2.0F));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(5, new LookAroundGoal(this));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));

        this.targetSelector.add(0, new HivemindTargetGoal(this));
        this.targetSelector.add(3, new RevengeGoal(this));
        this.targetSelector.add(5, new FollowTargetGoal(this, PlayerEntity.class, true));
    }

    @Override
    public EntityData initialize(WorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CompoundTag entityTag) {
        this.initEquipment(difficulty);

        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }

    public static DefaultAttributeContainer.Builder createEnderAgentAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.365D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0.5D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D);
    }

    @Override
    protected void initEquipment(LocalDifficulty difficulty) {
        super.initEquipment(difficulty);

        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(HELD_ITEMS[random.nextInt(HELD_ITEMS.length)]));
    }

    @Override
    protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {
        super.dropEquipment(source, lootingMultiplier, allowDrops);

        if (random.nextFloat() < DROP_CHANCE) {
            ItemStack held = this.getStackInHand(Hand.MAIN_HAND);

            if (held != null && !held.isEmpty()) {
                ItemEntity itemEntity = this.dropItem(held.getItem());

                if (itemEntity != null) {
                    itemEntity.setCovetedItem();
                }
            }
        }
    }

    class HivemindTargetGoal extends TrackTargetGoal {
        private EnderAgentEntity agentEntity;

        public HivemindTargetGoal(EnderAgentEntity agentEntity) {
            super(agentEntity, false, true);

            this.agentEntity = agentEntity;
        }

        @Override
        public boolean canStart() {
            List<EnderAgentEntity> fellowAgents = getNearbyAgents();

            for (EnderAgentEntity agent : fellowAgents) {
                if (agent.getAttacker() != null) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public boolean shouldContinue() {
            List<EnderAgentEntity> fellowAgents = getNearbyAgents();

            for (EnderAgentEntity agent : fellowAgents) {
                if (agent.getAttacker() != null && agent.getLastAttackedTime() != agentEntity.getLastAttackedTime()) {
                    agentEntity.setAttacker(agent.getAttacker());
                    agentEntity.lastAttackedTicks = agent.getLastAttackedTime();

                    return true;
                }
            }

            return false;
        }

        private List<EnderAgentEntity> getNearbyAgents() {
            return mob.world.getEntities(EnderAgentEntity.class, new Box(-16.0F, -16.0F, -16.0F, 16.0F, 16.0F, 16.0F), entity -> true);
        }
    }
}
