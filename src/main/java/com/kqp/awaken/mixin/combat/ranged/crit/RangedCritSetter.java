package com.kqp.awaken.mixin.combat.ranged.crit;

import com.kqp.awaken.init.AwakenEntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to set whether a projectile is critical or not, depending on the player's crit chance attribute.
 */
@Mixin(PersistentProjectileEntity.class)
public class RangedCritSetter {
    @Inject(method = "<init>*", at = @At("RETURN"))
    private void applyCritChance(CallbackInfo callbackInfo) {
        PersistentProjectileEntity projectile = (PersistentProjectileEntity) (Object) this;

        if (projectile.getOwner() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) projectile.getOwner();
            double critChance = player.getAttributeValue(AwakenEntityAttributes.CRIT_CHANCE);

            if (Math.random() < critChance) {
                projectile.setCritical(true);
            }
        }
    }
}
