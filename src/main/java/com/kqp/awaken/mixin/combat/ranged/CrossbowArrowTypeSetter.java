package com.kqp.awaken.mixin.combat.ranged;

import com.kqp.awaken.entity.attribute.RangedWeaponProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to modify crossbow projectiles.
 */
@Mixin(CrossbowItem.class)
public class CrossbowArrowTypeSetter {
    @Inject(method = "createArrow", at = @At("RETURN"))
    private static void setProjectileType(World world,
                                          LivingEntity entity,
                                          ItemStack crossbow,
                                          ItemStack arrow,
                                          CallbackInfoReturnable<PersistentProjectileEntity> callbackInfo) {
        ((RangedWeaponProjectile) callbackInfo.getReturnValue()).setType(RangedWeaponProjectile.Type.CROSSBOW);
    }
}
