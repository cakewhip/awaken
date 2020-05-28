package com.kqp.awaken.mixin.client.combat;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Used to cancel attack cool down resets from non-combat related actions.
 */
@Mixin(ClientPlayerInteractionManager.class)
public class NonCombatCooldownCanceller {
    @Redirect(method = "cancelBlockBreaking", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;resetLastAttackedTicks()V"))
    public void cancelResetAttackCooldown(ClientPlayerEntity player) {
    }
}
