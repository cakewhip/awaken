package com.kqp.awaken.mixin;

import com.kqp.awaken.data.AwakenLevelData;
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
        AwakenLevelData awakenLevelData = AwakenLevelData.getFor(world);

        if (!world.isClient && raid.hasWon() && !awakenLevelData.isPostRaid()) {
            awakenLevelData.setPostRaid();
            Broadcaster.broadcastMessage(world.getServer(), "A distant figure fades into the shadows...", Formatting.DARK_RED, false, true);
        }
    }
}
