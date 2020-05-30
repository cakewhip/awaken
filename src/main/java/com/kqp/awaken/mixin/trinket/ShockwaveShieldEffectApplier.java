package com.kqp.awaken.mixin.trinket;

import com.kqp.awaken.init.AwakenItems;
import com.kqp.awaken.util.EquipmentUtil;
import net.minecraft.entity.LivingEntity;
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
            if (EquipmentUtil.hasTrinket(player, AwakenItems.Trinkets.SHOCKWAVE_SHIELD)) {
                if (player.getRandom().nextFloat() < 0.75F) {
                    attacker.takeKnockback(0.5F * 1.5F, player.getX() - attacker.getX(), player.getZ() - attacker.getZ());

                    callbackInfo.cancel();
                }
            }
        }
    }
}
