package com.kqp.awaken.mixin.awakening;

import com.kqp.awaken.world.data.AwakenLevelData;
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
public abstract class RaidVictoryListener {
    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo callbackInfo) {
        Raid raid = (Raid) (Object) this;
        World world = raid.getWorld();

        if (!world.isClient) {
            AwakenLevelData awakenLevelData = AwakenLevelData.getFor(world.getServer());

            if (raid.hasWon() && !awakenLevelData.isPostRaid()) {
                awakenLevelData.setPostRaid();
                Broadcaster.broadcastMessage(world.getServer(), "The shadows stir around you...", Formatting.DARK_RED, false, true);
            }
        }
    }
}
