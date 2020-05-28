package com.kqp.awaken.mixin.combat.ranged;

import com.kqp.awaken.entity.attribute.RangedWeaponProjectile;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Used to implement the RangedWeaponProjectile interface.
 */
@Mixin(PersistentProjectileEntity.class)
public class ProjectileTypeImplementer implements RangedWeaponProjectile {
    public Type rangedWeaponType = Type.NONE;

    @Override
    public Type getType() {
        return rangedWeaponType;
    }

    @Override
    public void setType(Type rangedWeaponType) {
        this.rangedWeaponType = rangedWeaponType;
    }
}
