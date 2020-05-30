package com.kqp.awaken.item.trinket;

import com.kqp.awaken.item.effect.EntityFeatureGroup;
import com.kqp.awaken.item.effect.EntityFeatureGroupProvider;
import dev.emi.trinkets.api.ITrinket;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class AwakenTrinketItem extends Item implements ITrinket, EntityFeatureGroupProvider {
    public final String trinketGroup, trinketSlot;

    private final Optional<EntityFeatureGroup> itemMods;

    public AwakenTrinketItem(String trinketGroup, String trinketSlot, int durability, EntityFeatureGroup itemMods) {
        super(new Item.Settings().maxCount(1).group(ItemGroup.COMBAT).maxDamage(durability));

        this.trinketGroup = trinketGroup;
        this.trinketSlot = trinketSlot;

        this.itemMods = Optional.ofNullable(itemMods);
    }

    public AwakenTrinketItem(String trinketGroup, String trinketSlot, int durability, String jsonName) {
        this(trinketGroup, trinketSlot, durability, EntityFeatureGroup.fromJson("trinkets/" + jsonName));
    }

    public AwakenTrinketItem(String trinketGroup, String trinketSlot, int durability) {
        this(trinketGroup, trinketSlot, durability, (EntityFeatureGroup) null);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        int i = 0;
        String translationKey = this.getTranslationKey().concat("_tooltip" + i);

        while (I18n.hasTranslation(translationKey)) {
            tooltip.add(new TranslatableText(translationKey).formatted(Formatting.GRAY));

            translationKey = this.getTranslationKey().concat("_tooltip" + ++i);
        }

        itemMods.ifPresent(effects -> {
            effects.populateTooltips(tooltip);
        });
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

    public Optional<EntityFeatureGroup> getEntityFeatures() {
        return itemMods;
    }

    @Override
    public Set<EntityFeatureGroup> getEntityFeatureGroups() {
        Set<EntityFeatureGroup> entityFeatureGroups = new HashSet();

        itemMods.ifPresent(entityFeatureGroups::add);

        return entityFeatureGroups;
    }
}
