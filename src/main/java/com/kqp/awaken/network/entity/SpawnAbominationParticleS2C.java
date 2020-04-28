package com.kqp.awaken.network.entity;

import com.kqp.awaken.entity.AbominationEntity;
import com.kqp.awaken.network.AwakenPacketS2C;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class SpawnAbominationParticleS2C extends AwakenPacketS2C {
    public SpawnAbominationParticleS2C() {
        super("spawn_abomination_particles_s2c");
    }

    public void send(AbominationEntity abomination) {
        for (ServerPlayerEntity player : abomination.bossBar.getPlayers()) {
            this.sendToPlayer(player, (buf) -> {
                buf.writeInt(abomination.getEntityId());
            });
        }
    }

    @Override
    public void accept(PacketContext context, PacketByteBuf data) {
        int id = data.readInt();

        context.getTaskQueue().execute(() -> {
            World world = MinecraftClient.getInstance().world;

            Entity entity = world.getEntityById(id);
            world.addParticle(ParticleTypes.EXPLOSION_EMITTER, entity.getX(), entity.getY() + 0.1, entity.getZ(), 1.0D, 0.0D, 0.0D);
        });
    }
}
