package com.kqp.awaken.init;

import com.kqp.awaken.item.BossSpawnerItem;
import com.kqp.awaken.item.armor.AwakenArmorItem;
import com.kqp.awaken.item.bow.AwakenBowItem;
import com.kqp.awaken.item.bow.FlameBowItem;
import com.kqp.awaken.item.bow.StatusEffectBowItem;
import com.kqp.awaken.item.effect.EntityFeatureGroup;
import com.kqp.awaken.item.health.HealthIncreaseItem;
import com.kqp.awaken.item.material.AwakenArmorMaterial;
import com.kqp.awaken.item.material.AwakenToolMaterial;
import com.kqp.awaken.item.shovel.ArchaeologistSpadeItem;
import com.kqp.awaken.item.sword.AtlanteanSabreItem;
import com.kqp.awaken.item.sword.AwakenSwordItem;
import com.kqp.awaken.item.sword.EnderianCutlassItem;
import com.kqp.awaken.item.sword.StatusEffectSwordItem;
import com.kqp.awaken.item.tool.AwakenAxeItem;
import com.kqp.awaken.item.tool.AwakenPickaxeItem;
import com.kqp.awaken.item.trident.AwakenTridentItem;
import com.kqp.awaken.item.trinket.AwakenTrinketItem;
import com.kqp.awaken.item.trinket.flight.WingsTrinketItem;
import com.kqp.awaken.trinket.AwakenSlots;
import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AwakenItems {
    public static class Reagents {
        public static final Item CREEPER_TISSUE = genericItem("creeper_tissue");
        public static final Item NECROTIC_HEART = genericItem("necrotic_heart");
        public static final Item SPIDER_SILK = genericItem("spider_silk");
        public static final Item BONE_MARROW = genericItem("bone_marrow");
        public static final Item MONSTER_HEART = register("monster_heart", new HealthIncreaseItem("monster_heart", 10));

        public static final Item ENDER_DRAGON_SCALE = genericItem("ender_dragon_scale");
        public static final Item WITHER_RIB = genericItem("wither_rib");


        public static final Item FORTIFIED_STICK = genericItem("fortified_stick");

        public static final Item CORSAIR_TOKEN = genericItem("corsair_token");
        public static final Item RAPTOR_CHICKEN_EGG = genericItem("raptor_chicken_egg");
        public static final Item VOID_CRUX = genericItem("void_crux");

        public static final Item SALVIUM_INGOT = genericItem("salvium_ingot");
        public static final Item VALERIUM_INGOT = genericItem("valerium_ingot");

        public static final Item SMOLDERING_HEART = genericItem("smoldering_heart");
        public static final Item FIERY_CORE = genericItem("fiery_core");
        public static final Item MAGMA_STRAND = genericItem("magma_strand");
        public static final Item CINDERED_SOUL = genericItem("cindered_soul");
        public static final Item FIERY_HEART = register("fiery_heart", new HealthIncreaseItem("fiery_heart", 10, (HealthIncreaseItem) MONSTER_HEART));
    }

    public static class Armor {
        public static final Item DRAGON_SCALE_HELMET = register("dragon_scale_helmet", new AwakenArmorItem(AwakenArmorMaterial.DRAGON_SCALE, EquipmentSlot.HEAD, new EntityFeatureGroup()
                .addEntityAttributeMultiplier(AwakenEntityAttributes.RANGED_DAMAGE, "dragon_scale_ranged_damage", 5F / 100F)
                .addEntityAttributeMultiplier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "dragon_scale_movement_speed", 4F / 100F),
                null));
        public static final Item DRAGON_SCALE_CHESTPLATE = register("dragon_scale_chestplate", new AwakenArmorItem(AwakenArmorMaterial.DRAGON_SCALE, EquipmentSlot.CHEST));
        public static final Item DRAGON_SCALE_LEGGINGS = register("dragon_scale_leggings", new AwakenArmorItem(AwakenArmorMaterial.DRAGON_SCALE, EquipmentSlot.LEGS));
        public static final Item DRAGON_SCALE_BOOTS = register("dragon_scale_boots", new AwakenArmorItem(AwakenArmorMaterial.DRAGON_SCALE, EquipmentSlot.FEET));

        public static final Item WITHER_BONE_HELMET = register("wither_bone_helmet", new AwakenArmorItem(AwakenArmorMaterial.WITHER_BONE, EquipmentSlot.HEAD, new EntityFeatureGroup()
                .addEntityAttributeMultiplier(AwakenEntityAttributes.MELEE_DAMAGE, "wither_bone_damage", 5F / 100F)
                .addEntityAttributeAddition(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, "wither_bone_knockback_resistance", 4F / 100F),
                null));
        public static final Item WITHER_BONE_CHESTPLATE = register("wither_bone_chestplate", new AwakenArmorItem(AwakenArmorMaterial.WITHER_BONE, EquipmentSlot.CHEST));
        public static final Item WITHER_BONE_LEGGINGS = register("wither_bone_leggings", new AwakenArmorItem(AwakenArmorMaterial.WITHER_BONE, EquipmentSlot.LEGS));
        public static final Item WITHER_BONE_BOOTS = register("wither_bone_boots", new AwakenArmorItem(AwakenArmorMaterial.WITHER_BONE, EquipmentSlot.FEET));

        public static final Item SALVIUM_HEADGEAR = register("salvium_headgear", new AwakenArmorItem(AwakenArmorMaterial.SALVIUM, EquipmentSlot.HEAD, new EntityFeatureGroup()
                .addEntityAttributeMultiplier(AwakenEntityAttributes.RANGED_DAMAGE, "salvium_ranged_damage", 7F / 100F)
                .addEntityAttributeMultiplier(AwakenEntityAttributes.BOW_DAMAGE, "salvium_bow_damage", 5F / 100F)
                .addEntityAttributeMultiplier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "salvium_movement_speed", 6F / 100F),
                null));
        public static final Item SALVIUM_BERET = register("salvium_beret", new AwakenArmorItem(AwakenArmorMaterial.SALVIUM, EquipmentSlot.HEAD, new EntityFeatureGroup()
                .addEntityAttributeMultiplier(AwakenEntityAttributes.RANGED_DAMAGE, "salvium_ranged_damage", 7F / 100F)
                .addEntityAttributeMultiplier(AwakenEntityAttributes.CROSSBOW_DAMAGE, "salvium_crossbow_damage", 5F / 100F)
                .addEntityAttributeMultiplier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "salvium_movement_speed", 6F / 100F),
                null).setCustomTextureLayer("beret"));
        public static final Item SALVIUM_MASK = register("salvium_mask", new AwakenArmorItem(AwakenArmorMaterial.SALVIUM, EquipmentSlot.HEAD, new EntityFeatureGroup()
                .addEntityAttributeMultiplier(AwakenEntityAttributes.RANGED_DAMAGE, "salvium_ranged_damage", 7F / 100F)
                .addEntityAttributeMultiplier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "salvium_movement_speed", 20F / 100F),
                null).setCustomTextureLayer("mask"));
        public static final Item SALVIUM_CHESTPLATE = register("salvium_chestplate", new AwakenArmorItem(AwakenArmorMaterial.SALVIUM, EquipmentSlot.CHEST));
        public static final Item SALVIUM_LEGGINGS = register("salvium_leggings", new AwakenArmorItem(AwakenArmorMaterial.SALVIUM, EquipmentSlot.LEGS));
        public static final Item SALVIUM_BOOTS = register("salvium_boots", new AwakenArmorItem(AwakenArmorMaterial.SALVIUM, EquipmentSlot.FEET));

        public static final Item VALERIUM_HELMET = register("valerium_helmet", new AwakenArmorItem(AwakenArmorMaterial.VALERIUM, EquipmentSlot.HEAD, new EntityFeatureGroup()
                .addEntityAttributeMultiplier(AwakenEntityAttributes.MELEE_DAMAGE, "valerium_melee_damage", 7F / 100F)
                .addEntityAttributeMultiplier(AwakenEntityAttributes.SWORD_DAMAGE, "valerium_sword_damage", 5F / 100F)
                .addEntityAttributeAddition(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, "valerium_knockback_resistance", 6F / 100F),
                null));
        public static final Item VALERIUM_CAP = register("valerium_cap", new AwakenArmorItem(AwakenArmorMaterial.VALERIUM, EquipmentSlot.HEAD, new EntityFeatureGroup()
                .addEntityAttributeMultiplier(AwakenEntityAttributes.MELEE_DAMAGE, "valerium_melee_damage", 7F / 100F)
                .addEntityAttributeMultiplier(AwakenEntityAttributes.SWORD_DAMAGE, "valerium_axe_damage", 5F / 100F)
                .addEntityAttributeAddition(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, "valerium_knockback_resistance", 6F / 100F),
                null).setCustomTextureLayer("cap"));
        public static final Item VALERIUM_MASK = register("valerium_mask", new AwakenArmorItem(AwakenArmorMaterial.VALERIUM, EquipmentSlot.HEAD, new EntityFeatureGroup()
                .addEntityAttributeMultiplier(AwakenEntityAttributes.RANGED_DAMAGE, "valerium_melee_damage", 7F / 100F)
                .addEntityAttributeAddition(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, "valerium_knockback_resistance", 6F / 100F),
                null).setCustomTextureLayer("mask"));
        public static final Item VALERIUM_CHESTPLATE = register("valerium_chestplate", new AwakenArmorItem(AwakenArmorMaterial.VALERIUM, EquipmentSlot.CHEST));
        public static final Item VALERIUM_LEGGINGS = register("valerium_leggings", new AwakenArmorItem(AwakenArmorMaterial.VALERIUM, EquipmentSlot.LEGS));
        public static final Item VALERIUM_BOOTS = register("valerium_boots", new AwakenArmorItem(AwakenArmorMaterial.VALERIUM, EquipmentSlot.FEET));
    }

    public static class Pickaxes {
        public static final Item ESCAPE_PLAN = register("escape_plan", new AwakenPickaxeItem(
                AwakenToolMaterial.tool(3, 1500, 7F, 6F, 1F, 10),
                new EntityFeatureGroup().addEntityAttributeMultiplier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "escape_plan_speed", 5F / 100F)
        ));
        public static final Item SALVERIUM_PICKAXE = register("salverium_pickaxe", new AwakenPickaxeItem(AwakenToolMaterial.tool(4, 1561, 16F, 6F, 1.2F, 15, Reagents.SALVIUM_INGOT, Reagents.VALERIUM_INGOT)));
    }

    public static class Shovels {
        public static final Item ARCHAEOLOGIST_SPADE = register("archaeologist_spade", new ArchaeologistSpadeItem(AwakenToolMaterial.tool(-1, 3000, 8F, 6F, 1F, 22)));
    }

    public static class Swords {
        public static final Item RUSTY_SHANK = register("rusty_shank", new StatusEffectSwordItem(AwakenToolMaterial.sword(2500, 5F, 1.8F, 5), StatusEffects.POISON, 8 * 20, 0));
        public static final Item ATLANTEAN_SABRE = register("atlantean_sabre", new AtlanteanSabreItem(AwakenToolMaterial.sword(1500, 8F, 1.7F, 10)));
        public static final Item ASHEN_BLADE = register("ashen_blade", new StatusEffectSwordItem(AwakenToolMaterial.sword(1500, 8F, 1.7F, 10), StatusEffects.WITHER, 4 * 20, 0));
        public static final Item GLACIAL_SHARD = register("glacial_shard", new StatusEffectSwordItem(AwakenToolMaterial.sword(1500, 9F, 1.2F, 10), StatusEffects.SLOWNESS, 4 * 20, 0));
        public static final Item ENDERIAN_CUTLASS = register("enderian_cutlass", new EnderianCutlassItem(AwakenToolMaterial.sword(1500, 6F, 1.8F, 10)));
        public static final Item VALERIUM_SWORD = register("valerium_sword", new AwakenSwordItem(AwakenToolMaterial.sword(1561, 12F, 1.6F, 10, Reagents.VALERIUM_INGOT)));
    }

    public static class Axes {
        public static final Item RAIDERS_AXE = register("raiders_axe", new AwakenAxeItem(AwakenToolMaterial.sword(500, 10F, 1F, 10)));
    }

    public static class Bows {
        public static final Item CINDERED_BOW = register("cindered_bow", new FlameBowItem(512321.0D));
        public static final Item SLIMEY_BOW = register("slimey_bow", new StatusEffectBowItem(3.0D, StatusEffects.SLOWNESS, 2 * 20, 0));
        public static final Item SALVIUM_BOW = register("salvium_bow", new AwakenBowItem(4D));
    }

    public static class Trinkets {
        public static final Item BROKEN_ANKLE_CHAIN = register("broken_ankle_chain", new AwakenTrinketItem(SlotGroups.FEET, Slots.AGLET, 200, new EntityFeatureGroup()
                .addEntityAttributeMultiplier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "broken_ankle_chain_speed", -1.5F / 100)
                .addEntityAttributeMultiplier(EntityAttributes.GENERIC_ARMOR, "broken_ankle_chain_armor", 2F / 100F)
        ));
        public static final Item BROKEN_ANKLE_WEIGHTS = register("broken_ankle_weights", new AwakenTrinketItem(SlotGroups.FEET, Slots.AGLET, 200, new EntityFeatureGroup()
                .addEntityAttributeMultiplier(EntityAttributes.GENERIC_ARMOR, "broken_ankle_weights_armor", -1.5F / 100)
                .addEntityAttributeMultiplier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "broken_ankle_weights_speed", 2F / 100F)
        ));
        public static final Item GUARDIAN_AGLET = register("guardian_aglet", new AwakenTrinketItem(SlotGroups.FEET, Slots.AGLET, 200, new EntityFeatureGroup()
                .addEnchantmentModifier(Enchantments.DEPTH_STRIDER, 2)
        ));
        public static final Item GLACIAL_AGLET = register("glacial_aglet", new AwakenTrinketItem(SlotGroups.FEET, Slots.AGLET, 200, new EntityFeatureGroup()
                .addEnchantmentModifier(Enchantments.FROST_WALKER, 1)
        ));
        public static final Item GUARDIAN_CHAIN = register("guardian_chain", new AwakenTrinketItem(SlotGroups.FEET, Slots.AGLET, 200, new EntityFeatureGroup()
                .addEnchantmentModifier(Enchantments.DEPTH_STRIDER, 2)
                .addEntityAttributeMultiplier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "broken_ankle_chain_speed", -2.5F / 100)
                .addEntityAttributeMultiplier(EntityAttributes.GENERIC_ARMOR, "broken_ankle_chain_armor", 4F / 100F)
        ));
        public static final Item GLACIAL_WEIGHTS = register("glacial_weights", new AwakenTrinketItem(SlotGroups.FEET, Slots.AGLET, 200, new EntityFeatureGroup()
                .addEnchantmentModifier(Enchantments.FROST_WALKER, 1)
                .addEntityAttributeMultiplier(EntityAttributes.GENERIC_ARMOR, "broken_ankle_weights_armor", -2.5F / 100)
                .addEntityAttributeMultiplier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "broken_ankle_weights_speed", 4F / 100F)
        ));

        public static final Item CARNIVOROUS_MASK = register("carnivorous_mask", new AwakenTrinketItem(SlotGroups.HEAD, Slots.MASK, 200));
        public static final Item FARMERS_HANKERCHIEF = register("farmers_hankerchief", new AwakenTrinketItem(SlotGroups.HEAD, Slots.MASK, 200));
        public static final Item BABY_BIB = register("baby_bib", new AwakenTrinketItem(SlotGroups.HEAD, Slots.MASK, 200));
        public static final Item ADULT_BIB = register("adult_bib", new AwakenTrinketItem(SlotGroups.HEAD, Slots.MASK, 200));

        public static final Item SCORCHED_MASK = register("scorched_mask", new AwakenTrinketItem(SlotGroups.HEAD, Slots.MASK, 200));
        public static final Item SNORKEL_MASK = register("snorkel_mask", new AwakenTrinketItem(SlotGroups.HEAD, Slots.MASK, 200, new EntityFeatureGroup()
                .addEnchantmentModifier(Enchantments.RESPIRATION, 1)
        ));
        public static final Item MAGMA_VISOR = register("magma_visor", new AwakenTrinketItem(SlotGroups.HEAD, Slots.MASK, 200, new EntityFeatureGroup()
                .addStatusEffect(StatusEffects.FIRE_RESISTANCE, 0)
        ));

        public static final Item DYNAMITE_STICK = register("dynamite_stick", new AwakenTrinketItem(SlotGroups.LEGS, Slots.CHARM, 200));
        public static final Item LIGHTNING_BOTTLE = register("lightning_bottle", new AwakenTrinketItem(SlotGroups.LEGS, Slots.CHARM, 200));
        public static final Item ELECTRIFYING_DYNAMITE = register("electrifying_dynamite", new AwakenTrinketItem(SlotGroups.LEGS, Slots.CHARM, 200));

        // TODO: make the knockback EAM affect players too
        public static final Item BOXING_GLOVES = register("boxing_gloves", new AwakenTrinketItem(SlotGroups.HAND, Slots.GLOVES, 200, new EntityFeatureGroup()
                .addEntityAttributeAddition(AwakenEntityAttributes.UNARMED_DAMAGE, "boxing_gloves_unarmed_damage", 6F)
                .addEntityAttributeAddition(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, "boxing_gloves_knockback", 15F / 100F)
        ));
        public static final Item STEEL_GAUNTLET = register("steel_gauntlet", new AwakenTrinketItem(SlotGroups.HAND, Slots.GLOVES, 200, new EntityFeatureGroup()
                .addEntityAttributeAddition(AwakenEntityAttributes.UNARMED_DAMAGE, "steel_gauntlet_unarmed_damage", 14F)
                .addEntityAttributeAddition(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, "steel_gauntlet_knockback", 25F / 100F)
        ));

        public static final Item DISCORD_BELT = register("discord_belt", new AwakenTrinketItem(SlotGroups.CHEST, Slots.BELT, 200));

        public static final Item SILKY_GLOVE = register("silky_glove", new AwakenTrinketItem(SlotGroups.HAND, Slots.GLOVES, 200));

        public static final Item SHOCKWAVE_SHIELD = register("shockwave_shield", new AwakenTrinketItem(SlotGroups.LEGS, Slots.CHARM, 200));

        public static final Item RANGERS_GLOVE = register("rangers_glove", new AwakenTrinketItem(SlotGroups.HAND, Slots.GLOVES, 200));

        public static final Item COMBAT_SADDLE = register("combat_saddle", new AwakenTrinketItem(SlotGroups.CHEST, Slots.BELT, 200));

        public static final Item BONE_CROWN = register("bone_crown", new AwakenTrinketItem(SlotGroups.HEAD, AwakenSlots.HAT, 200));

        public static final Item RAIN_HAT = register("rain_hat", new AwakenTrinketItem(SlotGroups.HEAD, AwakenSlots.HAT, 200));
        public static final Item LUCKY_TACKLE = register("lucky_tackle", new AwakenTrinketItem(SlotGroups.LEGS, Slots.CHARM, 200, new EntityFeatureGroup().addEnchantmentModifier(Enchantments.LUCK_OF_THE_SEA, 1)));
        public static final Item ANCHOR_CHARM = register("anchor_charm", new AwakenTrinketItem(SlotGroups.LEGS, Slots.CHARM, 200, new EntityFeatureGroup().addEnchantmentModifier(Enchantments.LURE, 1)));

        // public static final Item JETPACK = register("jetpack", new FlightTrinketItem(SlotGroups.CHEST, Slots.CAPE, 200, null, 0.3D, 0.1D, 20, false));
        public static final Item TEST_WINGS = register("test_wings", new WingsTrinketItem(200, null, 0.315D, 0.115D, 30));
    }

    public static class BossSpawners {
        public static final Item ABOMINABLE_AMALGAM = register("abominable_amalgam", new BossSpawnerItem(AwakenEntities.ABOMINATION));
    }

    public static class Tridents {
        public static final Item FIERY_TRIDENT = register("fiery_trident", new AwakenTridentItem(16D, 500));
    }

    public static void init() {
        new Reagents();
        new Armor();
        new Pickaxes();
        new Shovels();
        new Swords();
        new Axes();
        new Bows();
        new Trinkets();
        new BossSpawners();
        new Tridents();
    }

    public static Item register(String name, Item item) {
        Registry.register(Registry.ITEM, new Identifier(Awaken.MOD_ID, name), item);

        return item;
    }

    private static Item genericItem(String name) {
        return register(name, new Item(new Item.Settings().group(ItemGroup.MATERIALS)));
    }
}
