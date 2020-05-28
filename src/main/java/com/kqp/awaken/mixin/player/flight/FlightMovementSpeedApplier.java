package com.kqp.awaken.mixin.player.flight;

import com.kqp.awaken.entity.player.PlayerFlightProperties;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to override movement speed while flying.
 */
@Mixin(LivingEntity.class)
public abstract class FlightMovementSpeedApplier {
    @Inject(method = "getMovementSpeed", at = @At("HEAD"), cancellable = true)
    private void overideFlyingMovementSpeed(float slipperiness, CallbackInfoReturnable<Float> callbackInfo) {
        LivingEntity entity = ((LivingEntity) (Object) this);

        if (this instanceof PlayerFlightProperties) {
            PlayerFlightProperties flightProperties = (PlayerFlightProperties) entity;

            if (flightProperties.canFly() && !entity.isOnGround()) {
                callbackInfo.setReturnValue((float) (entity.getMovementSpeed() * 0.28663526131445843));
            }
        }
    }
}
