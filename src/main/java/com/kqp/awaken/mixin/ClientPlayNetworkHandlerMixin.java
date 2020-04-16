package com.kqp.awaken.mixin;

import com.kqp.awaken.data.AwakenLevelDataTagContainer;
import com.kqp.awaken.data.AwakenLevelData;
import com.kqp.awaken.data.AwakenLevelDataContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Shadow
    private MinecraftClient client;

    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void receiveAwakenClientLevelData(GameJoinS2CPacket packet, CallbackInfo callbackInfo) {
        System.out.println("Setting level data from packet");
        CompoundTag awakenLevelDataTag = ((AwakenLevelDataTagContainer) packet).getAwakenLevelDataTag();
        AwakenLevelData awakenLevelData = new AwakenLevelData(awakenLevelDataTag);

        ((AwakenLevelDataContainer) client.world.getLevelProperties()).setAwakenServerLevelData(awakenLevelData);
    }
}
