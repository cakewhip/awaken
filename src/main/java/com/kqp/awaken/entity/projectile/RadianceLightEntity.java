package com.kqp.awaken.entity.projectile;

import com.kqp.awaken.init.AwakenEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class RadianceLightEntity extends ExplosiveProjectileEntity {
    private RadianceLightEntity(EntityType type, World world) {
        super(type, world);
    }

    public static RadianceLightEntity factory(EntityType type, World world) {
        return new RadianceLightEntity(type, world);
    }

    public RadianceLightEntity(World world, LivingEntity owner, double directionX, double directionY, double directionZ) {
        super(AwakenEntities.RADIANCE_LIGHT, owner, directionX, directionY, directionZ, world);
    }

    @Override
    protected void onEntityHit(EntityHitResult hitRes) {
        super.onEntityHit(hitRes);

        if (!this.world.isClient) {
            Entity target = hitRes.getEntity();
            Entity shooter = this.getOwner();

            boolean damageDealt;
            if (shooter instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) shooter;
                damageDealt = target.damage(DamageSource.magic(this, shooter), 8F);

                if (damageDealt) {
                    this.dealDamage(livingEntity, target);
                }
            } else {
                damageDealt = target.damage(DamageSource.MAGIC, 8F);
            }

            if (damageDealt && target instanceof LivingEntity) {
                int duration = 0;

                if (this.world.getDifficulty() == Difficulty.NORMAL) {
                    duration = 2;
                } else if (this.world.getDifficulty() == Difficulty.HARD) {
                    duration = 3;
                }

                if (duration > 0) {
                    ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 20 * duration, 1));
                }
            }
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        if (!this.world.isClient) {
            Explosion.DestructionType destructionType = this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;

            this.world.createExplosion(this, this.getX(), this.getY(), this.getZ(), 0.1F, true, destructionType);

            this.remove();
        }
    }

    @Override
    public boolean collides() {
        return false;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    protected boolean isBurning() {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }
}
