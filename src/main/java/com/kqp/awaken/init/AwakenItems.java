package com.kqp.awaken.init;

import com.kqp.awaken.entity.attribute.TEntityAttributes;
import com.kqp.awaken.group.ArmorGroup;
import com.kqp.awaken.group.ToolGroup;
import com.kqp.awaken.item.bow.FlameBowItem;
import com.kqp.awaken.item.bow.StatusEffectBowItem;
import com.kqp.awaken.item.effect.SetBonusEquippable;
import com.kqp.awaken.item.effect.SpecialItemRegistry;
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
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AwakenItems {
    public static final Item ENDER_DRAGON_SCALE = genericItem();
    public static final Item WITHER_RIB = genericItem();

    public static ArmorGroup WITHER_SCALE_ARMOR;

    public static final Item CINDERED_BOW = new FlameBowItem(4.0D, false).setRarity(AwakenRarity.RARE);
    public static final Item SLIMEY_BOW = new StatusEffectBowItem(3.0D, false, StatusEffects.SLOWNESS, 2 * 20, 1).setRarity(AwakenRarity.RARE);
    public static final Item RAIDERS_AXE = new AwakenAxeItem(AwakenToolMaterial.PHASE_1_SPECIAL_TOOL).setRarity(AwakenRarity.UNCOMMON);
    public static final Item ESCAPE_PLAN = new EscapePlanItem().setRarity(AwakenRarity.UNCOMMON);
    public static final Item ARCHAEOLOGIST_SPADE = new ArchaeologistSpadeItem().setRarity(AwakenRarity.UNCOMMON);
    public static final Item RUSTY_SHANK = new StatusEffectSwordItem(AwakenToolMaterial.PHASE_1_SPECIAL_TOOL, StatusEffects.POISON, 8 * 20, 0).setRarity(AwakenRarity.UNCOMMON);

    public static final Item ATLANTEAN_SABRE = new AtlanteanSabreItem().setRarity(AwakenRarity.EPIC);
    public static final Item ASHEN_BLADE = new StatusEffectSwordItem(AwakenToolMaterial.PHASE_1_SPECIAL_SWORD, StatusEffects.WITHER, 4 * 20, 1).setRarity(AwakenRarity.EPIC);
    public static final Item GLACIAL_SHARD = new StatusEffectSwordItem(AwakenToolMaterial.PHASE_1_SPECIAL_SWORD, StatusEffects.SLOWNESS, 4 * 20, 1).setRarity(AwakenRarity.EPIC);
    public static final Item ENDERIAN_CUTLASS = new EnderianCutlassItem().setRarity(AwakenRarity.EPIC);
    public static final Item JANG_KATANA = new JangKatanaItem().setRarity(AwakenRarity.FABLED);


    public static final Item RAPTOR_CHICKEN_EGG = genericItem();
    public static final Item SALVIUM_INGOT = genericItem();
    public static final Item VALERIUM_INGOT = genericItem();

    public static ArmorGroup SALVIUM_ARMOR;
    public static ArmorGroup VALERIUM_ARMOR;


    public static final Item CELESTIAL_STEEL_INGOT = genericItem();

    public static final Item SUNSTONE_FRAGMENT = genericItem();
    public static final Item MOONSTONE_FRAGMENT = genericItem();

    public static ToolGroup CELESTIAL_STEEL_TOOLS;
    public static ArmorGroup CELESTIAL_STEEL_ARMOR;


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
            WITHER_SCALE_ARMOR = new ArmorGroup("wither_scale", AwakenArmorMaterial.WITHER_SCALE);
        }

        // Phase 2
        {
            // Reagents
            register(RAPTOR_CHICKEN_EGG, "raptor_chicken_egg");
            register(SALVIUM_INGOT, "salvium_ingot");
            register(VALERIUM_INGOT, "valerium_ingot");

            // Armor
            SALVIUM_ARMOR = new ArmorGroup("salvium", AwakenArmorMaterial.SALVIUM, "Set bonus: 15% extra ranged damage");
            SpecialItemRegistry.addArmor(SALVIUM_ARMOR, new SetBonusEquippable()
                    .addEntityAttributeModifier(TEntityAttributes.RANGED_DAMAGE, "salvium_set_bonus", 0.15D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
            );

            VALERIUM_ARMOR = new ArmorGroup("valerium", AwakenArmorMaterial.VALERIUM, "Set bonus: 15% extra melee damage");
            SpecialItemRegistry.addArmor(VALERIUM_ARMOR, new SetBonusEquippable()
                    .addEntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "valerium_set_bonus", 0.15D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
            );
        }

        // Phase 3
        {
            // Reagents
            register(SUNSTONE_FRAGMENT, "sunstone_fragment");
            register(MOONSTONE_FRAGMENT, "moonstone_fragment");
            register(CELESTIAL_STEEL_INGOT, "celestial_steel_ingot");

            // Tools
            CELESTIAL_STEEL_TOOLS = new ToolGroup("celestial_steel", AwakenToolMaterial.CELESTIAL_STEEL);

            // Armor
            CELESTIAL_STEEL_ARMOR = new ArmorGroup("celestial_steel", AwakenArmorMaterial.CELESTIAL_STEEL);
        }
    }

    public static void register(Item item, String name) {
        Registry.register(Registry.ITEM, new Identifier(Awaken.MOD_ID, name), item);
    }

    private static Item genericItem() {
        return new Item(new Item.Settings().group(ItemGroup.MATERIALS));
    }
}
