package com.kqp.awaken.network.recipe;

import com.kqp.awaken.network.AwakenPacketS2C;
import com.kqp.awaken.recipe.AwakenRecipe;
import com.kqp.awaken.recipe.AwakenRecipeManagerProvider;
import com.kqp.awaken.recipe.SyncAwakenRecipes;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class SyncRecipesS2C extends AwakenPacketS2C {
    public SyncRecipesS2C() {
        super("sync_recipes_s2c");
    }

    @Override
    public void accept(PacketContext context, PacketByteBuf data) {
        HashMap<Identifier, AwakenRecipe> recipes = SyncAwakenRecipes.receiveFromServer(data);

        context.getTaskQueue().execute(() -> {
            ((AwakenRecipeManagerProvider) MinecraftClient.getInstance().getNetworkHandler()).getAwakenRecipeManager().setRecipes(recipes);
        });
    }
}
