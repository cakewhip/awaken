package com.kqp.awaken.mixin;

import com.kqp.awaken.entity.attribute.TEntityAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to apply ranged damage buffs.
 */
@Mixin(ProjectileEntity.class)
public class ProjectileEntityMixin {
    @Inject(method = "onEntityHit", at = @At("HEAD"))
    public void overrideProjectileDamage(EntityHitResult entityHitResult, CallbackInfo callbackInfo) {
        PersistentProjectileEntity projectileEntity = (PersistentProjectileEntity) (Object) this;
        if (projectileEntity.getOwner() instanceof LivingEntity) {
            LivingEntity owner = (LivingEntity) projectileEntity.getOwner();

            EntityAttributeInstance attribute = owner.getAttributeInstance(TEntityAttributes.RANGED_DAMAGE);

            if (attribute != null) {
                System.out.println("DMG WAS " + projectileEntity.getDamage());
                attribute.setBaseValue(projectileEntity.getDamage());
                projectileEntity.setDamage(attribute.getValue());
                System.out.println("DMG IS " + projectileEntity.getDamage());

                attribute.setBaseValue(0D);
            }
        }
    }
}
