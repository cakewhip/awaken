package com.kqp.awaken.entity;

import com.kqp.awaken.Awaken;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Class for the raptor chicken.
 * Extends AnimalEntity, but possesses a lot of hostile mob stuff as well.
 */
public class RaptorChickenEntity extends AnimalEntity {
    public int eggLayTime;

    public float field_6741;
    public float field_6743;
    public float field_6738;
    public float field_6736;
    public float field_6737 = 1.0F;

    public RaptorChickenEntity(World world) {
        super(Awaken.TEntities.RAPTOR_CHICKEN, world);

        resetEggTimer();

        this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.world.isClient && !isInLove() && this.getBreedingAge() == 0) {
            for (ItemEntity itemEntity : this.world.getEntities(ItemEntity.class, new Box(this.getBlockPos()), null)) {
                ItemStack stack = itemEntity.getStack();

                if (stack.getItem() == Items.WHEAT_SEEDS && stack.getCount() > 0) {
                    stack.decrement(1);
                    setInLove();

                    break;
                }
            }
        }
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return this.isBaby() ? dimensions.height * 0.85F : dimensions.height * 0.92F;
    }

    @Override
    protected void initGoals() {
        super.initGoals();

        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0D));
        this.goalSelector.add(3, new PounceAtTargetGoal(this, 0.4F));
        this.goalSelector.add(4, new RaptorChickenEntity.AttackGoal(this));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]));
        this.targetSelector.add(2, new FollowTargetGoal(this, PlayerEntity.class, false));
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.getAttributes().register(EntityAttributes.ATTACK_DAMAGE).setBaseValue(12.0D);
        this.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getAttributeInstance(EntityAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.33D);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        this.field_6736 = this.field_6741;
        this.field_6738 = this.field_6743;
        this.field_6743 = (float)((double)this.field_6743 + (double)(this.onGround ? -1 : 4) * 0.3D);
        this.field_6743 = MathHelper.clamp(this.field_6743, 0.0F, 1.0F);
        if (!this.onGround && this.field_6737 < 1.0F) {
            this.field_6737 = 1.0F;
        }

        this.field_6737 = (float)((double)this.field_6737 * 0.9D);
        Vec3d vec3d = this.getVelocity();
        if (!this.onGround && vec3d.y < 0.0D) {
            this.setVelocity(vec3d.multiply(1.0D, 0.6D, 1.0D));
        }

        this.field_6741 += this.field_6737 * 2.0F;
        if (!this.world.isClient && this.isAlive() && !this.isBaby() && --this.eggLayTime <= 0) {
            this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.dropItem(Awaken.TItems.RAPTOR_CHICKEN_EGG);
            resetEggTimer();
        }
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_CHICKEN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_CHICKEN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CHICKEN_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15F, 0.5F);
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        return effect.getEffectType() == StatusEffects.POISON ? false : super.canHaveStatusEffect(effect);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);

        if (tag.contains("EggLayTime")) {
            this.eggLayTime = tag.getInt("EggLayTime");
        }
    }

    @Override
    public boolean canBreedWith(AnimalEntity other) {
        return other instanceof RaptorChickenEntity && other != this && this.isInLove() && other.isInLove();
    }

    @Override
    public PassiveEntity createChild(PassiveEntity mate) {
        return Awaken.TEntities.RAPTOR_CHICKEN.create(this.world);
    }

    public void setInLove() {
        this.setLoveTicks(600);
        this.world.sendEntityStatus(this, (byte)18);
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);

        tag.putInt("EggLayTime", this.eggLayTime);
    }

    @Override
    public boolean tryAttack(Entity target) {
        if (!super.tryAttack(target)) {
            return false;
        } else {
            if (target instanceof LivingEntity) {
                ((LivingEntity)target).addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 200, 1));
            }

            return true;
        }
    }

    private void resetEggTimer() {
        this.eggLayTime = this.random.nextInt(12000) + 12000;
    }

    static class AttackGoal extends MeleeAttackGoal {
        public AttackGoal(RaptorChickenEntity raptorChickenEntity) {
            super(raptorChickenEntity, 1.0D, true);
        }

        protected double getSquaredMaxAttackDistance(LivingEntity entity) {
            return 8.0F;
        }
    }
}
