package com.kqp.awaken.mixin.combat.ranged;

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
 * Used to set the owner of projectiles from crossbows.
 */
@Mixin(CrossbowItem.class)
public class CrossbowArrowOwnerSetter {
    @Inject(method = "createArrow", at = @At("RETURN"))
    private static void setProjectileOwner(World world,
                                           LivingEntity entity,
                                           ItemStack crossbow,
                                           ItemStack arrow,
                                           CallbackInfoReturnable<PersistentProjectileEntity> callbackInfo) {
        callbackInfo.getReturnValue().setOwner(entity);
    }
}
