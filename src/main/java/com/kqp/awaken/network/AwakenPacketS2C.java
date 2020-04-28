package com.kqp.awaken.network;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.Consumer;

public abstract class AwakenPacketS2C extends AwakenPacket {
    public AwakenPacketS2C(String name) {
        super(name);
    }

    public void sendToPlayer(ServerPlayerEntity player, PacketByteBuf buf) {
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, this.id, buf);
    }

    public void sendToPlayer(ServerPlayerEntity player, Consumer<PacketByteBuf> dataConsumer) {
        PacketByteBuf buf = buf();
        dataConsumer.accept(buf);

        sendToPlayer(player, buf);
    }

    public void sendEmptyToPlayer(ServerPlayerEntity player) {
        sendToPlayer(player, buf());
    }
}
