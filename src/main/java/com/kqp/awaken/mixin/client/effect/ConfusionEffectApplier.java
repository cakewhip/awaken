package com.kqp.awaken.mixin.client.effect;

import com.kqp.awaken.entity.effect.AwakenStatusEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.GameOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to implement the confusion status effect.
 */
@Mixin(value = KeyboardInput.class, priority = 100)
public class ConfusionEffectApplier {
    @Shadow
    @Final
    private GameOptions settings;

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(boolean sneaking, CallbackInfo callbackInfo) {
        KeyboardInput input = (KeyboardInput) (Object) this;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player != null) {
            if (player.hasStatusEffect(AwakenStatusEffects.CONFUSION)) {

                input.pressingForward = settings.keyBack.isPressed();
                input.pressingBack = settings.keyForward.isPressed();
                input.pressingLeft = settings.keyRight.isPressed();
                input.pressingRight = settings.keyLeft.isPressed();

                input.movementForward = input.pressingForward == input.pressingBack ? 0.0F : (input.pressingForward ? 1.0F : -1.0F);
                input.movementSideways = input.pressingLeft == input.pressingRight ? 0.0F : (input.pressingLeft ? 1.0F : -1.0F);

                input.jumping = settings.keySneak.isPressed();
                input.sneaking = settings.keyJump.isPressed();

                if (sneaking) {
                    input.movementSideways = (float) (input.movementSideways * 0.3D);
                    input.movementForward = (float) (input.movementForward * 0.3D);
                }
            }
        }
    }
}
