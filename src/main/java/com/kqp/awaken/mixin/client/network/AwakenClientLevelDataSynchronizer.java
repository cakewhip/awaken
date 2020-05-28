package com.kqp.awaken.mixin.client.network;

import com.kqp.awaken.client.AwakenClientLevelData;
import com.kqp.awaken.data.AwakenLevelData;
import com.kqp.awaken.data.AwakenLevelDataTagContainer;
import com.kqp.awaken.recipe.AwakenRecipeManager;
import com.kqp.awaken.recipe.AwakenRecipeManagerProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to synchronize Awaken level data from the server.
 */
@Mixin(ClientPlayNetworkHandler.class)
public class AwakenClientLevelDataSynchronizer {
    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void receiveAwakenClientLevelData(GameJoinS2CPacket packet, CallbackInfo callbackInfo) {
        CompoundTag awakenLevelDataTag = ((AwakenLevelDataTagContainer) packet).getAwakenLevelDataTag();
        AwakenLevelData awakenLevelData = new AwakenLevelData(awakenLevelDataTag);

        AwakenClientLevelData.INSTANCE.setAwakenServerLevelData(awakenLevelData);
    }
}
