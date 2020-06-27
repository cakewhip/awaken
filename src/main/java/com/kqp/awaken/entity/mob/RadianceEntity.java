package com.kqp.awaken.entity.mob;

import com.kqp.awaken.init.AwakenEntities;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.function.Predicate;

public class RadianceEntity extends AwakenBossEntity implements RangedAttackMob {
    private static final TrackedData<Integer> INVUL_TIMER;
    private static final Predicate<LivingEntity> CAN_ATTACK_PREDICATE;

    public final RadianceTask[] tasks;
    public RadianceTask currentTask;
    public Vec3d offsetVec;

    public RadianceEntity(World world) {
        super(AwakenEntities.RADIANCE, world);

        this.bossBar.setColor(BossBar.Color.WHITE);
        this.experiencePoints = 50;
        this.regenAmt = 2;
        this.regenCoolDown = 20;

        this.noClip = true;
        this.setNoGravity(true);

        tasks = new RadianceTask[] { new BasicTask(), new QuickMoveTask() };
    }

    @Override
    protected void mobTick() {
        int j;
        if (this.getInvulnerableTimer() > 0) {
            j = this.getInvulnerableTimer() - 1;

            if (j <= 0) {
                Explosion.DestructionType destructionType = this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;

                this.world.createExplosion(this, this.getX(), this.getEyeY(), this.getZ(), 7.0F, false, destructionType);

                if (!this.isSilent()) {
                    this.world.syncGlobalEvent(1023, this.getBlockPos(), 0);
                }
            }

            this.setInvulTimer(j);
            if (this.age % 10 == 0) {
                this.heal(25.0F);
            }
        } else {
            super.mobTick();

            LivingEntity target = getTarget();

            if (target != null) {
                if (currentTask == null || currentTask.isDone()) {
                    if (currentTask != null) {
                        currentTask.end();
                    }

                    currentTask = tasks[random.nextInt(tasks.length)];
                    currentTask.start();
                }

                currentTask.tick();
            }
        }
    }

    protected Vec3d genRandomHorizontalVector(double range) {
        return new Vec3d(
                random.nextDouble() - random.nextDouble(),
                0.0D,
                random.nextDouble() - random.nextDouble()
        ).normalize().multiply(range);
    }

    @Override
    public void tickMovement() {
        if (!this.world.isClient && battleStarted()) {
            LivingEntity target = getTarget();

            if (target != null) {
                if (offsetVec != null) {
                    Vec3d targetVec = target.getPos().add(offsetVec);

                    if (!closeEnough(targetVec)) {
                        Vec3d diffVec = targetVec.add(this.getPos().negate()).normalize();
                        this.setVelocity(diffVec.multiply(getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)));
                    } else {
                        this.setVelocity(this.getVelocity().multiply(0.2D));
                    }
                }
            }
        }

        super.tickMovement();

        if (this.getInvulnerableTimer() > 0) {
            for (int u = 0; u < 3; ++u) {
                this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + this.random.nextGaussian(), this.getY() + (double) (this.random.nextFloat() * 3.3F), this.getZ() + this.random.nextGaussian(), 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D);
            }
        }
    }

    private boolean closeEnough(Vec3d targetVec) {
        return getPos().squaredDistanceTo(targetVec) < 0.5D;
    }

    public void onSpawn() {
        this.setInvulTimer(220);
        this.setHealth(this.getMaxHealth() / 3.0F);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new ProjectileAttackGoal(this, 1.0D, 40, 20.0F));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(4, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]));
        this.targetSelector.add(2, new FollowTargetGoal(this, PlayerEntity.class, 0, false, false, CAN_ATTACK_PREDICATE));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(INVUL_TIMER, 0);
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putInt("Invul", this.getInvulnerableTimer());
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        this.setInvulTimer(tag.getInt("Invul"));
        if (this.hasCustomName()) {
            this.bossBar.setName(this.getDisplayName());
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (source != DamageSource.DROWN && !(source.getAttacker() instanceof WitherEntity)) {
            if (this.getInvulnerableTimer() > 0 && source != DamageSource.OUT_OF_WORLD) {
                return false;
            }
        }

        return super.damage(source, amount);
    }

    @Override
    public boolean addStatusEffect(StatusEffectInstance effect) {
        return false;
    }

    public int getInvulnerableTimer() {
        return this.dataTracker.get(INVUL_TIMER);
    }

    public void setInvulTimer(int ticks) {
        this.dataTracker.set(INVUL_TIMER, ticks);
    }

    public boolean battleStarted() {
        return getInvulnerableTimer() <= 0;
    }

    public static DefaultAttributeContainer.Builder createRadianceAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.45D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 450D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64D)
                .add(EntityAttributes.GENERIC_ARMOR, 12.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.UNDEAD;
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        if (battleStarted() && false) {
            double g = this.getPos().x;
            double h = this.getPos().y + this.getHeight() / 2D;
            double i = this.getPos().z;
            double j = target.getPos().x - g;
            double k = target.getPos().y - h;
            double l = target.getPos().z - i;

            WitherSkullEntity witherSkullEntity = new WitherSkullEntity(this.world, this, j, k, l);
            witherSkullEntity.setOwner(this);

            witherSkullEntity.setPos(g, h, i);
            this.world.spawnEntity(witherSkullEntity);
        }
    }

    abstract class RadianceTask {
        public abstract void start();

        public abstract void tick();

        public abstract boolean isDone();

        public void end() {
        }
    }

    class BasicTask extends RadianceTask {
        private int timer = 0;

        @Override
        public void start() {
            offsetVec = genRandomHorizontalVector(4D).add(0D, 3D, 0D);
            timer = 120;
        }

        @Override
        public void tick() {
            timer--;
        }

        @Override
        public boolean isDone() {
            return timer <= 0;
        }
    }

    class QuickMoveTask extends RadianceTask {
        private EntityAttributeModifier SPEED_BOOST = new EntityAttributeModifier(
                "radiance_speed_boost",
                1D,
                EntityAttributeModifier.Operation.MULTIPLY_BASE
        );

        private int timer = 0;

        @Override
        public void start() {
            timer = 120;
            getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).addTemporaryModifier(SPEED_BOOST);
        }

        @Override
        public void tick() {
            if (timer % 15 == 0) {
                offsetVec = genRandomHorizontalVector(2.5D).add(0D, 2D, 0D);
            }

            timer--;
        }

        @Override
        public boolean isDone() {
            return timer <= 0;
        }

        @Override
        public void end() {
            getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).removeModifier(SPEED_BOOST);
        }
    }

    static {
        INVUL_TIMER = DataTracker.registerData(RadianceEntity.class, TrackedDataHandlerRegistry.INTEGER);
        CAN_ATTACK_PREDICATE = (livingEntity) -> livingEntity.getGroup() != EntityGroup.UNDEAD && livingEntity.isMobOrPlayer();
    }
}
