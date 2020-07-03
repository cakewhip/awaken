package com.kqp.awaken.mixin.ability;

import com.kqp.awaken.init.AwakenAbilities;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class NoFallDamageAbilityApplier {
    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    private void handleFallDamage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Boolean> callbackInfo) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (AwakenAbilities.NO_FALL_DAMAGE.get(player).flag) {
            callbackInfo.setReturnValue(false);
        }
    }
}
