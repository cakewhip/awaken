package com.kqp.awaken.mixin.awakening;

import com.kqp.awaken.data.AwakenLevelData;
import com.kqp.awaken.util.Broadcaster;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ElderGuardianEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to listen for deaths to trigger the awakening.
 */
@Mixin(LivingEntity.class)
public abstract class MobDeathListener {
    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeath(DamageSource source, CallbackInfo callbackInfo) {
        LivingEntity entity = (LivingEntity) (Object) this;
        World world = entity.world;

        if (!world.isClient) {
            if (source.getAttacker() instanceof PlayerEntity) {
                AwakenLevelData awakenLevelData = AwakenLevelData.getFor(world.getServer());
                MinecraftServer server = world.getServer();

                if (entity instanceof EnderDragonEntity && !awakenLevelData.isPostDragon()) {
                    awakenLevelData.setPostDragon();
                    Broadcaster.broadcastMessage(server, "The ground shakes beneath you...", Formatting.DARK_RED, false, true);
                } else if (entity instanceof WitherEntity && !awakenLevelData.isPostWither()) {
                    awakenLevelData.setPostWither();
                    Broadcaster.broadcastMessage(server, "Screams echo from below...", Formatting.DARK_RED, false, true);
                } else if (entity instanceof ElderGuardianEntity && !awakenLevelData.isPostElderGuardian()) {
                    awakenLevelData.setPostElderGuardian();
                    Broadcaster.broadcastMessage(server, "A sharp chill goes down your spine...", Formatting.DARK_RED, false, true);
                }
            }
        }
    }
}
