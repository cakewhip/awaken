package com.kqp.terminus.recipe;

import com.kqp.terminus.util.StringUtil;
import net.minecraft.item.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class Reagent {
    public final HashSet<ComparableItemStack> matchingStacks;
    public final int hashCode;

    public Reagent(List<ItemStack> itemStacks) {
        this.matchingStacks = new HashSet();

        itemStacks.forEach(itemStack -> {
            matchingStacks.add(new ComparableItemStack(itemStack));
        });

        hashCode = Objects.hash(matchingStacks);
    }

    public Reagent(ItemStack itemStack) {
        this(Arrays.asList(itemStack));
    }

    public boolean itemStackMatches(ItemStack itemStack) {
        return matchingStacks.contains(new ComparableItemStack(itemStack));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reagent reagent = (Reagent) o;
        return Objects.equals(matchingStacks, reagent.matchingStacks);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        List<String> list = matchingStacks.stream().map(ComparableItemStack::toString).collect(Collectors.toList());

        return StringUtil.commaSeparatedOr(list);
    }
}
