package com.kqp.awaken.mixin.item;

import com.kqp.awaken.entity.attribute.RangedWeaponProjectile;
import com.kqp.awaken.item.bow.AwakenBowItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Used to modify bow projectiles.
 */
@Mixin(BowItem.class)
public class BowItemMixin {
    @Redirect(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ArrowItem;createArrow(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;"))
    public PersistentProjectileEntity modifyArrow(ArrowItem arrowItem, World world, ItemStack stack, LivingEntity shooter) {
        PersistentProjectileEntity projectileEntity = arrowItem.createArrow(world, stack, shooter);

        if ((Object) this instanceof AwakenBowItem) {
            AwakenBowItem bowItem = (AwakenBowItem) (Object) this;
            bowItem.modifyProjectileEntity(projectileEntity);
        }

        ((RangedWeaponProjectile) projectileEntity).setType(RangedWeaponProjectile.Type.BOW);

        return projectileEntity;
    }
}
