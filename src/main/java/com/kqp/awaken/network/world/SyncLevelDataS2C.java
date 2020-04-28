package com.kqp.awaken.network.world;

import com.kqp.awaken.data.AwakenLevelData;
import com.kqp.awaken.data.AwakenLevelDataContainer;
import com.kqp.awaken.network.AwakenPacketS2C;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

public class SyncLevelDataS2C extends AwakenPacketS2C {
    public SyncLevelDataS2C() {
        super("sync_level_data_s2c");
    }

    @Override
    public void accept(PacketContext context, PacketByteBuf data) {
        CompoundTag awakenLevelDataTag = data.readCompoundTag();

        context.getTaskQueue().execute(() -> {
            ((AwakenLevelDataContainer) MinecraftClient.getInstance().world.getLevelProperties()).setAwakenServerLevelData(new AwakenLevelData(awakenLevelDataTag));
        });
    }
}
