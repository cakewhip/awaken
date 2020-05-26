package com.kqp.awaken.item.effect;

import com.google.common.collect.ArrayListMultimap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to hold status effects and entity attributes.
 */
public class EntityFeatureGroup {
    private final ArrayList<StatusEffectInstance> statusEffects = new ArrayList();
    private final ArrayListMultimap<EntityAttribute, EntityAttributeModifier> attributeModifiers = ArrayListMultimap.create();
    private final HashMap<Enchantment, Integer> enchantmentModifiers = new HashMap();

    public EntityFeatureGroup addStatusEffect(StatusEffect effect, int amplifier) {
        statusEffects.add(new StatusEffectInstance(effect, Integer.MAX_VALUE, amplifier));
        return this;
    }

    public EntityFeatureGroup addEntityAttributeMultiplier(EntityAttribute attribute, String name, double amount) {
        attributeModifiers.put(attribute, new EntityAttributeModifier(name, amount, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        return this;
    }

    public EntityFeatureGroup addEntityAttributeAddition(EntityAttribute attribute, String name, double amount) {
        attributeModifiers.put(attribute, new EntityAttributeModifier(name, amount, EntityAttributeModifier.Operation.ADDITION));
        return this;
    }

    public EntityFeatureGroup addEnchantmentModifier(Enchantment enchantment, int modifier) {
        enchantmentModifiers.put(enchantment, modifier);
        return this;
    }

    public void applyTo(LivingEntity living) {
        statusEffects.forEach(statusEffect -> living.addStatusEffect(statusEffect));
        living.getAttributes().addTemporaryModifiers(attributeModifiers);
    }

    public void removeFrom(LivingEntity living) {
        statusEffects.forEach(statusEffect -> living.removeStatusEffect(statusEffect.getEffectType()));
        living.getAttributes().removeModifiers(attributeModifiers);
    }

    @Environment(EnvType.CLIENT)
    public void populateTooltips(List<Text> tooltip) {
        statusEffects.forEach(statusEffect -> {
            tooltip.add(
                    new TranslatableText(statusEffect
                            .getTranslationKey())
                            .append(new TranslatableText("enchantment.level." + (statusEffect.getAmplifier() + 1)))
            );
        });

        attributeModifiers.forEach((attribute, modifier) -> {
            String value = String.format("%.1f", (modifier.getValue() * 100F)).replace(".0", "");

            String numberFormat = modifier.getOperation() != EntityAttributeModifier.Operation.ADDITION ? "%" : "";

            String change = modifier.getValue() > 0 ? "increased" : "decreased";

            Formatting color = modifier.getValue() > 0 ? Formatting.DARK_GREEN : Formatting.RED;

            String attributeName = I18n.translate(attribute.getTranslationKey()).toLowerCase();

            tooltip.add(new LiteralText(String.format("%s%s %s %s",
                    value,
                    numberFormat,
                    change,
                    attributeName
            )).formatted(color));
        });
    }

    public List<StatusEffectInstance> getStatusEffects() {
        return statusEffects;
    }

    public ArrayListMultimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers() {
        return attributeModifiers;
    }

    public Map<Enchantment, Integer> getEnchantmentModifiers() {
        return enchantmentModifiers;
    }
}
