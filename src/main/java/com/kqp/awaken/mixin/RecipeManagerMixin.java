package com.kqp.awaken.mixin;

import com.google.gson.JsonObject;
import com.kqp.awaken.init.Awaken;
import com.kqp.awaken.recipe.AwakenRecipeManager;
import com.kqp.awaken.recipe.Reagent;
import com.kqp.awaken.recipe.RecipeType;
import com.kqp.awaken.util.TimeUtil;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Used to convert vanilla recipes to Awaken recipes.
 */
@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {
    @Inject(at = @At("RETURN"), method = "apply")
    protected void apply(Map<Identifier, JsonObject> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo callbackInfo) {
        RecipeManager recipeManager = (RecipeManager) (Object) this;

        Awaken.info("Converting vanilla shaped/shapeless recipes to Awaken recipes");
        TimeUtil.profile(
                () -> addVanillaRecipes(recipeManager),
                time -> Awaken.info("Conversion of recipes took " + time + "ms")
        );
    }

    /**
     * Converts vanilla recipes to Awaken recipes.
     *
     * @param recipeManager The vanilla recipe manager
     */
    private static void addVanillaRecipes(RecipeManager recipeManager) {
        AwakenRecipeManager.clear();

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
                    Awaken.warn("Recipe for " + recipe.getOutput() + " has no reagents, ignoring");
                } else if (recipe.getOutput() == null) {
                    Awaken.warn("Output not found for vanilla recipe, ignoring");
                } else {
                    boolean twoByTwo = false;

                    if (recipe instanceof ShapedRecipe) {
                        twoByTwo = ((ShapedRecipe) recipe).getWidth() <= 2 && ((ShapedRecipe) recipe).getHeight() <= 2;
                    } else if (recipe instanceof ShapelessRecipe) {
                        twoByTwo = reagents.keySet().size() <= 4;
                    }

                    AwakenRecipeManager.addRecipe(
                            twoByTwo ? RecipeType.TWO_BY_TWO : RecipeType.CRAFTING_TABLE,
                            recipe.getOutput(),
                            reagents
                    );
                }
            }
        }
    }
}
