package com.kqp.awaken.item.pickaxe;

import com.kqp.awaken.item.effect.EffectAttributeEquippable;
import com.kqp.awaken.item.effect.SpecialItemRegistry;
import com.kqp.awaken.item.material.AwakenToolMaterial;
import com.kqp.awaken.item.tool.AwakenPickaxeItem;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

/**
 * Class for the escape plan item.
 */
public class EscapePlanItem extends AwakenPickaxeItem {
    public EscapePlanItem() {
        super(AwakenToolMaterial.PHASE_1_SPECIAL_TOOL);

        SpecialItemRegistry.EQUIPPABLE_ITEM.put(this, new EffectAttributeEquippable()
                .addStatusEffect(StatusEffects.SPEED, 1)
        );
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(new LiteralText("When equipped:"));
        tooltip.add(new LiteralText("Speed II"));
    }
}
