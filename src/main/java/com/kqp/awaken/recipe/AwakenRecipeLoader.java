package com.kqp.awaken.recipe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kqp.awaken.init.Awaken;
import com.kqp.awaken.util.TimeUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Loads Awaken recipes from JSON files.
 */
public class AwakenRecipeLoader extends JsonDataLoader {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public AwakenRecipeLoader() {
        super(GSON, "awaken_recipes");
    }

    /**
     * Deserializes a map of JSON objects into Awaken recipe objects.
     *
     * @param loader
     * @param manager
     * @param profiler
     */
    @Override
    protected void apply(Map<Identifier, JsonObject> loader, ResourceManager manager, Profiler profiler) {
        Awaken.info("Loading Awaken recipes");
        TimeUtil.profile(
                () -> {
                    loader.forEach((id, json) -> {
                        String type = json.get("type").getAsString();
                        JsonObject recipeNode = json.getAsJsonObject("recipe");

                        JsonObject outputNode = recipeNode.getAsJsonObject("output");
                        ItemStack output = new ItemStack(
                                Registry.ITEM.get(new Identifier(outputNode.get("item").getAsString())),
                                outputNode.get("count") != null ? outputNode.get("count").getAsInt() : 1
                        );

                        JsonArray reagentsNode = recipeNode.getAsJsonArray("reagents");
                        HashMap<Reagent, Integer> reagents = new HashMap();

                        reagentsNode.forEach(jsonElement -> {
                            JsonObject reagentNode = jsonElement.getAsJsonObject();
                            ArrayList<ItemStack> matchingStacks = new ArrayList();

                            if (reagentNode.get("item") != null) {
                                Item item = Registry.ITEM.get(new Identifier(reagentNode.get("item").getAsString()));

                                matchingStacks.add(new ItemStack(item));
                            } else {
                                JsonArray matchingArray = reagentNode.get("matching").getAsJsonArray();
                                matchingArray.forEach(matchingNode -> {
                                    JsonObject matchingStackNode = matchingNode.getAsJsonObject();
                                    Item item = Registry.ITEM.get(new Identifier(matchingStackNode.get("item").getAsString()));

                                    matchingStacks.add(new ItemStack(item));
                                });
                            }

                            JsonElement countNode = reagentNode.get("count");
                            int count = countNode != null ? countNode.getAsInt() : 1;

                            reagents.put(new Reagent(matchingStacks), count);
                        });

                        AwakenRecipeManager.addRecipe(
                                type,
                                new AwakenRecipe(output, reagents)
                        );

                        AwakenRecipeManager.sort();
                    });
                },
                time -> Awaken.info("Loading of Awaken recipes took " + time + "ms")
        );
    }

    /**
     * Converts vanilla recipes to Awaken recipes.
     *
     * @param recipeManager The vanilla recipe manager
     */
    public static void addVanillaRecipes(RecipeManager recipeManager) {
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
