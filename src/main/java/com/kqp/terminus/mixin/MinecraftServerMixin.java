package com.kqp.terminus.mixin;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.recipe.TerminusRecipeLoader;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.util.snooper.SnooperListener;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to:
 * Grab the server instance.
 * Register the Terminus recipe loader.
 */
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin extends ReentrantThreadExecutor<ServerTask> implements SnooperListener, CommandOutput, AutoCloseable, Runnable {
    @Shadow
    @Final
    private ReloadableResourceManager dataManager;

    public MinecraftServerMixin(String name) {
        super(name);
    }

    @Inject(at = @At("RETURN"), method = "<init>*")
    public void construct(CallbackInfo callbackInfo) {
        MinecraftServer server = (MinecraftServer) (Object) this;

        Terminus.server = server;

        dataManager.registerListener(new TerminusRecipeLoader());
    }
}
