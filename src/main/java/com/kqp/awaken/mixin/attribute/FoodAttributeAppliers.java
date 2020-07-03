package com.kqp.awaken.mixin.attribute;

import com.kqp.awaken.entity.player.PlayerReference;
import com.kqp.awaken.init.AwakenEntityAttributes;
import com.kqp.awaken.util.AttributeUtil;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.HashSet;
import java.util.Set;

/**
 * Used to implement food attributes.
 */
@Mixin(HungerManager.class)
public class FoodAttributeAppliers implements PlayerReference {
    private static final Set<Item> MEAT = new HashSet();
    private static final Set<Item> VEGETARIAN = new HashSet();

    public PlayerEntity player;

    @Redirect(method = "eat", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;add(IF)V"))
    private void applyAttributes(HungerManager hungerManager, int food, float saturation, Item item, ItemStack stack) {
        EntityAttributeInstance carnivore = player.getAttributeInstance(AwakenEntityAttributes.CARNIVORE);
        EntityAttributeInstance herbivore = player.getAttributeInstance(AwakenEntityAttributes.HERBIVORE);
        EntityAttributeInstance omnivore = player.getAttributeInstance(AwakenEntityAttributes.OMNIVORE);
        EntityAttributeInstance foodSaturation = player.getAttributeInstance(AwakenEntityAttributes.FOOD_SATURATION);

        if (MEAT.contains(item)) {
            food = (int) AttributeUtil.applyAttribute(carnivore, food);
        }

        if (VEGETARIAN.contains(item)) {
            food = (int) AttributeUtil.applyAttribute(herbivore, food);
        }

        food = (int) AttributeUtil.applyAttribute(omnivore, food);
        saturation = (float) AttributeUtil.applyAttribute(foodSaturation, saturation);

        ((HungerManager) (Object) this).add(food, saturation);
    }

    @Override
    public PlayerEntity getPlayer() {
        return player;
    }

    @Override
    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }

    static {
        MEAT.add(Items.PORKCHOP);
        MEAT.add(Items.COOKED_PORKCHOP);
        MEAT.add(Items.BEEF);
        MEAT.add(Items.COOKED_BEEF);
        MEAT.add(Items.MUTTON);
        MEAT.add(Items.COOKED_MUTTON);
        MEAT.add(Items.CHICKEN);
        MEAT.add(Items.COOKED_CHICKEN);
        MEAT.add(Items.COD);
        MEAT.add(Items.COOKED_COD);
        MEAT.add(Items.SALMON);
        MEAT.add(Items.COOKED_SALMON);
        MEAT.add(Items.ROTTEN_FLESH);

        VEGETARIAN.add(Items.BREAD);
        VEGETARIAN.add(Items.CARROT);
        VEGETARIAN.add(Items.GOLDEN_CARROT);
        VEGETARIAN.add(Items.POTATO);
        VEGETARIAN.add(Items.BAKED_POTATO);
        VEGETARIAN.add(Items.POISONOUS_POTATO);
        VEGETARIAN.add(Items.CHORUS_FRUIT);
        VEGETARIAN.add(Items.DRIED_KELP);
        VEGETARIAN.add(Items.PUMPKIN_PIE);
        VEGETARIAN.add(Items.BEETROOT);
        VEGETARIAN.add(Items.BEETROOT_SOUP);
        VEGETARIAN.add(Items.MUSHROOM_STEW);
        VEGETARIAN.add(Items.APPLE);
    }
}
