package com.kqp.awaken.item.armor;

import com.kqp.awaken.item.effect.EffectAttributeEquippable;
import com.kqp.awaken.item.effect.SetBonusEquippable;
import com.kqp.awaken.item.effect.SpecialItemRegistry;
import jdk.internal.jline.internal.Nullable;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

public class AwakenArmorItem extends ArmorItem {
    private String[] customToolTips;

    public AwakenArmorItem(ArmorMaterial material, EquipmentSlot slot, String... customToolTips) {
        super(material, slot, new Item.Settings().group(ItemGroup.COMBAT));
        this.customToolTips = customToolTips;
    }

    public AwakenArmorItem(ArmorMaterial material, EquipmentSlot slot, EffectAttributeEquippable setBonus, String... customToolTips) {
        this(material, slot, customToolTips);

        SpecialItemRegistry.EQUIPPABLE_ARMOR.put(this, setBonus);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        for (String str : customToolTips) {
            tooltip.add(new LiteralText(str));
        }
    }
}
