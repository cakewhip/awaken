package com.kqp.awaken.mixin.combat.ranged.crit;

import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Used to stop the crossbow item from setting the critical flag in the projectile entity.
 */
@Mixin(CrossbowItem.class)
public class DefaultCrossbowCritCanceller {
    @Redirect(method = "createArrow", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setCritical(Z)V"))
    private static void cancelCritSet(PersistentProjectileEntity projectile, boolean critical) {
    }
}
