package com.kqp.awaken.mixin.item;

import com.kqp.awaken.entity.attribute.RangedWeaponProjectile;
import net.minecraft.entity.Entity;
import net.minecraft.item.BowItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Used to tag bow projectiles.
 */
@Mixin(BowItem.class)
public class BowItemMixin {
    @Redirect(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    public boolean tagBowProjectile(World world, Entity entity) {
        ((RangedWeaponProjectile) entity).setType(RangedWeaponProjectile.Type.BOW);
        return world.spawnEntity(entity);
    }
}
