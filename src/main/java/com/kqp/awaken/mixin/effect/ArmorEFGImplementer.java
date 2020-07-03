package com.kqp.awaken.mixin.effect;

import com.kqp.awaken.effect.ActiveEntityFeatureGroupProvider;
import com.kqp.awaken.effect.EntityEquipmentListener;
import com.kqp.awaken.effect.EntityFeatureGroup;
import com.kqp.awaken.item.armor.ArmorEFGMutator;
import com.kqp.awaken.util.ArmorUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(ArmorItem.class)
public class ArmorEFGImplementer implements ArmorEFGMutator, EntityEquipmentListener, ActiveEntityFeatureGroupProvider {
    public Optional<EntityFeatureGroup> itemMods = Optional.empty(), setMods = Optional.empty();

    @Override
    public void setBonus(EntityFeatureGroup efg) {
        itemMods = Optional.ofNullable(efg);
    }

    @Override
    public Optional<EntityFeatureGroup> getItemMods() {
        return itemMods;
    }

    @Override
    public void setSetBonus(EntityFeatureGroup efg) {
        setMods = Optional.ofNullable(efg);
    }

    @Override
    public Optional<EntityFeatureGroup> getSetMods() {
        return setMods;
    }

    @Override
    public void onEquip(LivingEntity entity, ItemStack itemStack, ItemStack equippedStack, EquipmentSlot equipmentSlot) {
        if (itemStack.getItem() == (Object) this) {
            itemMods.ifPresent(effects -> effects.applyTo(entity));
            checkForSetBonus(entity, itemStack);
        }
    }

    @Override
    public void onUnEquip(LivingEntity entity, ItemStack itemStack, ItemStack unEquippedStack, EquipmentSlot equipmentSlot) {
        if (itemStack.getItem() == (Object) this) {
            itemMods.ifPresent(effects -> effects.removeFrom(entity));
        }

        checkForSetBonus(entity, itemStack);
    }

    private void checkForSetBonus(LivingEntity entity, ItemStack itemStack) {
        if (ArmorUtil.wearingFullSet(entity, ((ArmorItem) (Object) this).getMaterial())) {
            setMods.ifPresent(effects -> effects.applyTo(entity));
        } else {
            setMods.ifPresent(effects -> effects.removeFrom(entity));
        }
    }

    @Override
    public List<EntityFeatureGroup> getActiveEntityFeatureGroups(PlayerEntity player) {
        List<EntityFeatureGroup> entityFeatureGroups = new ArrayList();

        itemMods.ifPresent(entityFeatureGroups::add);

        if (ArmorUtil.wearingFullSet(player, ((ArmorItem) (Object) this).getMaterial())) {
            setMods.ifPresent(entityFeatureGroups::add);
        }

        return entityFeatureGroups;
    }
}
