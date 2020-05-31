package com.kqp.awaken.mixin.trinket;

import com.kqp.awaken.init.AwakenEntityAttributes;
import com.kqp.awaken.util.AttributeUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to apply the shock-wave shield effect.
 */
@Mixin(LivingEntity.class)
public abstract class ShockwaveShieldEffectApplier {
    @Inject(method = "takeShieldHit", at = @At("HEAD"), cancellable = true)
    private void applyShockwaveShieldEffect(LivingEntity attacker, CallbackInfo callbackInfo) {
        if ((Object) this instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) (Object) this;
            EntityAttributeInstance shieldKnockack = player.getAttributeInstance(AwakenEntityAttributes.SHIELD_KNOCKBACK);

            attacker.takeKnockback((float) AttributeUtil.applyAttribute(shieldKnockack, 0.5D), player.getX() - attacker.getX(), player.getZ() - attacker.getZ());

            callbackInfo.cancel();
        }
    }
}
