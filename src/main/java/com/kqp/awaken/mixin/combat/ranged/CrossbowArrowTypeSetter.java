package com.kqp.awaken.mixin.combat.ranged;

import com.kqp.awaken.entity.attribute.RangedWeaponProjectile;
import com.kqp.awaken.item.crossbow.AwakenCrossbowItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Used to modify crossbow projectiles.
 */
@Mixin(CrossbowItem.class)
public class CrossbowArrowTypeSetter {
    @Redirect(method = "createArrow", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ArrowItem;createArrow(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;"))
    private static PersistentProjectileEntity modifyArrow(ArrowItem arrowItem, World world, ItemStack stack, LivingEntity shooter, World world2, LivingEntity entity, ItemStack crossbow, ItemStack arrow) {
        PersistentProjectileEntity projectileEntity = arrowItem.createArrow(world, stack, shooter);

        if (crossbow.getItem() instanceof AwakenCrossbowItem) {
            AwakenCrossbowItem bowItem = (AwakenCrossbowItem) crossbow.getItem();
            bowItem.modifyProjectileEntity(projectileEntity);
        }

        ((RangedWeaponProjectile) projectileEntity).setType(RangedWeaponProjectile.Type.CROSSBOW);

        return projectileEntity;
    }
}
