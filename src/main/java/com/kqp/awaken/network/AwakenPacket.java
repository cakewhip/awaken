package com.kqp.awaken.network;

import com.kqp.awaken.init.Awaken;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public abstract class AwakenPacket {
    public final Identifier id;

    public AwakenPacket(String name) {
        this.id = new Identifier(Awaken.MOD_ID, name);
    }

    public abstract void accept(PacketContext context, PacketByteBuf data);

    public static PacketByteBuf buf() {
        return new PacketByteBuf(Unpooled.buffer());
    }
}
