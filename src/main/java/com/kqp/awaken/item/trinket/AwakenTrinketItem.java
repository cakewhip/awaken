package com.kqp.awaken.item.trinket;

import com.kqp.awaken.effect.EntityFeatureGroup;
import com.kqp.awaken.effect.EntityFeatureGroupProvider;
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

import java.util.ArrayList;
import java.util.List;

public class AwakenTrinketItem extends Item implements ITrinket, EntityFeatureGroupProvider {
    public final String trinketGroup, trinketSlot;

    private final List<EntityFeatureGroup> entityFeatureGroups;

    public AwakenTrinketItem(String trinketGroup, String trinketSlot, int durability) {
        super(new Item.Settings().maxCount(1).group(ItemGroup.COMBAT).maxDamage(durability));

        this.trinketGroup = trinketGroup;
        this.trinketSlot = trinketSlot;

        this.entityFeatureGroups = new ArrayList();
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

        entityFeatureGroups.forEach(entityFeatureGroup -> {
            entityFeatureGroup.populateTooltips(tooltip);
        });
    }

    @Override
    public boolean canWearInSlot(String group, String slot) {
        return group.equals(trinketGroup) && slot.equals(trinketSlot);
    }

    @Override
    public void onEquip(PlayerEntity player, ItemStack stack) {

    }

    @Override
    public void onUnequip(PlayerEntity player, ItemStack stack) {

    }

    @Override
    public List<EntityFeatureGroup> getEntityFeatureGroups() {
        return entityFeatureGroups;
    }
}
