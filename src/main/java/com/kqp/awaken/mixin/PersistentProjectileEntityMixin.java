package com.kqp.awaken.mixin;

import com.kqp.awaken.entity.attribute.AwakenEntityAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to apply ranged damage buffs.
 */
@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin {
    @Inject(method = "onEntityHit", at = @At("HEAD"))
    public void overrideProjectileDamage(EntityHitResult entityHitResult, CallbackInfo callbackInfo) {
        PersistentProjectileEntity projectileEntity = (PersistentProjectileEntity) (Object) this;
        if (projectileEntity.getOwner() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) projectileEntity.getOwner();

            EntityAttributeInstance attribute = player.getAttributeInstance(AwakenEntityAttributes.RANGED_DAMAGE);

            if (attribute != null) {
                attribute.setBaseValue(projectileEntity.getDamage());
                projectileEntity.setDamage(attribute.getValue());

                attribute.setBaseValue(0D);
            }
        }
    }
}
