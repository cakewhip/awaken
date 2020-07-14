package com.kqp.awaken.network.entity;

import com.kqp.awaken.network.AwakenPacketS2C;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

import java.util.UUID;

public class SpawnEntityPacketS2C extends AwakenPacketS2C {
    public SpawnEntityPacketS2C() {
        super("spawn_entity_packet_s2c");
    }

    public void send(Entity entity) {
        PacketByteBuf buf = buf();

        buf.writeVarInt(Registry.ENTITY_TYPE.getRawId(entity.getType()));
        buf.writeUuid(entity.getUuid());
        buf.writeVarInt(entity.getEntityId());
        buf.writeDouble(entity.getX());
        buf.writeDouble(entity.getY());
        buf.writeDouble(entity.getZ());
        buf.writeByte(MathHelper.floor(entity.pitch * 256.0F / 360.0F));
        buf.writeByte(MathHelper.floor(entity.yaw * 256.0F / 360.0F));
        buf.writeFloat(entity.pitch);
        buf.writeFloat(entity.yaw);

        for (PlayerEntity player : entity.getEntityWorld().getPlayers()) {
            this.sendToPlayer((ServerPlayerEntity) player, buf);
        }
    }

    @Override
    public void accept(PacketContext context, PacketByteBuf data) {
        EntityType<?> type = Registry.ENTITY_TYPE.get(data.readVarInt());

        UUID entityUUID = data.readUuid();
        int entityID = data.readVarInt();

        double x = data.readDouble();
        double y = data.readDouble();
        double z = data.readDouble();

        float pitch = (data.readByte() * 360) / 256.0F;
        float yaw = (data.readByte() * 360) / 256.0F;

        context.getTaskQueue().execute(() -> {
            spawnEntity(type, entityUUID, entityID, x, y, z, pitch, yaw);
        });
    }

    @Environment(EnvType.CLIENT)
    private static void spawnEntity(EntityType type, UUID entityUUID, int entityID, double x, double y, double z, float pitch, float yaw) {
        ClientWorld world = MinecraftClient.getInstance().world;
        Entity entity = type.create(world);

        if (entity != null) {
            entity.updatePosition(x, y, z);
            entity.updateTrackedPosition(x, y, z);

            entity.pitch = pitch;
            entity.yaw = yaw;

            entity.setEntityId(entityID);
            entity.setUuid(entityUUID);

            world.addEntity(entityID, entity);
        }
    }
}
