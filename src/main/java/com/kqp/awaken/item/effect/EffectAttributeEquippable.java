package com.kqp.awaken.item.effect;

import com.google.common.collect.ArrayListMultimap;
import com.kqp.awaken.entity.attribute.AwakenEntityAttributes;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

/**
 * Used to add equip-sensitive status effects and entity attribute modifiers.
 */
public class EffectAttributeEquippable implements Equippable {
    private final ArrayList<StatusEffectInstance> statusEffects = new ArrayList();
    private final ArrayListMultimap<EntityAttribute, EntityAttributeModifier> attributeModifiers = ArrayListMultimap.create();

    public EffectAttributeEquippable addStatusEffect(StatusEffect effect, int amplifier) {
        statusEffects.add(new StatusEffectInstance(effect, Integer.MAX_VALUE, amplifier));
        return this;
    }

    public EffectAttributeEquippable addEntityAttributeModifier(EntityAttribute attribute, String name, double amount) {
        attributeModifiers.put(attribute, new EntityAttributeModifier(name, amount, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        return this;
    }

    @Override
    public void equip(ItemStack itemStack, PlayerEntity player) {
        applyEffects(player);
    }

    @Override
    public void unEquip(ItemStack itemStack, PlayerEntity player) {
        removeEffects(player);
    }

    public void applyEffects(PlayerEntity player) {
        statusEffects.forEach(statusEffect -> player.addStatusEffect(statusEffect));
        player.getAttributes().addTemporaryModifiers(attributeModifiers);
    }

    public void removeEffects(PlayerEntity player) {
        statusEffects.forEach(statusEffect -> player.removeStatusEffect(statusEffect.getEffectType()));
        player.getAttributes().removeModifiers(attributeModifiers);
    }
}
