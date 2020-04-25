package com.kqp.awaken.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.util.hit.HitResult.Type.ENTITY;

/**
 * Used to cancel entity attacks if the cool down is still in progress.
 */
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    protected int attackCooldown;

    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void doAttack(CallbackInfo callbackInfo) {
        MinecraftClient client = (MinecraftClient) (Object) this;
        if (this.attackCooldown <= 0) {
            if (client.crosshairTarget != null && !client.player.isRiding()) {
                if (client.crosshairTarget.getType() == ENTITY) {
                    if (!canAttack(client.player)) {
                        callbackInfo.cancel();
                    }
                }
            }
        }
    }

    private static boolean canAttack(PlayerEntity player) {
        return player.getAttackCooldownProgress(0.0F) >= 1.0F;
    }
}
