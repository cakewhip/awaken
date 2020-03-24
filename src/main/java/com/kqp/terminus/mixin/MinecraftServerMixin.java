package com.kqp.terminus.mixin;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.util.Broadcaster;
import net.minecraft.entity.raid.Raid;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Inject(at = @At("RETURN"), method = "<init>*")
    public void construct(CallbackInfo callbackInfo) {
        MinecraftServer server = (MinecraftServer) (Object) this;

        Terminus.server = server;
    }
}
