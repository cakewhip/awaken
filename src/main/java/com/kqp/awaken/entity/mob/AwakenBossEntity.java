package com.kqp.awaken.entity.mob;

import com.kqp.awaken.init.AwakenConfig;
import com.kqp.awaken.init.AwakenNetworking;
import jdk.internal.jline.internal.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AwakenBossEntity extends HostileEntity {
    public final ServerBossBar bossBar;

    private int despawnTickTime;
    protected int regenAmt, regenCoolDown;

    protected int regenTimer;

    public AwakenBossEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);

        this.bossBar = new ServerBossBar(this.getDisplayName(), BossBar.Color.YELLOW, BossBar.Style.PROGRESS);
        this.setHealth(this.getMaxHealth());
        this.getNavigation().setCanSwim(true);
    }

    @Override
    public void baseTick() {
        super.baseTick();

        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());

        if (this.shouldRemove()) {
            this.remove();
        } else if (this.isDespawning()) {
            this.despawnTickTime++;

            this.setOnFireFor(30);

            if (this.despawnTickTime % 5 == 0) {
                AwakenNetworking.BOSS_DESPAWNING_S2C.send(this);
            }
        }

        if (regenTimer == 0) {
            this.heal(regenAmt);
            regenTimer = regenCoolDown;
        } else {
            regenTimer = Math.max(0, regenTimer - 1);
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        this.regenTimer = regenCoolDown;

        return super.damage(source, amount);
    }

    @Override
    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);

        this.bossBar.setName(this.getDisplayName());
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);

        this.bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);

        this.bossBar.removePlayer(player);
    }

    @Override
    public void slowMovement(BlockState state, Vec3d multiplier) {
    }

    @Override
    protected boolean canStartRiding(Entity entity) {
        return false;
    }

    @Override
    public boolean canUsePortals() {
        return false;
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
        return false;
    }

    public boolean isDespawning() {
        long time = world.getTimeOfDay() % 24000;

        return time > AwakenConfig.BOSS_DESPAWN_TIME_START;
    }

    public boolean shouldRemove() {
        long time = world.getTimeOfDay() % 24000;

        return time < AwakenConfig.VALID_BOSS_TIME_START || time > AwakenConfig.VALID_BOSS_TIME_END;
    }

    public boolean aboveHalfHealth() {
        return (this.getHealth() / this.getMaxHealth()) >= 0.5;
    }
}
