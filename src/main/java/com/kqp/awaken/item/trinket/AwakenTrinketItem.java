package com.kqp.awaken.item.trinket;

import com.kqp.awaken.item.effect.EntityFeatureGroup;
import dev.emi.trinkets.api.ITrinket;
import dev.emi.trinkets.api.TrinketComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.Optional;

public class AwakenTrinketItem extends Item implements ITrinket {
    public final String trinketGroup, trinketSlot;

    private final Optional<EntityFeatureGroup> itemMods;

    public AwakenTrinketItem(String trinketGroup, String trinketSlot, int durability, EntityFeatureGroup itemMods) {
        super(new Item.Settings().maxCount(1).group(ItemGroup.COMBAT).maxDamage(durability));

        this.trinketGroup = trinketGroup;
        this.trinketSlot = trinketSlot;

        this.itemMods = Optional.ofNullable(itemMods);
    }

    public AwakenTrinketItem(String trinketGroup, String trinketSlot, int durability) {
        this(trinketGroup, trinketSlot, durability, null);
    }

    @Override
    public boolean canWearInSlot(String group, String slot) {
        return group.equals(trinketGroup) && slot.equals(trinketSlot);
    }

    @Override
    public void onEquip(PlayerEntity player, ItemStack stack) {
        itemMods.ifPresent(effects -> effects.applyTo(player));
    }

    @Override
    public void onUnequip(PlayerEntity player, ItemStack stack) {
        itemMods.ifPresent(effects -> effects.removeFrom(player));
    }
}
