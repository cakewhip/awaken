package com.kqp.awaken.init;

import com.kqp.awaken.entity.attribute.AwakenEntityAttributes;
import com.kqp.awaken.item.BossSpawnerItem;
import com.kqp.awaken.item.armor.AwakenArmorItem;
import com.kqp.awaken.item.bow.AwakenBowItem;
import com.kqp.awaken.item.bow.FlameBowItem;
import com.kqp.awaken.item.bow.StatusEffectBowItem;
import com.kqp.awaken.item.effect.SetBonusEquippable;
import com.kqp.awaken.item.health.HealthIncreaseItem;
import com.kqp.awaken.item.material.AwakenArmorMaterial;
import com.kqp.awaken.item.material.AwakenToolMaterial;
import com.kqp.awaken.item.pickaxe.EscapePlanItem;
import com.kqp.awaken.item.shovel.ArchaeologistSpadeItem;
import com.kqp.awaken.item.sword.AtlanteanSabreItem;
import com.kqp.awaken.item.sword.AwakenSwordItem;
import com.kqp.awaken.item.sword.EnderianCutlassItem;
import com.kqp.awaken.item.sword.StatusEffectSwordItem;
import com.kqp.awaken.item.tool.AwakenAxeItem;
import com.kqp.awaken.item.tool.AwakenPickaxeItem;
import com.kqp.awaken.item.trident.AwakenTridentItem;
import com.kqp.awaken.loot.AwakenRarity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AwakenItems {
    public static final Item CREEPER_TISSUE = genericItem();
    public static final Item NECROTIC_HEART = genericItem();
    public static final Item SPIDER_SILK = genericItem();
    public static final Item BONE_MARROW = genericItem();
    public static final Item MONSTER_HEART = new HealthIncreaseItem("monster_heart", 10);

    public static final Item ENDER_DRAGON_SCALE = genericItem();
    public static final Item WITHER_RIB = genericItem();

    public static final Item DRAGON_SCALE_HELMET = new AwakenArmorItem(AwakenArmorMaterial.DRAGON_SCALE, EquipmentSlot.HEAD, new SetBonusEquippable()
            .addEntityAttributeMultiplier(AwakenEntityAttributes.RANGED_DAMAGE, "dragon_scale_ranged_damage", 5F / 100F)
            .addEntityAttributeMultiplier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "dragon_scale_movement_speed", 4F / 100F))
            .addToolTip("Set bonus:").addToolTip("5% extra ranged damage").addToolTip("4% extra movement speed");
    public static final Item DRAGON_SCALE_CHESTPLATE = new AwakenArmorItem(AwakenArmorMaterial.DRAGON_SCALE, EquipmentSlot.CHEST);
    public static final Item DRAGON_SCALE_LEGGINGS = new AwakenArmorItem(AwakenArmorMaterial.DRAGON_SCALE, EquipmentSlot.LEGS);
    public static final Item DRAGON_SCALE_BOOTS = new AwakenArmorItem(AwakenArmorMaterial.DRAGON_SCALE, EquipmentSlot.FEET);

    public static final Item WITHER_BONE_HELMET = new AwakenArmorItem(AwakenArmorMaterial.WITHER_BONE, EquipmentSlot.HEAD, new SetBonusEquippable()
            .addEntityAttributeMultiplier(AwakenEntityAttributes.MELEE_DAMAGE, "wither_bone_damage", 5F / 100F)
            .addEntityAttributeAddition(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, "wither_bone_knockback_resistance", 4F / 100F))
            .addToolTip("Set bonus:").addToolTip("5% extra melee damage").addToolTip("4% knockback extra resistance");
    public static final Item WITHER_BONE_CHESTPLATE = new AwakenArmorItem(AwakenArmorMaterial.WITHER_BONE, EquipmentSlot.CHEST);
    public static final Item WITHER_BONE_LEGGINGS = new AwakenArmorItem(AwakenArmorMaterial.WITHER_BONE, EquipmentSlot.LEGS);
    public static final Item WITHER_BONE_BOOTS = new AwakenArmorItem(AwakenArmorMaterial.WITHER_BONE, EquipmentSlot.FEET);

    public static final Item CINDERED_BOW = new FlameBowItem(512321.0D).setRarity(AwakenRarity.RARE);
    public static final Item SLIMEY_BOW = new StatusEffectBowItem(3.0D, StatusEffects.SLOWNESS, 2 * 20, 0).setRarity(AwakenRarity.RARE);
    public static final Item RAIDERS_AXE = new AwakenAxeItem(AwakenToolMaterial.sword(500, 10F, 1F, 10)).setRarity(AwakenRarity.UNCOMMON);
    public static final Item ESCAPE_PLAN = new EscapePlanItem(AwakenToolMaterial.tool(-1, 3, 1500, 6F, 1F, 10)).setRarity(AwakenRarity.UNCOMMON);
    public static final Item ARCHAEOLOGIST_SPADE = new ArchaeologistSpadeItem(AwakenToolMaterial.tool(-1, 3000, 8F, 6F, 1F, 22)).setRarity(AwakenRarity.UNCOMMON);
    public static final Item RUSTY_SHANK = new StatusEffectSwordItem(AwakenToolMaterial.sword(2500, 5F, 1.8F, 5), StatusEffects.POISON, 8 * 20, 0).setRarity(AwakenRarity.UNCOMMON);

    public static final Item ATLANTEAN_SABRE = new AtlanteanSabreItem(AwakenToolMaterial.sword(1500, 8F, 1.7F, 10)).setRarity(AwakenRarity.EPIC);
    public static final Item ASHEN_BLADE = new StatusEffectSwordItem(AwakenToolMaterial.sword(1500, 8F, 1.7F, 10), StatusEffects.WITHER, 4 * 20, 0).setRarity(AwakenRarity.EPIC);
    public static final Item GLACIAL_SHARD = new StatusEffectSwordItem(AwakenToolMaterial.sword(1500, 9F, 1.2F, 10), StatusEffects.SLOWNESS, 4 * 20, 0).setRarity(AwakenRarity.EPIC);
    public static final Item ENDERIAN_CUTLASS = new EnderianCutlassItem(AwakenToolMaterial.sword(1500, 6F, 1.8F, 10)).setRarity(AwakenRarity.EPIC);


    public static final Item SMOLDERING_HEART = genericItem();
    public static final Item FIERY_CORE = genericItem();
    public static final Item MAGMA_STRAND = genericItem();
    public static final Item CINDERED_SOUL = genericItem();
    public static final Item FIERY_HEART = new HealthIncreaseItem("fiery_heart", 10, (HealthIncreaseItem) MONSTER_HEART);

    public static final Item ABOMINABLE_AMALGAM = new BossSpawnerItem(AwakenEntities.ABOMINATION);

    public static final Item CORSAIR_TOKEN = genericItem();
    public static final Item RAPTOR_CHICKEN_EGG = genericItem();
    public static final Item SALVIUM_INGOT = genericItem();
    public static final Item VALERIUM_INGOT = genericItem();

    public static final Item SALVIUM_BOW = new AwakenBowItem(4D);
    public static final Item VALERIUM_SWORD = new AwakenSwordItem(AwakenToolMaterial.sword(2031, 12F, 1.6F, 10, VALERIUM_INGOT));
    public static final Item SALVERIUM_PICKAXE = new AwakenPickaxeItem(AwakenToolMaterial.tool(4, 2031, 9F, 6F, 1.2F, 15, SALVIUM_INGOT, VALERIUM_INGOT));
    public static final Item FIERY_TRIDENT = new AwakenTridentItem(16D, 500);

    public static final Item SALVIUM_HEADGEAR = new AwakenArmorItem(AwakenArmorMaterial.SALVIUM, EquipmentSlot.HEAD, new SetBonusEquippable()
            .addEntityAttributeMultiplier(AwakenEntityAttributes.RANGED_DAMAGE, "salvium_ranged_damage", 7F / 100F)
            .addEntityAttributeMultiplier(AwakenEntityAttributes.BOW_DAMAGE, "salvium_bow_damage", 5F / 100F)
            .addEntityAttributeMultiplier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "salvium_movement_speed", 6F / 100F))
            .addToolTip("Set bonus:").addToolTip("7% extra ranged damage").addToolTip("5% extra bow damage").addToolTip("6% extra movement speed");
    public static final Item SALVIUM_BERET = new AwakenArmorItem(AwakenArmorMaterial.SALVIUM, EquipmentSlot.HEAD, new SetBonusEquippable()
            .addEntityAttributeMultiplier(AwakenEntityAttributes.RANGED_DAMAGE, "salvium_ranged_damage", 7F / 100F)
            .addEntityAttributeMultiplier(AwakenEntityAttributes.CROSSBOW_DAMAGE, "salvium_crossbow_damage", 5F / 100F)
            .addEntityAttributeMultiplier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "salvium_movement_speed", 6F / 100F))
            .addToolTip("Set bonus:").addToolTip("7% extra ranged damage").addToolTip("5% extra crossbow damage").addToolTip("6% extra movement speed")
            .setCustomTextureLayer("beret");
    public static final Item SALVIUM_MASK = new AwakenArmorItem(AwakenArmorMaterial.SALVIUM, EquipmentSlot.HEAD, new SetBonusEquippable()
            .addEntityAttributeMultiplier(AwakenEntityAttributes.RANGED_DAMAGE, "salvium_ranged_damage", 7F / 100F)
            .addEntityAttributeMultiplier(AwakenEntityAttributes.TRIDENT_DAMAGE, "salvium_trident_damage", 5F / 100F)
            .addEntityAttributeMultiplier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "salvium_movement_speed", 20F / 100F))
            .addToolTip("Set bonus:").addToolTip("7% extra ranged damage").addToolTip("5% extra trident damage").addToolTip("6% extra movement speed")
            .setCustomTextureLayer("mask");
    public static final Item SALVIUM_CHESTPLATE = new AwakenArmorItem(AwakenArmorMaterial.SALVIUM, EquipmentSlot.CHEST);
    public static final Item SALVIUM_LEGGINGS = new AwakenArmorItem(AwakenArmorMaterial.SALVIUM, EquipmentSlot.LEGS);
    public static final Item SALVIUM_BOOTS = new AwakenArmorItem(AwakenArmorMaterial.SALVIUM, EquipmentSlot.FEET);

    public static final Item VALERIUM_HELMET = new AwakenArmorItem(AwakenArmorMaterial.VALERIUM, EquipmentSlot.HEAD, new SetBonusEquippable()
            .addEntityAttributeMultiplier(AwakenEntityAttributes.MELEE_DAMAGE, "valerium_melee_damage", 7F / 100F)
            .addEntityAttributeMultiplier(AwakenEntityAttributes.SWORD_DAMAGE, "valerium_sword_damage", 5F / 100F)
            .addEntityAttributeAddition(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, "valerium_knockback_resistance", 6F / 100F))
            .addToolTip("Set bonus:").addToolTip("7% extra melee damage").addToolTip("5% extra sword damage").addToolTip("6% extra knockback resistance");
    public static final Item VALERIUM_CAP = new AwakenArmorItem(AwakenArmorMaterial.VALERIUM, EquipmentSlot.HEAD, new SetBonusEquippable()
            .addEntityAttributeMultiplier(AwakenEntityAttributes.MELEE_DAMAGE, "valerium_melee_damage", 7F / 100F)
            .addEntityAttributeMultiplier(AwakenEntityAttributes.SWORD_DAMAGE, "valerium_axe_damage", 5F / 100F)
            .addEntityAttributeAddition(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, "valerium_knockback_resistance", 6F / 100F))
            .addToolTip("Set bonus:").addToolTip("7% extra melee damage").addToolTip("5% extra axe damage").addToolTip("6% extra knockback resistance")
            .setCustomTextureLayer("cap");
    public static final Item VALERIUM_MASK = new AwakenArmorItem(AwakenArmorMaterial.VALERIUM, EquipmentSlot.HEAD, new SetBonusEquippable()
            .addEntityAttributeMultiplier(AwakenEntityAttributes.RANGED_DAMAGE, "valerium_melee_damage", 7F / 100F)
            .addEntityAttributeMultiplier(AwakenEntityAttributes.TRIDENT_DAMAGE, "valerium_trident_damage", 5F / 100F)
            .addEntityAttributeAddition(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, "valerium_knockback_resistance", 6F / 100F))
            .addToolTip("Set bonus:").addToolTip("7% extra melee damage").addToolTip("5% extra trident damage").addToolTip("6% extra knockback resistance")
            .setCustomTextureLayer("mask");
    public static final Item VALERIUM_CHESTPLATE = new AwakenArmorItem(AwakenArmorMaterial.VALERIUM, EquipmentSlot.CHEST);
    public static final Item VALERIUM_LEGGINGS = new AwakenArmorItem(AwakenArmorMaterial.VALERIUM, EquipmentSlot.LEGS);
    public static final Item VALERIUM_BOOTS = new AwakenArmorItem(AwakenArmorMaterial.VALERIUM, EquipmentSlot.FEET);

    // TODO: add unbreakable elytra using post-awakened materials (Dragon Bone Wings)

    public static void init() {
        Awaken.info("Initializing items");

        // Phase 1
        {
            // Reagents
            register(CREEPER_TISSUE, "creeper_tissue");
            register(NECROTIC_HEART, "necrotic_heart");
            register(SPIDER_SILK, "spider_silk");
            register(BONE_MARROW, "bone_marrow");
            register(MONSTER_HEART, "monster_heart");

            register(ENDER_DRAGON_SCALE, "ender_dragon_scale");
            register(WITHER_RIB, "wither_rib");

            // Special Tools
            register(CINDERED_BOW, "cindered_bow");
            register(SLIMEY_BOW, "slimey_bow");
            register(RAIDERS_AXE, "raiders_axe");
            register(ESCAPE_PLAN, "escape_plan");
            register(ARCHAEOLOGIST_SPADE, "archaeologist_spade");
            register(RUSTY_SHANK, "rusty_shank");

            // Special Swords
            register(ATLANTEAN_SABRE, "atlantean_sabre");
            register(ASHEN_BLADE, "ashen_blade");
            register(GLACIAL_SHARD, "glacial_shard");
            register(ENDERIAN_CUTLASS, "enderian_cutlass");

            // Armor
            register(DRAGON_SCALE_HELMET, "dragon_scale_helmet");
            register(DRAGON_SCALE_CHESTPLATE, "dragon_scale_chestplate");
            register(DRAGON_SCALE_LEGGINGS, "dragon_scale_leggings");
            register(DRAGON_SCALE_BOOTS, "dragon_scale_boots");

            register(WITHER_BONE_HELMET, "wither_bone_helmet");
            register(WITHER_BONE_CHESTPLATE, "wither_bone_chestplate");
            register(WITHER_BONE_LEGGINGS, "wither_bone_leggings");
            register(WITHER_BONE_BOOTS, "wither_bone_boots");
        }

        // Phase 2
        {
            // Reagents
            register(SMOLDERING_HEART, "smoldering_heart");
            register(FIERY_CORE, "fiery_core");
            register(MAGMA_STRAND, "magma_strand");
            register(CINDERED_SOUL, "cindered_soul");
            register(FIERY_HEART, "fiery_heart");

            register(ABOMINABLE_AMALGAM, "abominable_amalgam");

            register(CORSAIR_TOKEN, "corsair_token");
            register(RAPTOR_CHICKEN_EGG, "raptor_chicken_egg");
            register(SALVIUM_INGOT, "salvium_ingot");
            register(VALERIUM_INGOT, "valerium_ingot");

            // Weapons
            register(FIERY_TRIDENT, "fiery_trident");

            // Armor
            register(SALVIUM_HEADGEAR, "salvium_headgear");
            register(SALVIUM_BERET, "salvium_beret");
            register(SALVIUM_MASK, "salvium_mask");
            register(SALVIUM_CHESTPLATE, "salvium_chestplate");
            register(SALVIUM_LEGGINGS, "salvium_leggings");
            register(SALVIUM_BOOTS, "salvium_boots");

            register(VALERIUM_HELMET, "valerium_helmet");
            register(VALERIUM_CAP, "valerium_cap");
            register(VALERIUM_MASK, "valerium_mask");
            register(VALERIUM_CHESTPLATE, "valerium_chestplate");
            register(VALERIUM_LEGGINGS, "valerium_leggings");
            register(VALERIUM_BOOTS, "valerium_boots");
        }
    }

    public static void register(Item item, String name) {
        Registry.register(Registry.ITEM, new Identifier(Awaken.MOD_ID, name), item);
    }

    private static Item genericItem() {
        return new Item(new Item.Settings().group(ItemGroup.MATERIALS));
    }
}
