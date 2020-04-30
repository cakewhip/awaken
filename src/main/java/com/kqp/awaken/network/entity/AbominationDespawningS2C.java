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

import java.util.Random;

public class AbominationDespawningS2C extends AwakenPacketS2C {
    public AbominationDespawningS2C() {
        super("abomination_despawning_s2c");
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
            Random r = world.random;

            for (int i = 0; i < 3; i++) {
                world.addParticle(ParticleTypes.LAVA,
                        entity.getX() + 2 * (r.nextDouble() - r.nextDouble()),
                        entity.getY(),
                        entity.getZ() + 2 * (r.nextDouble() - r.nextDouble()),
                        r.nextDouble() - r.nextDouble(), r.nextDouble(), r.nextDouble() - r.nextDouble()
                );
            }

            for (int i = 0; i < 3; i++) {
                world.addParticle(ParticleTypes.LARGE_SMOKE,
                        entity.getX() + 2 * (r.nextDouble() - r.nextDouble()),
                        entity.getY(),
                        entity.getZ() + 2 * (r.nextDouble() - r.nextDouble()),
                        0, r.nextDouble(), 0
                );
            }
        });
    }
}
