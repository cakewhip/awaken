package com.kqp.awaken.mixin.server.network;

import com.kqp.awaken.entity.player.PlayerFlightProperties;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Redirect(method = "onPlayerMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;isInTeleportationState()Z"))
    public boolean redirectTeleportationState(ServerPlayerEntity player) {
        PlayerFlightProperties flightProperties = (PlayerFlightProperties) player;

        return player.isInTeleportationState() || (flightProperties.canFly() && !player.isOnGround());
    }
}
