package com.kqp.awaken.mixin.combat.ranged;

import com.kqp.awaken.entity.attribute.RangedWeaponProjectile;
import com.kqp.awaken.entity.attribute.RangedWeaponProjectile.Type;
import com.kqp.awaken.init.AwakenEntityAttributes;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.kqp.awaken.entity.attribute.RangedWeaponProjectile.Type;

/**
 * Used to apply ranged damage buffs.
 */
@Mixin(PersistentProjectileEntity.class)
public class ProjectileDamageSetter {
    @Inject(method = "onEntityHit", at = @At("HEAD"))
    public void overrideProjectileDamage(EntityHitResult entityHitResult, CallbackInfo callbackInfo) {
        PersistentProjectileEntity projectileEntity = (PersistentProjectileEntity) (Object) this;
        double damage = projectileEntity.getDamage();

        if (projectileEntity.getOwner() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) projectileEntity.getOwner();

            EntityAttributeInstance attribute = player.getAttributeInstance(AwakenEntityAttributes.RANGED_DAMAGE);
            attribute.setBaseValue(damage);
            damage = attribute.getValue();
            attribute.setBaseValue(0D);

            Type rangedWeaponType = ((RangedWeaponProjectile) this).getType();

            switch (rangedWeaponType) {
                case BOW:
                    attribute = player.getAttributeInstance(AwakenEntityAttributes.BOW_DAMAGE);
                    break;
                case CROSSBOW:
                    attribute = player.getAttributeInstance(AwakenEntityAttributes.CROSSBOW_DAMAGE);
                    break;
                case NONE:
                    attribute = null;
                    break;
            }

            if (attribute != null) {
                attribute.setBaseValue(damage);
                damage = attribute.getValue();
                attribute.setBaseValue(0D);
            }
        }

        projectileEntity.setDamage(damage);
    }
}
