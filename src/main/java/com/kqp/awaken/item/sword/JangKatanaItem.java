package com.kqp.awaken.item.sword;

import com.kqp.awaken.item.effect.EffectAttributeEquippable;
import com.kqp.awaken.item.effect.SpecialItemRegistry;
import com.kqp.awaken.item.material.AwakenToolMaterial;
import com.kqp.awaken.item.tool.AwakenSwordItem;
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
 * Class for the Jang Katana.
 */
public class JangKatanaItem extends AwakenSwordItem {
    public JangKatanaItem() {
        super(AwakenToolMaterial.newSwordMaterial(1500, 51232, 10));

        SpecialItemRegistry.EQUIPPABLE_ITEM.put(this, new EffectAttributeEquippable()
                .addStatusEffect(StatusEffects.REGENERATION, 1)
                .addStatusEffect(StatusEffects.SPEED, 1)
                .addStatusEffect(StatusEffects.RESISTANCE, 1)
        );
    }

    @Override
    public boolean hasEnchantmentGlint(ItemStack stack) {
        return true;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(new LiteralText("When equipped:"));
        tooltip.add(new LiteralText("Regeneration II"));
        tooltip.add(new LiteralText("Speed II"));
        tooltip.add(new LiteralText("Resistance II"));
    }
}
