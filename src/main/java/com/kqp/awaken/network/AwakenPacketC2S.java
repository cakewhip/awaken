package com.kqp.awaken.network;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.network.PacketByteBuf;

import java.util.function.Consumer;

public abstract class AwakenPacketC2S extends AwakenPacket {
    public AwakenPacketC2S(String name) {
        super(name);
    }

    public void sendToServer(PacketByteBuf buf) {
        ClientSidePacketRegistry.INSTANCE.sendToServer(this.id, buf);
    }

    public void sendToServer(Consumer<PacketByteBuf> dataConsumer) {
        PacketByteBuf buf = buf();
        dataConsumer.accept(buf);

        sendToServer(buf);
    }

    public void sendEmptyToServer() {
        sendToServer(buf());
    }
}
