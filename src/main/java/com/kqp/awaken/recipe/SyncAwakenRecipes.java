package com.kqp.awaken.recipe;

import com.kqp.awaken.init.AwakenNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;

public class SyncAwakenRecipes {
    public static void syncToPlayer(HashMap<Identifier, AwakenRecipe> recipes, ServerPlayerEntity player) {
        AwakenNetworking.SYNC_RECIPES_S2C.sendToPlayer(player, (buf) -> {
            buf.writeInt(recipes.size());

            recipes.forEach(((identifier, betterCraftingRecipe) -> {
                writeRecipe(buf, identifier, betterCraftingRecipe);
            }));
        });
    }

    public static HashMap<Identifier, AwakenRecipe> receiveFromServer(PacketByteBuf buf) {
        HashMap<Identifier, AwakenRecipe> recipes = new HashMap();
        int nRecipes = buf.readInt();

        for (int i = 0; i < nRecipes; i++) {
            readRecipe(recipes, buf);
        }

        return recipes;
    }

    private static void writeRecipe(PacketByteBuf buf, Identifier identifier, AwakenRecipe recipe) {
        buf.writeIdentifier(identifier);
        buf.writeString(recipe.recipeType);
        buf.writeItemStack(recipe.result);
        buf.writeInt(recipe.reagents.size());

        recipe.reagents.forEach(((reagent, count) -> {
            buf.writeInt(reagent.matchingStacks.size());

            reagent.matchingStacks.forEach((comparableItemStack -> buf.writeItemStack(comparableItemStack.itemStack)));
            buf.writeInt(count);
        }));
    }

    private static void readRecipe(HashMap<Identifier, AwakenRecipe> recipes, PacketByteBuf buf) {
        Identifier identifier = buf.readIdentifier();
        String recipeType = buf.readString();
        ItemStack resultStack = buf.readItemStack();
        HashMap<Reagent, Integer> reagents = new HashMap();
        int nReagents = buf.readInt();

        for (int i = 0; i < nReagents; i++) {
            ArrayList<ItemStack> matchingStacks = new ArrayList();
            int nMatchingStacks = buf.readInt();

            for (int j = 0; j < nMatchingStacks; j++) {
                matchingStacks.add(buf.readItemStack());
            }

            int count = buf.readInt();

            reagents.put(new Reagent(matchingStacks), count);
        }

        recipes.put(identifier, new AwakenRecipe(recipeType, resultStack, reagents));
    }
}
