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

    private final String groupName;

    public EntityFeatureGroup(String groupName) {
        this.groupName = groupName;
    }

    public EntityFeatureGroup addStatusEffect(StatusEffect effect, int amplifier) {
        statusEffects.add(new StatusEffectInstance(effect, Integer.MAX_VALUE, amplifier));
        return this;
    }

    public EntityFeatureGroup addEntityAttributeMultiplier(EntityAttribute attribute, double amount) {
        attributeModifiers.put(attribute, new EntityAttributeModifier(groupName + "_" + attribute.getTranslationKey(), amount, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        return this;
    }

    public EntityFeatureGroup addEntityAttributeAddition(EntityAttribute attribute, double amount) {
        attributeModifiers.put(attribute, new EntityAttributeModifier(groupName + "_" + attribute.getTranslationKey(), amount, EntityAttributeModifier.Operation.ADDITION));
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
                    new TranslatableText(statusEffect.getTranslationKey()).formatted(Formatting.GRAY)
                            .append(" ").formatted(Formatting.GRAY)
                            .append(new TranslatableText("enchantment.level." + (statusEffect.getAmplifier() + 1)).formatted(Formatting.GRAY))
            );
        });

        enchantmentModifiers.forEach((enchantment, lvl) -> {
            tooltip.add(
                    new LiteralText("+").formatted(Formatting.GRAY)
                            .append(new TranslatableText(enchantment.getTranslationKey())).formatted(Formatting.GRAY)
                            .append(" ").formatted(Formatting.GRAY)
                            .append(new TranslatableText("enchantment.level." + lvl)).formatted(Formatting.GRAY)
            );
        });

        List<String> attribModText = new ArrayList();

        attributeModifiers.forEach((attribute, modifier) -> {
            String value = String.format("%.1f", (Math.abs(modifier.getValue()) * (modifier.getOperation() == EntityAttributeModifier.Operation.ADDITION ? 1F : 100F))).replace(".0", "");

            String numberFormat = modifier.getOperation() != EntityAttributeModifier.Operation.ADDITION ? "%" : "";

            String change = modifier.getValue() > 0 ? "+" : "-";

            String attributeName = I18n.translate(attribute.getTranslationKey()).toLowerCase();

            attribModText.add(String.format("%s%s%s %s",
                    change,
                    value,
                    numberFormat,
                    attributeName
            ));
        });

        attribModText.stream().sorted().forEach(text -> tooltip.add(new LiteralText(text).formatted(Formatting.GRAY)));
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
