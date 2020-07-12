package com.kqp.awaken.entity.mob;

import com.kqp.awaken.entity.projectile.RadianceLightEntity;
import com.kqp.awaken.init.AwakenNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.List;
import java.util.stream.Collectors;

public class RadianceEntity extends AwakenBossEntity implements RangedAttackMob {
    private static final TrackedData<Integer> INVUL_TIMER;

    public final RadianceTask[] firstPhase, secondPhase;
    public RadianceTask[] currentTasks;
    public RadianceTask currentTask;
    public Vec3d offsetVec;

    public RadianceEntity(EntityType type, World world) {
        super(type, world);

        this.bossBar.setColor(BossBar.Color.WHITE);
        this.experiencePoints = 50;
        this.regenAmt = 2;
        this.regenCoolDown = 20;

        this.noClip = true;
        this.setNoGravity(true);

        firstPhase = new RadianceTask[] { new BasicTask(), new QuickMoveTask(), new CircleTask(), new QuickAttackTask(1, 2.5D) };
        secondPhase = new RadianceTask[] { new CircleTask(), new QuickAttackTask(3, 2.5D) };
    }

    public PlayerEntity getNextRandomTarget() {
        List<PlayerEntity> targets = this.bossBar.getPlayers()
                .stream()
                .filter(player -> !player.isCreative())
                .collect(Collectors.toList());

        if (targets.isEmpty()) {
            return null;
        }

        return targets.get(random.nextInt(targets.size()));
    }

    @Override
    protected void mobTick() {
        int j;
        if (!battleStarted()) {
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

            // If can't find a target, attempt to find one here
            if (target == null) {
                this.setTarget(getNextRandomTarget());
                target = getTarget();
            }

            if (target != null) {
                if (this.aboveHalfHealth()) {
                    currentTasks = secondPhase;
                } else {
                    currentTasks = firstPhase;
                }

                if (currentTask == null || currentTask.isDone()) {
                    if (currentTask != null) {
                        currentTask.end();

                        // When a task ends, go ahead and find another target
                        targetRandomPlayer();
                    }

                    currentTask = currentTasks[random.nextInt(currentTasks.length)];
                    currentTask.start();
                }

                currentTask.tick();

                if (age % getAttackTime() == 0) {
                    attack(target, 0F);
                }
            }
        }
    }

    protected void targetRandomPlayer() {
        this.setTarget(getNextRandomTarget());
    }

    protected int getAttackTime() {
        if (aboveHalfHealth()) {
            return 40;
        } else {
            return 25;
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

                    double speed = getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                    double distance = Math.sqrt(this.squaredDistanceTo(targetVec));

                    if (!closeEnough(targetVec)) {
                        if (distance > 4D) {
                            speed *= 3D;
                        }

                        if (speed > distance) {
                            speed = distance;
                        }

                        Vec3d diffVec = targetVec.add(this.getPos().negate()).normalize();
                        this.setVelocity(diffVec.multiply(speed));
                    } else {
                        this.setVelocity(this.getVelocity().multiply(0.25D));
                    }
                }

                this.lookAtEntity(target, 10F, 10F);
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
        return getPos().squaredDistanceTo(targetVec) < 0.05D;
    }

    public void onSpawn() {
        this.setInvulTimer(220);
        this.setHealth(this.getMaxHealth() / 3.0F);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(3, new LookAroundGoal(this));
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
        if (this.isInvulnerableTo(source) || source.getAttacker() == this) {
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
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 750D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 128D)
                .add(EntityAttributes.GENERIC_ARMOR, 12.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.UNDEAD;
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        if (battleStarted()) {
            double g = this.getPos().x;
            double h = this.getPos().y + this.getHeight() / 2D;
            double i = this.getPos().z;
            double j = target.getPos().x - g;
            double k = target.getPos().y - h;
            double l = target.getPos().z - i;

            RadianceLightEntity lightEntity = new RadianceLightEntity(this.world, this, j, k, l);
            lightEntity.setOwner(this);
            lightEntity.setPos(g, h, i);

            AwakenNetworking.SPAWN_ENTITY_PACKET_S2C.send(lightEntity);
            this.world.spawnEntity(lightEntity);
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

    class CircleTask extends RadianceTask {
        private int timer = 0;
        private double angle = 0;
        private int direction = 1;

        @Override
        public void start() {
            timer = 150;
        }

        @Override
        public void tick() {
            if (timer % 50 == 0) {
                rollFlip();
            }

            angle += 4D * Math.PI / 180D * direction;
            offsetVec = new Vec3d(Math.sin(angle), 0D, Math.cos(angle)).multiply(5D).add(0D, 3D, 0D);

            timer--;
        }

        private void rollFlip() {
            if (random.nextFloat() < 0.5F) {
                direction = random.nextBoolean() ? 1 : -1;
            }
        }

        @Override
        public boolean isDone() {
            return timer <= 0;
        }
    }

    class QuickMoveTask extends RadianceTask {
        private EntityAttributeModifier SPEED_BOOST = new EntityAttributeModifier(
                "radiance_speed_boost",
                1.5D,
                EntityAttributeModifier.Operation.MULTIPLY_BASE
        );

        private int timer = 0;

        @Override
        public void start() {
            timer = 128;
            getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).addTemporaryModifier(SPEED_BOOST);
        }

        @Override
        public void tick() {
            if (timer % 16 == 0) {
                offsetVec = genRandomHorizontalVector(2.5D).add(0D, 2D + random.nextDouble() * 2D, 0D);
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

    class QuickAttackTask extends RadianceTask {

        private final int maxTimes;
        private final EntityAttributeModifier speedBoost;
        private int times = 0;
        private int timer = 0;

        public QuickAttackTask(int maxTimes, double speedBoost) {
            this.maxTimes = maxTimes;
            this.speedBoost = new EntityAttributeModifier(
                    "radiance_quick_attack_speed_boost",
                    speedBoost,
                    EntityAttributeModifier.Operation.MULTIPLY_BASE
            );
        }

        @Override
        public void start() {
            getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).addTemporaryModifier(speedBoost);
            times = 0;
            restartAttack();
        }

        @Override
        public void tick() {
            if (timer > 8) {
                if (timer % 16 == 0) {
                    offsetVec = genRandomHorizontalVector(2.5D).add(0D, 2D + random.nextDouble() * 2D, 0D);
                }
            } else if (timer == 8) {
                offsetVec = offsetVec.multiply(-2D);
            } else if (timer > 0 && timer < 8) {
                RadianceEntity radiance = RadianceEntity.this;
                List<Entity> hit = world.getEntities(radiance, radiance.getBoundingBox());

                for (Entity entity : hit) {
                    if (entity instanceof PlayerEntity) {
                        entity.damage(DamageSource.mob(radiance), (float) radiance.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
                    }
                }
            } else if (timer <= 0) {
                times++;
                restartAttack();
            }

            timer--;
        }

        private void restartAttack() {
            RadianceEntity radiance = RadianceEntity.this;

            radiance.targetRandomPlayer();
            timer = 64;
            offsetVec = genRandomHorizontalVector(4D).add(0D, 3D, 0D);
        }

        @Override
        public boolean isDone() {
            return times >= maxTimes;
        }

        @Override
        public void end() {
            getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).removeModifier(speedBoost);
        }
    }

    static {
        INVUL_TIMER = DataTracker.registerData(RadianceEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }
}
