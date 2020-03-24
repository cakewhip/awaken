package com.kqp.terminus.mixin;

import com.google.gson.JsonObject;
import net.minecraft.recipe.*;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Map;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {
    @Inject(at = @At("RETURN"), method = "apply")
    protected void apply(Map<Identifier, JsonObject> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo callbackInfo) {
        RecipeManager recipeManager = (RecipeManager) (Object) this;

        // todo convert vanilla recipes to mine
        /*
        recipeManager.keys().map(recipeManager::get).forEach(optionalRecipe -> {
            if (optionalRecipe.isPresent()) {
                Recipe<?> recipe = optionalRecipe.get();

                if (recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe) {
                    List<Ingredient> ingredientList = recipe.getPreviewInputs();
                    for (Ingredient ingredient : ingredientList) {
                    }
                }
            }
        });
         */
    }
}
