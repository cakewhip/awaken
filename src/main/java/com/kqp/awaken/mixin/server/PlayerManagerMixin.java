package com.kqp.awaken.mixin.server;

import com.kqp.awaken.data.AwakenLevelData;
import com.kqp.awaken.data.AwakenLevelDataTagContainer;
import com.kqp.awaken.recipe.AwakenRecipeManager;
import com.kqp.awaken.recipe.SyncAwakenRecipes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to:
 * Sync recipe data with clients.
 * Sync level data with clients.
 */
@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(method = "onPlayerConnect", at = @At("RETURN"))
    public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo callbackInfo) {
        AwakenRecipeManager awakenRecipeManager = AwakenRecipeManager.getFor(player.world);
        SyncAwakenRecipes.syncToPlayer(awakenRecipeManager.getRecipes(), player);
    }

    @Redirect(method = "onPlayerConnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V"))
    public void fillAwakenLevelData(ServerPlayNetworkHandler networkHandler, Packet<?> packet) {
        if (packet instanceof GameJoinS2CPacket) {
            CompoundTag awakenLevelDataTag = new CompoundTag();
            AwakenLevelData.getFor(networkHandler.player.world).writeToTag(awakenLevelDataTag);

            ((AwakenLevelDataTagContainer) packet).setAwakenLevelDataTag(awakenLevelDataTag);
        }

        networkHandler.sendPacket(packet);
    }
}
