package com.kqp.awaken.mixin.client.input;

import com.kqp.awaken.entity.player.PlayerFlightProperties;
import com.kqp.awaken.init.AwakenNetworking;
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
 * Used to listen for and synchronize jump button presses following the initial jump.
 */
@Mixin(KeyboardInput.class)
public class SecondSpacingListener {
    private boolean previousJumping = false;

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(boolean sneaking, CallbackInfo callbackInfo) {
        KeyboardInput input = (KeyboardInput) (Object) this;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player != null) {
            if (input.jumping != previousJumping) {
                if (!input.jumping || (!player.isOnGround())) {
                    ((PlayerFlightProperties) player).setSecondSpacing(input.jumping);

                    AwakenNetworking.PLAYER_INPUT_SYNC_C2S.sendToServer((buf) -> {
                        buf.writeBoolean(input.jumping);
                    });
                }
            }

            previousJumping = input.jumping;
        }
    }
}
