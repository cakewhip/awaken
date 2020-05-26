package com.kqp.awaken.item.armor;

import com.kqp.awaken.item.effect.EntityEquipmentListener;
import com.kqp.awaken.item.effect.EntityFeatureGroup;
import com.kqp.awaken.util.ArmorUtil;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class AwakenArmorItem extends ArmorItem implements EntityEquipmentListener {
    private String customTextureLayer = null;

    private final Optional<EntityFeatureGroup> itemMods, setMods;

    public AwakenArmorItem(ArmorMaterial material, EquipmentSlot slot, EntityFeatureGroup itemMods, EntityFeatureGroup setMods) {
        super(material, slot, new Item.Settings().group(ItemGroup.COMBAT));

        this.itemMods = Optional.ofNullable(itemMods);
        this.setMods = Optional.ofNullable(setMods);
    }

    public AwakenArmorItem(ArmorMaterial material, EquipmentSlot slot) {
        this(material, slot, null, null);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        itemMods.ifPresent(effects -> {
            tooltip.add(new LiteralText("When Equipped:").formatted(Formatting.GRAY));
            effects.populateTooltips(tooltip);
        });

        setMods.ifPresent(effects -> {
            tooltip.add(new LiteralText("Set Bonus:").formatted(Formatting.GRAY));
            effects.populateTooltips(tooltip);
        });
    }

    public AwakenArmorItem setCustomTextureLayer(String customTextureLayer) {
        this.customTextureLayer = customTextureLayer;

        return this;
    }

    public String getCustomTextureLayer() {
        return customTextureLayer;
    }

    @Override
    public void onEquip(LivingEntity entity, ItemStack itemStack, ItemStack equippedStack, EquipmentSlot equipmentSlot) {
        if (itemStack.getItem() == this) {
            itemMods.ifPresent(effects -> effects.applyTo(entity));
        }
    }

    @Override
    public void onUnEquip(LivingEntity entity, ItemStack itemStack, ItemStack unEquippedStack, EquipmentSlot equipmentSlot) {
        if (itemStack.getItem() == this) {
            itemMods.ifPresent(effects -> effects.removeFrom(entity));
        }
    }

    private void checkForSetBonus(LivingEntity entity, ItemStack itemStack) {
        if (ArmorUtil.wearingFullSet(entity, this.type)) {
            setMods.ifPresent(effects -> effects.applyTo(entity));
        } else {
            setMods.ifPresent(effects -> effects.removeFrom(entity));
        }
    }
}
