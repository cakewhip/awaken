package com.kqp.awaken.effect;

import com.google.common.collect.ArrayListMultimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.kqp.awaken.ability.Ability;
import com.kqp.awaken.ability.AbilityComponent;
import com.kqp.awaken.init.Awaken;
import com.kqp.awaken.init.AwakenAbilities;
import com.kqp.awaken.util.AttributeUtil;
import com.kqp.awaken.util.EquipmentUtil;
import com.kqp.awaken.util.TooltipUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Used to hold status effects and entity attributes.
 */
public class EntityFeatureGroup {
    private final ArrayList<StatusEffectInstance> statusEffects = new ArrayList();
    private final ArrayListMultimap<EntityAttribute, EntityAttributeModifier> attributeModifiers = ArrayListMultimap.create();
    private final HashMap<Enchantment, Integer> enchantmentModifiers = new HashMap();
    private final HashSet<Ability> abilitySet = new HashSet();

    private String groupName;

    public EntityFeatureGroup addStatusEffect(StatusEffect effect, int amplifier) {
        statusEffects.add(new StatusEffectInstance(effect, Integer.MAX_VALUE, amplifier));
        return this;
    }

    public EntityFeatureGroup addEntityAttributeModifier(EntityAttribute attribute, EntityAttributeModifier.Operation operation, double amount) {
        attributeModifiers.put(attribute, new EntityAttributeModifier(groupName + "_" + attribute.getTranslationKey(), amount, operation));
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

    public EntityFeatureGroup addAbility(Ability ability) {
        abilitySet.add(ability);

        return this;
    }

    public void applyTo(LivingEntity living) {
        statusEffects.forEach(statusEffect -> living.addStatusEffect(statusEffect));

        living.getAttributes().addTemporaryModifiers(attributeModifiers);

        if (!living.world.isClient && living instanceof PlayerEntity) {
            abilitySet.forEach(ability -> {
                AbilityComponent abilityComponent = ability.get((PlayerEntity) living);

                boolean prevState = abilityComponent.flag;

                abilityComponent.flag = true;

                if (!prevState) {
                    abilityComponent.sync();
                }
            });
        }
    }

    public void removeFrom(LivingEntity living) {
        statusEffects.forEach(statusEffect -> living.removeStatusEffect(statusEffect.getEffectType()));
        living.getAttributes().removeModifiers(attributeModifiers);

        if (!living.world.isClient && living instanceof PlayerEntity) {
            abilitySet.forEach(ability -> {
                ability.get((PlayerEntity) living).flag = false;
            });

            EquipmentUtil.getAllEntityFeatureGroupProviders((PlayerEntity) living).stream()
                    .map(EntityFeatureGroup::getAbilitySet)
                    .flatMap(Set::stream)
                    .filter(abilitySet::contains)
                    .forEach(ability -> {
                        ability.get((PlayerEntity) living).flag = true;
                    });

            abilitySet.forEach(ability -> {
                if (!ability.get((PlayerEntity) living).flag) {
                    ability.getComponentType().get(living).sync();
                }
            });
        }
    }

    /**
     * TODO: find a way to determine whether the entity attrib is a percentage or not; things like the ammo consumption attrib are confusing
     */
    @Environment(EnvType.CLIENT)
    public void populateTooltips(List<Text> tooltip) {
        abilitySet.forEach(ability -> {
            TooltipUtil.addIterableTooltips(tooltip, ability.getTranslationKey(), Formatting.BLUE);
        });

        statusEffects.forEach(statusEffect -> {
            tooltip.add(
                    new TranslatableText(statusEffect.getTranslationKey()).formatted(Formatting.BLUE)
                            .append(" ").formatted(Formatting.BLUE)
                            .append(new TranslatableText("enchantment.level." + (statusEffect.getAmplifier() + 1)).formatted(Formatting.BLUE))
            );
        });

        enchantmentModifiers.forEach((enchantment, lvl) -> {
            tooltip.add(
                    new LiteralText("+").formatted(Formatting.BLUE)
                            .append(new TranslatableText(enchantment.getTranslationKey())).formatted(Formatting.BLUE)
                            .append(" ").formatted(Formatting.BLUE)
                            .append(new TranslatableText("enchantment.level." + lvl)).formatted(Formatting.BLUE)
            );
        });

        List<String> attribModText = new ArrayList();

        attributeModifiers.forEach((attribute, modifier) -> {
            attribModText.add(AttributeUtil.toTooltip(attribute, modifier));
        });

        attribModText.stream().sorted().forEach(text -> tooltip.add(new LiteralText(text).formatted(Formatting.BLUE)));
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

    public Set<Ability> getAbilitySet() {
        return abilitySet;
    }

    public EntityFeatureGroup setGroupName(String groupName) {
        this.groupName = groupName;

        return this;
    }

    public String getGroupName() {
        return groupName;
    }

    public static EntityFeatureGroup fromJsonObject(String name, JsonObject jsonEfg) {
        EntityFeatureGroup efg = new EntityFeatureGroup();
        efg.setGroupName(name);

        JsonArray statusEffectsJson = jsonEfg.get("status_effects").getAsJsonArray();
        statusEffectsJson.forEach(statusEffectElement -> {
            JsonObject statusEffectJson = statusEffectElement.getAsJsonObject();

            Identifier statusEffectId = new Identifier(statusEffectJson.get("status_effect").getAsString());
            StatusEffect statusEffect = Registry.STATUS_EFFECT.get(statusEffectId);

            int amplifier = statusEffectJson.get("amplifier").getAsInt();

            amplifier = amplifier - 1; // Just to make the JSON more human-friendly

            efg.addStatusEffect(statusEffect, amplifier);
        });

        JsonArray attribModsJson = jsonEfg.get("attribute_modifiers").getAsJsonArray();
        attribModsJson.forEach(attribModElement -> {
            JsonObject attribModJson = attribModElement.getAsJsonObject();

            Identifier attribId = new Identifier(attribModJson.get("entity_attribute").getAsString());
            EntityAttribute attribute = Registry.ATTRIBUTE.get(attribId);

            String operationString = attribModJson.get("operation").getAsString();
            EntityAttributeModifier.Operation operation = EntityAttributeModifier.Operation.ADDITION;

            switch (operationString) {
                case "addition":
                    operation = EntityAttributeModifier.Operation.ADDITION;
                    break;
                case "multiply_base":
                    operation = EntityAttributeModifier.Operation.MULTIPLY_BASE;
                    break;
                case "multiply_total":
                    operation = EntityAttributeModifier.Operation.MULTIPLY_TOTAL;
                    break;
            }

            double value = attribModJson.get("value").getAsDouble();

            efg.addEntityAttributeModifier(attribute, operation, value);
        });

        JsonArray enchantModsJson = jsonEfg.get("enchantment_modifiers").getAsJsonArray();
        enchantModsJson.forEach(enchantModElement -> {
            JsonObject enchantModJson = enchantModElement.getAsJsonObject();

            Identifier enchantmentId = new Identifier(enchantModJson.get("enchantment").getAsString());
            Enchantment enchantment = Registry.ENCHANTMENT.get(enchantmentId);

            int value = enchantModJson.get("value").getAsInt();

            efg.addEnchantmentModifier(enchantment, value);
        });

        JsonArray abilitiesJson = jsonEfg.get("abilities").getAsJsonArray();
        abilitiesJson.forEach(abilityElement -> {
            Identifier abilityId = new Identifier(abilityElement.getAsString());
            Ability ability = AwakenAbilities.ABILITY_MAP.get(abilityId);

            if (ability == null) {
                throw new RuntimeException("Could not find ability with ID " + abilityId + " when parsing item " + name);
            }

            efg.addAbility(ability);
        });

        return efg;
    }
}
