package com.kqp.awaken.mixin;

import com.kqp.awaken.entity.attribute.RangedWeaponProjectile;
import net.minecraft.entity.Entity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Used to tag crossbow projectiles.
 */
@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
    @Redirect(method = "shoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private static boolean tagCrossbowProjectile(World world, Entity entity) {
        ((RangedWeaponProjectile) entity).setType(RangedWeaponProjectile.Type.CROSSBOW);
        return world.spawnEntity(entity);
    }
}
