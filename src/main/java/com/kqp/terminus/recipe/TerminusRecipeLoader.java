package com.kqp.terminus.recipe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kqp.terminus.Terminus;
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

public class TerminusRecipeLoader extends JsonDataLoader {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public TerminusRecipeLoader() {
        super(GSON, "terminus_recipes");
    }

    @Override
    protected void apply(Map<Identifier, JsonObject> loader, ResourceManager manager, Profiler profiler) {
        loader.forEach((id, json) -> {
            String type = json.get("type").getAsString();
            JsonObject recipeNode = json.getAsJsonObject("recipe");

            JsonObject outputNode = recipeNode.getAsJsonObject("output");
            ItemStack output = new ItemStack(
                    Registry.ITEM.get(new Identifier(outputNode.get("item").getAsString())),
                    outputNode.get("count").getAsInt()
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

                int count = reagentNode.get("count").getAsInt();

                reagents.put(new Reagent(matchingStacks), count);
            });

            TerminusRecipeManager.addRecipe(
                    type,
                    new TerminusRecipe(output, reagents)
            );
        });

        TerminusRecipeManager.sort();
    }
}
