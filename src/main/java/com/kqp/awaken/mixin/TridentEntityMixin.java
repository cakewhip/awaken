package com.kqp.awaken.mixin;

import com.kqp.awaken.entity.attribute.AwakenEntityAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Used to apply the trident attribute modifier.
 */
@Mixin(TridentEntity.class)
public class TridentEntityMixin {
    @Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    public boolean applyTridentAttribute(Entity target, DamageSource damageSource, float damage) {
        TridentEntity trident = (TridentEntity) (Object) this;

        if (trident.getOwner() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) trident.getOwner();

            EntityAttributeInstance attribute = player.getAttributeInstance(AwakenEntityAttributes.TRIDENT_DAMAGE);
            attribute.setBaseValue(damage);
            damage = (float) attribute.getValue();
            attribute.setBaseValue(0D);
        }

        return target.damage(damageSource, damage);
    }
}
