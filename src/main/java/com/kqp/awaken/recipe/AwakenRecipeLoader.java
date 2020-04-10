package com.kqp.awaken.recipe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kqp.awaken.Awaken;
import com.kqp.awaken.util.TimeUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
}
