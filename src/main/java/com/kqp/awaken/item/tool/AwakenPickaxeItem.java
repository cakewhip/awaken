package com.kqp.awaken.item.tool;

import com.kqp.awaken.effect.EntityFeatureGroup;
import com.kqp.awaken.item.material.AwakenToolMaterial;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

/**
 * Class for creating pickaxes because the super constructor is protected.
 */
public class AwakenPickaxeItem extends PickaxeItem {
    public final Optional<EntityFeatureGroup> itemMods;

    public AwakenPickaxeItem(AwakenToolMaterial material, EntityFeatureGroup itemMods) {
        super(material, -1, material.getAttackSpeed() - 4, new Settings().group(ItemGroup.TOOLS));

        this.itemMods = Optional.ofNullable(itemMods);
    }

    public AwakenPickaxeItem(AwakenToolMaterial material) {
        this(material, null);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        itemMods.ifPresent(effects -> {
            tooltip.add(new LiteralText("When Equipped:"));
            effects.populateTooltips(tooltip);
        });
    }
}
