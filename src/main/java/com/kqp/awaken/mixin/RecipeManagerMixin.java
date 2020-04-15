package com.kqp.awaken.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.kqp.awaken.init.Awaken;
import com.kqp.awaken.recipe.AwakenRecipeLoader;
import com.kqp.awaken.util.TimeUtil;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

/**
 * Used to convert vanilla recipes to Awaken recipes.
 */
@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {
    @Shadow
    private Map<RecipeType<?>, Map<Identifier, Recipe<?>>> recipes = ImmutableMap.of();

    @Inject(at = @At("RETURN"), method = "apply")
    protected void apply(Map<Identifier, JsonObject> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo callbackInfo) {
        RecipeManager recipeManager = (RecipeManager) (Object) this;

        // Map<Identifier, Recipe<?>> craftingRecipes = this.recipes.get(RecipeType.CRAFTING);
        // craftingRecipes.remove(new Identifier(""));

        Awaken.info("Converting vanilla shaped/shapeless recipes to Awaken recipes");
        TimeUtil.profile(
                () -> AwakenRecipeLoader.addVanillaRecipes(recipeManager),
                time -> Awaken.info("Conversion of recipes took " + time + "ms")
        );
    }
}
