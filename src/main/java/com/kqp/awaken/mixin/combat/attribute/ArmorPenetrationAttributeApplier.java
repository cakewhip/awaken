package com.kqp.awaken.mixin.combat.attribute;

import com.kqp.awaken.init.AwakenEntityAttributes;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to apply armor penetration attribute.
 */
@Mixin(LivingEntity.class)
public class ArmorPenetrationAttributeApplier {
    @Inject(method = "applyArmorToDamage", at = @At("HEAD"), cancellable = true)
    private void applyArmorPenetration(DamageSource source, float amount, CallbackInfoReturnable<Float> callbackInfo) {
        if (!source.bypassesArmor()) {
            if (source.getAttacker() instanceof PlayerEntity) {
                LivingEntity target = (LivingEntity) (Object) this;
                float damageMitigated = amount - DamageUtil.getDamageLeft(amount, (float) target.getArmor(), (float) target.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS));

                PlayerEntity player = (PlayerEntity) source.getAttacker();
                EntityAttributeInstance armorPenAttrib = player.getAttributeInstance(AwakenEntityAttributes.ARMOR_PENETRATION);

                damageMitigated *= (1D - armorPenAttrib.getValue());

                amount -= damageMitigated;

                callbackInfo.setReturnValue(amount);
            }
        }
    }
}
