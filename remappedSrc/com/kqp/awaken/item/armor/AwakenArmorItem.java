package com.kqp.awaken.item.armor;

import com.kqp.awaken.item.effect.EffectAttributeEquippable;
import com.kqp.awaken.item.effect.SpecialItemRegistry;
import jdk.internal.jline.internal.Nullable;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class AwakenArmorItem extends ArmorItem {
    private List<String> toolTips;
    private String customTextureLayer = null;

    public AwakenArmorItem(ArmorMaterial material, EquipmentSlot slot) {
        super(material, slot, new Item.Settings().group(ItemGroup.COMBAT));
        toolTips = new ArrayList();
    }

    public AwakenArmorItem(ArmorMaterial material, EquipmentSlot slot, EffectAttributeEquippable setBonus) {
        this(material, slot);

        SpecialItemRegistry.EQUIPPABLE_ARMOR.put(this, setBonus);
    }

    public AwakenArmorItem addToolTip(String text) {
        toolTips.add(text);

        return this;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        for (String str : toolTips) {
            tooltip.add(new LiteralText(str));
        }
    }

    public AwakenArmorItem setCustomTextureLayer(String customTextureLayer) {
        this.customTextureLayer = customTextureLayer;

        return this;
    }

    public String getCustomTextureLayer() {
        return customTextureLayer;
    }
}
