package com.kqp.awaken.mixin;

import com.kqp.awaken.data.AwakenLevelDataContainer;
import com.kqp.awaken.data.AwakenLevelDataTagContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Redirect(method = "onPlayerConnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V"))
    public void fillAwakenLevelData(ServerPlayNetworkHandler networkHandler, Packet<?> packet) {
        if (packet instanceof GameJoinS2CPacket) {
            CompoundTag awakenLevelDataTag = new CompoundTag();
            ((AwakenLevelDataContainer) networkHandler.player.world.getLevelProperties()).getAwakenLevelData().writeToTag(awakenLevelDataTag);

            ((AwakenLevelDataTagContainer) packet).setAwakenLevelDataTag(awakenLevelDataTag);
        }

        networkHandler.sendPacket(packet);
    }
}
