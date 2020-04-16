package com.kqp.awaken.init;

import com.kqp.awaken.entity.attribute.AwakenEntityAttributes;
import com.kqp.awaken.item.armor.ArmorSetBonus;
import com.kqp.awaken.item.armor.AwakenArmorItem;
import com.kqp.awaken.item.bow.FlameBowItem;
import com.kqp.awaken.item.bow.StatusEffectBowItem;
import com.kqp.awaken.item.effect.SetBonusEquippable;
import com.kqp.awaken.item.material.AwakenArmorMaterial;
import com.kqp.awaken.item.material.AwakenToolMaterial;
import com.kqp.awaken.item.pickaxe.EscapePlanItem;
import com.kqp.awaken.item.shovel.ArchaeologistSpadeItem;
import com.kqp.awaken.item.sword.AtlanteanSabreItem;
import com.kqp.awaken.item.sword.EnderianCutlassItem;
import com.kqp.awaken.item.sword.JangKatanaItem;
import com.kqp.awaken.item.sword.StatusEffectSwordItem;
import com.kqp.awaken.item.tool.AwakenAxeItem;
import com.kqp.awaken.loot.AwakenRarity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AwakenItems {
    public static final Item ENDER_DRAGON_SCALE = genericItem();
    public static final Item WITHER_RIB = genericItem();

    public static final Item DRAGON_SCALE_HELMET = new AwakenArmorItem(AwakenArmorMaterial.DRAGON_SCALE, EquipmentSlot.HEAD, new SetBonusEquippable()
            .addEntityAttributeModifier(AwakenEntityAttributes.RANGED_DAMAGE, "dragon_scale_ranged_damage", 0.05F))
            .addToolTip("Set bonus: 5% extra ranged damage");
    public static final Item DRAGON_SCALE_CHESTPLATE = new AwakenArmorItem(AwakenArmorMaterial.DRAGON_SCALE, EquipmentSlot.CHEST);
    public static final Item DRAGON_SCALE_LEGGINGS = new AwakenArmorItem(AwakenArmorMaterial.DRAGON_SCALE, EquipmentSlot.LEGS);
    public static final Item DRAGON_SCALE_BOOTS = new AwakenArmorItem(AwakenArmorMaterial.DRAGON_SCALE, EquipmentSlot.FEET);

    public static final Item WITHER_BONE_HELMET = new AwakenArmorItem(AwakenArmorMaterial.WITHER_BONE, EquipmentSlot.HEAD, new SetBonusEquippable()
            .addEntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "wither_bone_damage", 0.05F))
            .addToolTip("Set bonus: 5% extra melee damage");
    public static final Item WITHER_BONE_CHESTPLATE = new AwakenArmorItem(AwakenArmorMaterial.WITHER_BONE, EquipmentSlot.CHEST);
    public static final Item WITHER_BONE_LEGGINGS = new AwakenArmorItem(AwakenArmorMaterial.WITHER_BONE, EquipmentSlot.LEGS);
    public static final Item WITHER_BONE_BOOTS = new AwakenArmorItem(AwakenArmorMaterial.WITHER_BONE, EquipmentSlot.FEET);

    public static final Item CINDERED_BOW = new FlameBowItem(5.0D, false).setRarity(AwakenRarity.RARE);
    public static final Item SLIMEY_BOW = new StatusEffectBowItem(3.0D, false, StatusEffects.SLOWNESS, 2 * 20, 0).setRarity(AwakenRarity.RARE);
    public static final Item RAIDERS_AXE = new AwakenAxeItem(AwakenToolMaterial.newSwordMaterial(1500, 4F, 5)).setRarity(AwakenRarity.UNCOMMON);
    public static final Item ESCAPE_PLAN = new EscapePlanItem(AwakenToolMaterial.newToolMaterial(3, 1500, 8F, 1F, 10)).setRarity(AwakenRarity.UNCOMMON);
    public static final Item ARCHAEOLOGIST_SPADE = new ArchaeologistSpadeItem(AwakenToolMaterial.newToolMaterial(0, 3000, 8F, 3F, 22)).setRarity(AwakenRarity.UNCOMMON);
    public static final Item RUSTY_SHANK = new StatusEffectSwordItem(AwakenToolMaterial.newSwordMaterial(1000, 3F, 5), StatusEffects.POISON, 8 * 20, 0).setRarity(AwakenRarity.UNCOMMON);

    public static final Item ATLANTEAN_SABRE = new AtlanteanSabreItem(AwakenToolMaterial.newSwordMaterial(1500, 5, 10)).setRarity(AwakenRarity.EPIC);
    public static final Item ASHEN_BLADE = new StatusEffectSwordItem(AwakenToolMaterial.newSwordMaterial(1500, 5, 10), StatusEffects.WITHER, 4 * 20, 0).setRarity(AwakenRarity.EPIC);
    public static final Item GLACIAL_SHARD = new StatusEffectSwordItem(AwakenToolMaterial.newSwordMaterial(1500, 5, 10), StatusEffects.SLOWNESS, 4 * 20, 0).setRarity(AwakenRarity.EPIC);
    public static final Item ENDERIAN_CUTLASS = new EnderianCutlassItem(AwakenToolMaterial.newSwordMaterial(1500, 6, 10)).setRarity(AwakenRarity.EPIC);
    public static final Item JANG_KATANA = new JangKatanaItem().setRarity(AwakenRarity.FABLED);


    public static final Item RAPTOR_CHICKEN_EGG = genericItem();
    public static final Item SALVIUM_INGOT = genericItem();
    public static final Item VALERIUM_INGOT = genericItem();
    
    public static final Item SALVIUM_HEADGEAR = new AwakenArmorItem(AwakenArmorMaterial.SALVIUM, EquipmentSlot.HEAD, new SetBonusEquippable()
            .addEntityAttributeModifier(AwakenEntityAttributes.BOW_DAMAGE, "salvium_bow_damage", 0.08F))
            .addToolTip("Set bonus: 8% extra bow damage");
    public static final Item SALVIUM_BERET = new AwakenArmorItem(AwakenArmorMaterial.SALVIUM, EquipmentSlot.HEAD, new SetBonusEquippable()
            .addEntityAttributeModifier(AwakenEntityAttributes.CROSSBOW_DAMAGE, "salvium_crossbow_damage", 0.08F))
            .addToolTip("Set bonus: 8% extra crossbow damage")
            .setCustomTextureLayer("beret");;
    public static final Item SALVIUM_CHESTPLATE = new AwakenArmorItem(AwakenArmorMaterial.SALVIUM, EquipmentSlot.CHEST);
    public static final Item SALVIUM_LEGGINGS = new AwakenArmorItem(AwakenArmorMaterial.SALVIUM, EquipmentSlot.LEGS);
    public static final Item SALVIUM_BOOTS = new AwakenArmorItem(AwakenArmorMaterial.SALVIUM, EquipmentSlot.FEET);

    public static final Item VALERIUM_HELMET = new AwakenArmorItem(AwakenArmorMaterial.VALERIUM, EquipmentSlot.HEAD, new SetBonusEquippable()
            .addEntityAttributeModifier(AwakenEntityAttributes.SWORD_DAMAGE, "valerium_sword_damage", 0.08F))
            .addToolTip("Set bonus: 8% extra sword damage");
    public static final Item VALERIUM_MASK = new AwakenArmorItem(AwakenArmorMaterial.VALERIUM, EquipmentSlot.HEAD, new SetBonusEquippable()
            .addEntityAttributeModifier(AwakenEntityAttributes.TRIDENT_DAMAGE, "valerium_trident_damage", 0.08F))
            .addToolTip("Set bonus: 8% extra trident damage")
            .setCustomTextureLayer("mask");
    public static final Item VALERIUM_CHESTPLATE = new AwakenArmorItem(AwakenArmorMaterial.VALERIUM, EquipmentSlot.CHEST);
    public static final Item VALERIUM_LEGGINGS = new AwakenArmorItem(AwakenArmorMaterial.VALERIUM, EquipmentSlot.LEGS);
    public static final Item VALERIUM_BOOTS = new AwakenArmorItem(AwakenArmorMaterial.VALERIUM, EquipmentSlot.FEET);

    public static final Item CELESTIAL_STEEL_INGOT = genericItem();

    public static final Item SUNSTONE_FRAGMENT = genericItem();
    public static final Item MOONSTONE_FRAGMENT = genericItem();


    // TODO: add unbreakable elytra using post-awakened materials (Dragon Bone Wings)

    public static void init() {
        Awaken.info("Initializing items");

        // Phase 1
        {
            // Reagents
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
            register(JANG_KATANA, "jang_katana");

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
            register(RAPTOR_CHICKEN_EGG, "raptor_chicken_egg");
            register(SALVIUM_INGOT, "salvium_ingot");
            register(VALERIUM_INGOT, "valerium_ingot");

            // Armor
            register(SALVIUM_HEADGEAR, "salvium_headgear");
            register(SALVIUM_BERET, "salvium_beret");
            register(SALVIUM_CHESTPLATE, "salvium_chestplate");
            register(SALVIUM_LEGGINGS, "salvium_leggings");
            register(SALVIUM_BOOTS, "salvium_boots");

            register(VALERIUM_HELMET, "valerium_helmet");
            register(VALERIUM_MASK, "valerium_mask");
            register(VALERIUM_CHESTPLATE, "valerium_chestplate");
            register(VALERIUM_LEGGINGS, "valerium_leggings");
            register(VALERIUM_BOOTS, "valerium_boots");
        }

        // Phase 3
        {
            // Reagents
            register(SUNSTONE_FRAGMENT, "sunstone_fragment");
            register(MOONSTONE_FRAGMENT, "moonstone_fragment");
            register(CELESTIAL_STEEL_INGOT, "celestial_steel_ingot");
        }
    }

    public static void register(Item item, String name) {
        Registry.register(Registry.ITEM, new Identifier(Awaken.MOD_ID, name), item);
    }

    private static Item genericItem() {
        return new Item(new Item.Settings().group(ItemGroup.MATERIALS));
    }
}
