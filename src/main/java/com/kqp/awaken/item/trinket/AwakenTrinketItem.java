package com.kqp.awaken.item.trinket;

import com.kqp.awaken.effect.ActiveEntityFeatureGroupProvider;
import com.kqp.awaken.effect.EntityFeatureGroup;
import com.kqp.awaken.util.TooltipUtil;
import dev.emi.trinkets.api.Trinket;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

public class AwakenTrinketItem extends Item implements Trinket, ActiveEntityFeatureGroupProvider {
    public final String trinketGroup, trinketSlot;

    private final EntityFeatureGroup entityFeatureGroup;

    public AwakenTrinketItem(String trinketGroup, String trinketSlot, EntityFeatureGroup entityFeatureGroup) {
        super(new Item.Settings().maxCount(1).group(ItemGroup.COMBAT));

        this.trinketGroup = trinketGroup;
        this.trinketSlot = trinketSlot;

        this.entityFeatureGroup = entityFeatureGroup;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        int size = tooltip.size();
        TooltipUtil.addIterableTooltips(tooltip, this.getTranslationKey(), Formatting.YELLOW);

        // Only add divider if there were iterable tooltips to add
        if (size != tooltip.size()) {
            tooltip.add(new LiteralText(""));
        }

        tooltip.add(new LiteralText("When Equipped:").formatted(Formatting.GRAY));
        entityFeatureGroup.populateTooltips(tooltip);
    }

    @Override
    public boolean canWearInSlot(String group, String slot) {
        return group.equals(trinketGroup) && slot.equals(trinketSlot);
    }

    @Override
    public void onEquip(PlayerEntity player, ItemStack stack) {
        entityFeatureGroup.applyTo(player);
    }

    @Override
    public void onUnequip(PlayerEntity player, ItemStack stack) {
        entityFeatureGroup.removeFrom(player);
    }

    @Override
    public List<EntityFeatureGroup> getActiveEntityFeatureGroups(PlayerEntity player) {
        return Arrays.asList(entityFeatureGroup);
    }
}
