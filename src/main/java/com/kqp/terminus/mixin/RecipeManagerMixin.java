package com.kqp.terminus.mixin;

import com.google.gson.JsonObject;
import com.kqp.terminus.Terminus;
import com.kqp.terminus.recipe.Reagent;
import com.kqp.terminus.recipe.RecipeType;
import com.kqp.terminus.recipe.TerminusRecipeManager;
import com.kqp.terminus.util.TimeUtil;
import net.minecraft.recipe.*;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.stream.Collectors;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {
    @Inject(at = @At("RETURN"), method = "apply")
    protected void apply(Map<Identifier, JsonObject> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo callbackInfo) {
        RecipeManager recipeManager = (RecipeManager) (Object) this;

        Terminus.info("Converting vanilla shaped/shapeless recipes to Terminus recipes");
        TimeUtil.profile(
                () -> addVanillaRecipes(recipeManager),
                time -> Terminus.info("Conversion of recipes took " + time + "ms")
        );
    }

    private static void addVanillaRecipes(RecipeManager recipeManager) {
        List<Recipe> recipes = recipeManager.keys()
                .map(recipeManager::get)
                .filter(optionalRecipe -> optionalRecipe.isPresent())
                .map(optionalRecipe -> optionalRecipe.get())
                .collect(Collectors.toList());

        for (Recipe recipe : recipes) {
            if (recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe) {
                List<Ingredient> ingredientList = recipe.getPreviewInputs();
                HashMap<Reagent, Integer> reagents = new HashMap();

                for (Ingredient ingredient : ingredientList) {
                    if (!ingredient.isEmpty()) {
                        Reagent reagent = new Reagent(Arrays.asList(ingredient.getMatchingStacksClient()));
                        int currentCount = reagents.getOrDefault(reagent, 0);

                        reagents.put(reagent, currentCount + 1);
                    }
                }

                if (reagents.isEmpty()) {
                    Terminus.warn("Recipe for " + recipe.getOutput() + " has no reagents, ignoring");
                } else if (recipe.getOutput() == null) {
                    Terminus.warn("Output not found for vanilla recipe, ignoring");
                } else {
                    TerminusRecipeManager.addRecipe(RecipeType.VANILLA, recipe.getOutput(), reagents);
                    Terminus.info("Adding recipe for " + recipe.getOutput() + " with reagents " + reagents);
                }
            }
        }

        TerminusRecipeManager.sort();
    }
}
