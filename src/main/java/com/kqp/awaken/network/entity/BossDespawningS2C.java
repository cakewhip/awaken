package com.kqp.awaken.network.entity;

import com.kqp.awaken.entity.mob.AwakenBossEntity;
import com.kqp.awaken.network.AwakenPacketS2C;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Random;

public class BossDespawningS2C extends AwakenPacketS2C {
    public BossDespawningS2C() {
        super("boss_despawning_s2c");
    }

    public void send(AwakenBossEntity boss) {
        for (ServerPlayerEntity player : boss.bossBar.getPlayers()) {
            this.sendToPlayer(player, (buf) -> {
                buf.writeInt(boss.getEntityId());
            });
        }
    }

    @Override
    public void accept(PacketContext context, PacketByteBuf data) {
        int id = data.readInt();

        context.getTaskQueue().execute(() -> {
            despawn(id);
        });
    }

    @Environment(EnvType.CLIENT)
    private static void despawn(int id) {
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
    }
}
