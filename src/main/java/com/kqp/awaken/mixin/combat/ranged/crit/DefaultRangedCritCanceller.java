package com.kqp.awaken.mixin.combat.ranged.crit;

import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Used to stop the projectile entity from applying the vanilla critical damage calculations.
 */
@Mixin(PersistentProjectileEntity.class)
public class DefaultRangedCritCanceller {
    @Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;isCritical()Z"))
    private boolean cancelDefaultCritBehavior(PersistentProjectileEntity projectile) {
        return false;
    }
}
