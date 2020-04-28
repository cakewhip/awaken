package com.kqp.awaken.mixin.server;

import com.kqp.awaken.recipe.AwakenRecipeManager;
import com.kqp.awaken.recipe.AwakenRecipeManagerProvider;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to register the Awaken recipe loader.
 */
@Mixin(MinecraftServer.class)
@Implements(@Interface(iface = AwakenRecipeManagerProvider.class, prefix = "vw$"))
public abstract class MinecraftServerMixin implements AwakenRecipeManagerProvider {
    private AwakenRecipeManager awakenRecipeManager;

    @Shadow
    @Final
    private ReloadableResourceManager dataManager;

    @Shadow
    @Final
    private RecipeManager recipeManager;

    @Inject(method = "<init>*", at = @At("RETURN"))
    public void construct(CallbackInfo callbackInfo) {
        this.awakenRecipeManager = new AwakenRecipeManager();
        dataManager.registerListener(this.awakenRecipeManager);
    }

    @Inject(method = "reloadDataPacks", at = @At("RETURN"))
    private void convertVanillaRecipes(CallbackInfo callbackInfo) {
        this.awakenRecipeManager.addVanillaRecipes(this.recipeManager);
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
