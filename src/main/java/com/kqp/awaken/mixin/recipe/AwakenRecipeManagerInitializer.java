package com.kqp.awaken.mixin.recipe;

import com.kqp.awaken.recipe.AwakenRecipeManager;
import com.kqp.awaken.recipe.AwakenRecipeManagerProvider;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to initialize the Awaken recipe manager and add vanilla recipes to it.
 */
@Mixin(ServerResourceManager.class)
public abstract class AwakenRecipeManagerInitializer implements AwakenRecipeManagerProvider {
    private AwakenRecipeManager awakenRecipeManager;

    @Shadow
    @Final
    private ReloadableResourceManager resourceManager;

    @Shadow
    @Final
    private RecipeManager recipeManager;

    @Inject(method = "<init>*", at = @At("RETURN"))
    public void construct(CallbackInfo callbackInfo) {
        this.awakenRecipeManager = new AwakenRecipeManager(recipeManager);
        resourceManager.registerListener(this.awakenRecipeManager);
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
