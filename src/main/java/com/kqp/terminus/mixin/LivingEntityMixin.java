package com.kqp.terminus.mixin;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.util.Broadcaster;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(at = @At("HEAD"), method = "onDeath")
    public void onDeath(DamageSource source, CallbackInfo callbackInfo) {
        LivingEntity le = (LivingEntity) (Object) this;
        World world = le.world;

        if (!world.isClient) {
            Broadcaster broadcaster = new Broadcaster();

            if (source.getAttacker() instanceof PlayerEntity) {
                if (le instanceof CowEntity && !Terminus.worldProperties.isPostDragon()) {
                    Terminus.worldProperties.setPostDragon();
                    broadcaster.broadcastMessage("The ground shakes beneath you...", Formatting.DARK_RED, false, true);
                } else if (le instanceof SheepEntity && !Terminus.worldProperties.isPostWither()) {
                    Terminus.worldProperties.setPostWither();
                    broadcaster.broadcastMessage("Screams echo from below...", Formatting.DARK_RED, false, true);
                } else if (le instanceof PigEntity && !Terminus.worldProperties.isPostElderGuardian()) {
                    Terminus.worldProperties.setPostElderGuardian();
                    broadcaster.broadcastMessage("A sharp chill goes down your spine...", Formatting.DARK_RED, false, true);
                } else if (le instanceof ZombieEntity && !Terminus.worldProperties.isPostRaid()) {
                    Terminus.worldProperties.setPostRaid();
                    broadcaster.broadcastMessage("A distant figure fades into the shadows...", Formatting.DARK_RED, false, true);
                }
            }
        }
    }
}
