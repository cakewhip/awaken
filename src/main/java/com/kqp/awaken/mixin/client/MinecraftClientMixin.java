package com.kqp.awaken.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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

        if (client.crosshairTarget != null) {
            if (client.crosshairTarget.getType() == ENTITY) {
                callbackInfo.cancel();

                if (canAttack(client.player)) {
                    client.interactionManager.attackEntity(client.player, ((EntityHitResult) client.crosshairTarget).getEntity());
                    client.player.swingHand(Hand.MAIN_HAND);
                }
            }
        }
    }

    @Redirect(method = "doAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;resetLastAttackedTicks()V"))
    public void cancelResetAttackCooldown(ClientPlayerEntity player) {
        MinecraftClient client = (MinecraftClient) (Object) this;

        if (client.crosshairTarget != null) {
            if (client.crosshairTarget.getType() == ENTITY) {
                if (canAttack(client.player)) {
                    client.player.resetLastAttackedTicks();
                }
            }
        }
    }

    private static boolean canAttack(PlayerEntity player) {
        return player.getAttackCooldownProgress(0.0F) >= 1.0F;
    }
}
