package com.kqp.awaken.mixin.combat.ranged.crit;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Used to apply critical damage to projectiles.
 */
@Mixin(PersistentProjectileEntity.class)
public class RangedCritApplier {
    @Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean applyCritDamage(Entity target, DamageSource source, float damage) {
        PersistentProjectileEntity projectile = (PersistentProjectileEntity) (Object) this;

        if (projectile.isCritical()) {
            damage = (float) ((damage * 1.5D) + (Math.random() * damage * 0.25D));
        }

        return target.damage(source, damage);
    }
}
