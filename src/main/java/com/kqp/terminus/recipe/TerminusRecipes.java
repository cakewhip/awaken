package com.kqp.terminus.recipe;

import com.kqp.terminus.Terminus;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class TerminusRecipes {
    public static final ArrayList<TerminusRecipe> RECIPES = new ArrayList();

    static {
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Terminus.Groups.CELESTIAL.SWORD, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 128),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 128)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Terminus.Groups.CELESTIAL.SHOVEL, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 64),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 64)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Terminus.Groups.CELESTIAL.PICKAXE, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 128),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 128)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Terminus.Groups.CELESTIAL.AXE, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 72),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 72)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.DIAMOND, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.IRON_AXE, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.ACACIA_BOAT, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.DIAMOND_BLOCK, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.DIAMOND_BOOTS, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.DIAMOND_CHESTPLATE, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.DIAMOND_SWORD, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.EMERALD, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.FISHING_ROD, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.FERMENTED_SPIDER_EYE, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.BLUE_SHULKER_BOX, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.VEX_SPAWN_EGG, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.GHAST_TEAR, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.ACACIA_LOG, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.DETECTOR_RAIL, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.DIAMOND, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.IRON_AXE, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.ACACIA_BOAT, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.DIAMOND_BLOCK, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.DIAMOND_BOOTS, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.DIAMOND_CHESTPLATE, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.DIAMOND_SWORD, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.EMERALD, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.FISHING_ROD, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.FERMENTED_SPIDER_EYE, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.BLUE_SHULKER_BOX, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.VEX_SPAWN_EGG, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.GHAST_TEAR, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.ACACIA_LOG, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.DETECTOR_RAIL, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.DIAMOND, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.IRON_AXE, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.ACACIA_BOAT, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.DIAMOND_BLOCK, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.DIAMOND_BOOTS, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.DIAMOND_CHESTPLATE, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.DIAMOND_SWORD, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.EMERALD, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.FISHING_ROD, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.FERMENTED_SPIDER_EYE, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.BLUE_SHULKER_BOX, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.VEX_SPAWN_EGG, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.GHAST_TEAR, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.ACACIA_LOG, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.DETECTOR_RAIL, 1),
                new ItemStack(Terminus.Groups.MOONSTONE.INGOT, 2),
                new ItemStack(Terminus.Groups.SUNSTONE.INGOT, 2)
        ));
    }

    public static List<TerminusRecipe> getMatches(List<ItemStack> itemStacks) {
        ArrayList<TerminusRecipe> output = new ArrayList();
        HashMap<ComparableItemStack, Integer> input = TerminusRecipe.toComparableMap(itemStacks);

        for (TerminusRecipe recipe : RECIPES) {
            if (recipe.matches(input)) {
                output.add(recipe);
            }
        }

        return output;
    }

    public static List<TerminusRecipe> getMatchesForOutput(ItemStack itemStack) {
        ArrayList<TerminusRecipe> output = new ArrayList();

        for (TerminusRecipe recipe : RECIPES) {
            if (ItemStack.areItemsEqual(recipe.result, itemStack)) {
                output.add(recipe);
            }
        }

        return output;
    }
}
