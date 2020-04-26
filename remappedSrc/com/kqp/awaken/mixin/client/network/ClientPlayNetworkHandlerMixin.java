package com.kqp.awaken.mixin.client.network;

import com.kqp.awaken.data.AwakenLevelData;
import com.kqp.awaken.data.AwakenLevelDataContainer;
import com.kqp.awaken.data.AwakenLevelDataTagContainer;
import com.kqp.awaken.recipe.AwakenRecipeManager;
import com.kqp.awaken.recipe.AwakenRecipeManagerProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.recipe.RecipeManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to:
 * Receive Awaken level data from the server.
 * Initialize the Awaken recipe manager.
 */
@Mixin(ClientPlayNetworkHandler.class)
@Implements(@Interface(iface = AwakenRecipeManagerProvider.class, prefix = "vw$"))
public class ClientPlayNetworkHandlerMixin implements AwakenRecipeManagerProvider {
    @Shadow
    private MinecraftClient client;

    private AwakenRecipeManager awakenRecipeManager;

    @Inject(method = "<init>*", at = @At("RETURN"))
    public void construct(CallbackInfo callbackInfo) {
        this.awakenRecipeManager = new AwakenRecipeManager();
    }

    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void receiveAwakenClientLevelData(GameJoinS2CPacket packet, CallbackInfo callbackInfo) {
        CompoundTag awakenLevelDataTag = ((AwakenLevelDataTagContainer) packet).getAwakenLevelDataTag();
        AwakenLevelData awakenLevelData = new AwakenLevelData(awakenLevelDataTag);

        ((AwakenLevelDataContainer) client.world.getLevelProperties()).setAwakenServerLevelData(awakenLevelData);
    }

    @Override
    public AwakenRecipeManager getAwakenRecipeManager() {
        return this.awakenRecipeManager;
    }

    @Override
    public void setAwakenRecipeManager(AwakenRecipeManager awakenRecipeManager) {
        this.awakenRecipeManager = awakenRecipeManager;
    }
}
