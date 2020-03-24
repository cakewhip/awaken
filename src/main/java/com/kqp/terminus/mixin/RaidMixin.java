package com.kqp.terminus.mixin;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.util.Broadcaster;
import net.minecraft.entity.raid.Raid;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Raid.class)
public abstract class RaidMixin {
    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo callbackInfo) {
        Raid raid = (Raid) (Object) this;
        World world = raid.getWorld();

        if (!world.isClient && raid.hasWon() && !Terminus.worldProperties.isPostRaid()) {
            Broadcaster broadcaster = new Broadcaster();
            Terminus.worldProperties.setPostRaid();
            broadcaster.broadcastMessage("A distant figure fades into the shadows...", Formatting.DARK_RED, false, true);
        }
    }
}
