package com.kqp.awaken.mixin;

import com.kqp.awaken.entity.attribute.AwakenEntityAttributes;
import com.kqp.awaken.entity.attribute.RangedWeaponProjectile;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to:
 * Apply ranged damage buffs.
 * Implement {@link RangedWeaponProjectile} to distinguish between bows and crossbows.
 */
@Mixin(PersistentProjectileEntity.class)
@Implements(@Interface(iface = RangedWeaponProjectile.class, prefix = "vw$"))
public class PersistentProjectileEntityMixin implements RangedWeaponProjectile {
    public Type rangedWeaponType = Type.NONE;

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

    @Override
    public Type getType() {
        return rangedWeaponType;
    }

    @Override
    public void setType(Type rangedWeaponType) {
        this.rangedWeaponType = rangedWeaponType;
    }
}
