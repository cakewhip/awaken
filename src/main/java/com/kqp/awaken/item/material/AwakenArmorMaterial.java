package com.kqp.awaken.item.material;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.kqp.awaken.init.Awaken;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Used to create custom armor materials.
 */
public class AwakenArmorMaterial implements ArmorMaterial {
    private static final int[] BASE_DURABILITY = new int[] { 13, 15, 16, 11 };
    private final String name;
    private final int durabilityMultiplier;
    private final int[] protectionAmounts;
    private final int enchantability;
    private final Supplier<SoundEvent> equipSoundSupplier;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredientSupplier;

    private AwakenArmorMaterial(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, Supplier<SoundEvent> equipSoundSupplier, float toughness, float knockbackResistance, Supplier<Ingredient> ingredientSupplier) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantability = enchantability;
        this.equipSoundSupplier = equipSoundSupplier;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredientSupplier = ingredientSupplier;
    }

    @Override
    public int getDurability(EquipmentSlot slot) {
        return BASE_DURABILITY[slot.getEntitySlotId()] * this.durabilityMultiplier;
    }

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        return this.protectionAmounts[slot.getEntitySlotId()];
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSoundSupplier.get();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredientSupplier.get();
    }

    @Environment(EnvType.CLIENT)
    public String getName() {
        return this.name;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    private static JsonDeserializer<AwakenArmorMaterial> DESERIALIZER = (json, typeOfT, context) -> {
        JsonObject jsonArmorMaterial = json.getAsJsonObject();

        // Easy to grab fields
        String name = jsonArmorMaterial.get("name").getAsString();
        int durabilityMultiplier = jsonArmorMaterial.get("durability_multiplier").getAsInt();
        int enchantability = jsonArmorMaterial.get("enchantability").getAsInt();
        float toughness = jsonArmorMaterial.get("toughness").getAsFloat();

        // Protection amounts is stored in a JSON array, just need to iterate through first four
        int[] protectionAmounts = new int[4];
        JsonArray jsonProtectionAmounts = jsonArmorMaterial.getAsJsonArray("protection_amounts");
        for (int i = 0; i < protectionAmounts.length; i++) {
            protectionAmounts[i] = jsonProtectionAmounts.get(i).getAsInt();
        }

        String equipSoundString = jsonArmorMaterial.get("equip_sound").getAsString();
        Identifier equipSoundId = new Identifier(equipSoundString);
        Supplier<SoundEvent> equipSoundSupplier = () -> {
            return Registry.SOUND_EVENT.get(equipSoundId);
        };

        // Repair ingredients are stored as string identifiers
        JsonArray jsonRepairIngredients = jsonArmorMaterial.get("repair_ingredients").getAsJsonArray();
        List<Identifier> repairIngredientIds = new ArrayList<Identifier>();

        // Grab strings, map to Identifier objects, add to list of IDs
        jsonRepairIngredients.forEach(repairIngredientElement -> {
            repairIngredientIds.add(new Identifier(repairIngredientElement.getAsString()));
        });

        // Create supplier that will dynamically grab items and map them from item->itemstack->ingredient
        // This is because it's uncertain whether items will be loaded when armor materials are loaded
        Supplier<Ingredient> repairIngredientsSupplier = () -> {
            return Ingredient.method_26964(repairIngredientIds
                    .stream()
                    .map(repairIngredientId -> Registry.ITEM.get(repairIngredientId))
                    .map(ItemStack::new)
            );
        };

        return new AwakenArmorMaterial(
                name,
                durabilityMultiplier,
                protectionAmounts,
                enchantability,
                equipSoundSupplier,
                toughness,
                0.0F,
                repairIngredientsSupplier
        );
    };

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(AwakenArmorMaterial.class, DESERIALIZER)
            .create();

    private static AwakenArmorMaterial fromJson(String name) {
        String path = String.format("data/%s/item_stats/armor/%s.json", Awaken.MOD_ID, name);
        InputStreamReader reader = new InputStreamReader(AwakenArmorMaterial.class.getClassLoader().getResourceAsStream(path));

        return GSON.fromJson(reader, AwakenArmorMaterial.class);
    }

    // TODO: move these to a registry and make armor items auto-magically pull from it
    public static final AwakenArmorMaterial DRAGON_SCALE = fromJson("dragon_scale");

    public static final AwakenArmorMaterial WITHER_BONE = fromJson("wither_bone");

    public static final AwakenArmorMaterial SALVIUM = fromJson("salvium");

    public static final AwakenArmorMaterial VALERIUM = fromJson("valerium");
}
