package com.kqp.awaken.recipe.dynamic;

import com.kqp.awaken.recipe.AwakenRecipe;
import com.kqp.awaken.recipe.ComparableItemStack;

import java.util.List;
import java.util.Map;

public abstract class DynamicAwakenRecipe {
    public abstract List<AwakenRecipe> getPossibleRecipes(Map<ComparableItemStack, Integer> inputMap);
}
