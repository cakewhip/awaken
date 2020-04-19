package com.kqp.awaken.recipe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kqp.awaken.init.Awaken;
import com.kqp.awaken.util.TimeUtil;
import net.minecraft.client.MinecraftClient;
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
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manages all of the Awaken recipes.
 * TODO: take note of block tags and shorten recipe lists {@link net.minecraft.tag.BlockTags}.
 */
public class AwakenRecipeManager extends JsonDataLoader {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    /**
     * Map of recipe types ({@link RecipeType}) to a list of recipes.
     */
    private HashMap<Identifier, AwakenRecipe> recipes = new HashMap();

    public AwakenRecipeManager() {
        super(GSON, "awaken_recipes");
    }

    public void addRecipe(Identifier identifier, String recipeType, ItemStack output, HashMap<Reagent, Integer> reagents) {
        AwakenRecipe recipe = new AwakenRecipe(recipeType, output, reagents);
        recipes.put(identifier, recipe);
    }

    public void addRecipe(Identifier identifier, AwakenRecipe recipe) {
        recipes.put(identifier, recipe);
    }

    /**
     * Returns a list of recipes for a given recipe type.
     *
     * @param recipeType RecipeType
     * @return List of corresponding recipes
     */
    private List<AwakenRecipe> getRecipesForType(String recipeType) {
        ArrayList<AwakenRecipe> recipeList = new ArrayList();

        for (AwakenRecipe recipe : recipes.values()) {
            if (recipe.recipeType.equals(recipeType)) {
                recipeList.add(recipe);
            }
        }

        return recipeList;
    }

    /**
     * Returns a list of recipes that a passed list of item stacks can craft.
     *
     * @param recipeTypes      Recipe types to access
     * @param itemStacks Input item stacks
     * @return List of possible recipes
     */
    public List<AwakenRecipe> getMatches(String[] recipeTypes, List<ItemStack> itemStacks) {
        HashSet<String> recipeTypeSet = new HashSet(Arrays.asList(recipeTypes));
        HashMap<ComparableItemStack, Integer> input = AwakenRecipeManager.toComparableMap(itemStacks);
        ArrayList<AwakenRecipe> output = new ArrayList();

        output.addAll(recipes.values().parallelStream()
                .filter(recipe -> recipeTypeSet.contains(recipe.recipeType))
                .filter(recipe -> recipe.matches(input))
                .collect(Collectors.toList())
        );

        // Sort for that hot UX
        output.sort(Comparator.comparing(AwakenRecipe::getSortString));

        return output;
    }

    /**
     * Returns a list of recipes that have the passed item stack as an output.
     *
     * @param recipeTypes     Recipe types to access
     * @param itemStack Item stack output
     * @return List of recipes that have the passed item stack as an output
     */
    public List<AwakenRecipe> getMatchesForOutput(String[] recipeTypes, ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return Collections.emptyList();
        }

        HashSet<String> recipeTypeSet = new HashSet(Arrays.asList(recipeTypes));
        ArrayList<AwakenRecipe> output = new ArrayList();

        output.addAll(recipes.values().parallelStream()
                .filter(recipe -> recipeTypeSet.contains(recipe.recipeType))
                .filter(recipe -> ItemStack.areItemsEqual(recipe.result, itemStack))
                .collect(Collectors.toList())
        );

        return output;
    }

    /**
     * Returns a list of recipes that have the passed item stack as an input.
     *
     * @param itemStack Item stack input
     * @return List of recipes that have the passed item stack as an input
     */
    public List<AwakenRecipe> getRecipesUsingItemStack(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return Collections.emptyList();
        }

        ComparableItemStack comparableItemStack = new ComparableItemStack(itemStack);
        ArrayList<AwakenRecipe> output = new ArrayList();

        output.addAll(recipes.values().parallelStream()
                .filter(recipe -> {
                    for (Reagent reagent : recipe.reagents.keySet()) {
                        if (reagent.matchingStacks.contains(comparableItemStack)) {
                            return true;
                        }
                    }

                    return false;
                })
                .collect(Collectors.toList())
        );

        // Sort for that hot UX
        output.sort(Comparator.comparing(AwakenRecipe::getSortString));

        return output;
    }

    public void setRecipes(HashMap<Identifier, AwakenRecipe> recipes) {
        this.recipes = recipes;
    }

    public HashMap<Identifier, AwakenRecipe> getRecipes() {
        return this.recipes;
    }

    /**
     * Converts the passed list of item stacks to a map of comparable item stack objects to their counts.
     *
     * @param input List of item stacks to convert
     * @return Map of comparable item stacks to their counts
     */
    public static HashMap<ComparableItemStack, Integer> toComparableMap(List<ItemStack> input) {
        HashMap<ComparableItemStack, Integer> ret = new HashMap();

        input.stream().forEach(itemStack -> {
            ComparableItemStack key = new ComparableItemStack(itemStack);

            if (ret.containsKey(key)) {
                ret.replace(key, ret.get(key) + itemStack.getCount());
            } else {
                ret.put(key, itemStack.getCount());
            }
        });

        return ret;
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
        this.recipes.clear();

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

                        this.addRecipe(
                                id,
                                new AwakenRecipe(type, output, reagents)
                        );
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
    public void addVanillaRecipes(RecipeManager recipeManager) {
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
                        Reagent reagent = new Reagent(Arrays.asList(((MatchingStackProvider) (Object) ingredient).getMatchingStacks()));
                        int currentCount = reagents.getOrDefault(reagent, 0);

                        reagents.put(reagent, currentCount + 1);
                    }
                }

                if (reagents.isEmpty()) {
                    Awaken.warn("Recipe for " + recipe.getOutput() + " has no reagents, ignoring");
                } else if (recipe.getOutput() == null) {
                    Awaken.warn("Output not found for vanilla recipe, ignoring");
                } else {
                    String recipeType;

                    if (recipe instanceof ShapedRecipe) {
                        if (((ShapedRecipe) recipe).getWidth() <= 2 && ((ShapedRecipe) recipe).getHeight() <= 2) {
                            recipeType = RecipeType.TWO_BY_TWO;
                        } else {
                            recipeType = RecipeType.CRAFTING_TABLE;
                        }
                    } else if (recipe instanceof ShapelessRecipe) {
                        if (reagents.keySet().size() <= 4) {
                            recipeType = RecipeType.TWO_BY_TWO;
                        } else {
                            recipeType = RecipeType.CRAFTING_TABLE;
                        }
                    } else {
                        throw new IllegalStateException("Couldn't determine Awaken recipe type for vanilla recipe: " + recipe);
                    }

                    this.addRecipe(
                            recipe.getId(),
                            recipeType,
                            recipe.getOutput(),
                            reagents
                    );
                }
            }
        }
    }

    public static AwakenRecipeManager getFor(World world) {
        if (!world.isClient) {
            return ((AwakenRecipeManagerProvider) world.getServer()).getAwakenRecipeManager();
        } else {
            return ((AwakenRecipeManagerProvider) MinecraftClient.getInstance().getNetworkHandler()).getAwakenRecipeManager();
        }
    }
}
