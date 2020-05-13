package com.kqp.awaken.network.entity.player;

import com.kqp.awaken.entity.player.PlayerFlyingInfo;
import com.kqp.awaken.network.AwakenPacketC2S;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.network.PacketByteBuf;

public class PlayerInputSyncC2S extends AwakenPacketC2S {
    public PlayerInputSyncC2S() {
        super("player_input_sync");
    }

    @Override
    public void accept(PacketContext context, PacketByteBuf data) {
        boolean jumping = data.readBoolean();

        context.getTaskQueue().execute(() -> {
            ((PlayerFlyingInfo) context.getPlayer()).setSecondSpacing(jumping);
        });
    }
}
