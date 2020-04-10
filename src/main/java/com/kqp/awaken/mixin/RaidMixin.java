package com.kqp.awaken.mixin;

import com.kqp.awaken.init.Awaken;
import com.kqp.awaken.util.Broadcaster;
import net.minecraft.entity.raid.Raid;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to detect when the raid has been defeated.
 */
@Mixin(Raid.class)
public abstract class RaidMixin {
    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo callbackInfo) {
        Raid raid = (Raid) (Object) this;
        World world = raid.getWorld();

        if (!world.isClient && raid.hasWon() && !Awaken.worldProperties.isPostRaid()) {
            Broadcaster broadcaster = new Broadcaster();
            Awaken.worldProperties.setPostRaid();
            Broadcaster.broadcastMessage("A distant figure fades into the shadows...", Formatting.DARK_RED, false, true);
        }
    }
}
