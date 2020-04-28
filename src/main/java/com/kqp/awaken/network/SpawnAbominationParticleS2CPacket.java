package com.kqp.awaken.network;

import com.kqp.awaken.entity.AbominationEntity;
import com.kqp.awaken.init.AwakenNetworking;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class SpawnAbominationParticleS2CPacket {
    public static void send(AbominationEntity abomination) {
        System.out.println("Sending");

        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(abomination.getEntityId());

        for (ServerPlayerEntity player : abomination.bossBar.getPlayers()) {
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, AwakenNetworking.SPAWN_ABOMINATION_SMASH_PARTICLE_S2C_ID, buf);
            System.out.println("SENT");
        }
    }

    public static void accept(PacketContext context, PacketByteBuf data) {
        int id = data.readInt();
        System.out.println("Received");

        context.getTaskQueue().execute(() -> {
            World world = MinecraftClient.getInstance().world;

            Entity entity = world.getEntityById(id);
            world.addParticle(ParticleTypes.EXPLOSION_EMITTER, entity.getX(), entity.getY() + 0.1, entity.getZ(), 1.0D, 0.0D, 0.0D);
            System.out.println(entity.getPos());
            System.out.println("Spawned");
        });
    }
}
