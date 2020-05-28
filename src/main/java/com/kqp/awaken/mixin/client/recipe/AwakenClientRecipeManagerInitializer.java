package com.kqp.awaken.mixin.client.recipe;

import com.kqp.awaken.recipe.AwakenRecipeManager;
import com.kqp.awaken.recipe.AwakenRecipeManagerProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to initialize the Awaken recipe manager.
 */
@Mixin(ClientPlayNetworkHandler.class)
public class AwakenClientRecipeManagerInitializer implements AwakenRecipeManagerProvider {
    @Shadow
    private MinecraftClient client;

    private AwakenRecipeManager awakenRecipeManager;

    @Inject(method = "<init>*", at = @At("RETURN"))
    public void construct(CallbackInfo callbackInfo) {
        this.awakenRecipeManager = new AwakenRecipeManager();
    }

    @Override
    public AwakenRecipeManager getAwakenRecipeManager() {
        return this.awakenRecipeManager;
    }

    @Override
    public void setAwakenRecipeManager(AwakenRecipeManager awakenRecipeManager) {
        this.awakenRecipeManager = awakenRecipeManager;
    }
}
