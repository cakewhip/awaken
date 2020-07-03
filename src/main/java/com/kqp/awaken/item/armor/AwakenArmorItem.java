package com.kqp.awaken.item.armor;

import com.kqp.awaken.effect.EntityEquipmentListener;
import com.kqp.awaken.effect.EntityFeatureGroup;
import com.kqp.awaken.effect.ActiveEntityFeatureGroupProvider;
import com.kqp.awaken.util.ArmorUtil;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AwakenArmorItem extends ArmorItem implements EntityEquipmentListener, ActiveEntityFeatureGroupProvider {
    private String customTextureLayer = null;

    public Optional<EntityFeatureGroup> itemMods, setMods;

    public AwakenArmorItem(ArmorMaterial material, EquipmentSlot slot) {
        super(material, slot, new Item.Settings().group(ItemGroup.COMBAT));

        setItemMods(null);
        setSetMods(null);
    }

    public void setItemMods(EntityFeatureGroup efg) {
        itemMods = Optional.ofNullable(efg);
    }

    public void setSetMods(EntityFeatureGroup efg) {
        setMods = Optional.ofNullable(efg);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
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
            checkForSetBonus(entity, itemStack);
        }
    }

    @Override
    public void onUnEquip(LivingEntity entity, ItemStack itemStack, ItemStack unEquippedStack, EquipmentSlot equipmentSlot) {
        if (itemStack.getItem() == this) {
            itemMods.ifPresent(effects -> effects.removeFrom(entity));
        }

        checkForSetBonus(entity, itemStack);
    }

    private void checkForSetBonus(LivingEntity entity, ItemStack itemStack) {
        if (ArmorUtil.wearingFullSet(entity, this.type)) {
            setMods.ifPresent(effects -> effects.applyTo(entity));
        } else {
            setMods.ifPresent(effects -> effects.removeFrom(entity));
        }
    }

    @Override
    public List<EntityFeatureGroup> getActiveEntityFeatureGroups(PlayerEntity player) {
        List<EntityFeatureGroup> entityFeatureGroups = new ArrayList();

        itemMods.ifPresent(entityFeatureGroups::add);

        if (ArmorUtil.wearingFullSet(player, this.type)) {
            setMods.ifPresent(entityFeatureGroups::add);
        }

        return entityFeatureGroups;
    }
}
