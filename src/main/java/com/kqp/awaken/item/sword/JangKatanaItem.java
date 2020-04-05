package com.kqp.awaken.item.sword;

import com.kqp.awaken.item.AwakenToolMaterial;
import com.kqp.awaken.item.effect.Equippable;
import com.kqp.awaken.item.effect.SpecialItemRegistry;
import com.kqp.awaken.item.tool.AwakenSwordItem;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

/**
 * Class for the Jang Katana.
 * TODO: make this a generic class that takes in a ToolMaterial and a list of status effects to apply
 */
public class JangKatanaItem extends AwakenSwordItem implements Equippable {
    private static final StatusEffectInstance REGEN_EFFECT = new StatusEffectInstance(StatusEffects.REGENERATION, Integer.MAX_VALUE, 1);
    private static final StatusEffectInstance SPEED_EFFECT = new StatusEffectInstance(StatusEffects.SPEED, Integer.MAX_VALUE, 1);
    private static final StatusEffectInstance RESISTANCE_EFFECT = new StatusEffectInstance(StatusEffects.RESISTANCE, Integer.MAX_VALUE, 1);

    public JangKatanaItem() {
        super(AwakenToolMaterial.JANG_KATANA);

        SpecialItemRegistry.EQUIPPABLE_ITEM.put(this, this);
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

    @Override
    public void equip(ItemStack itemStack, PlayerEntity player) {
        player.addStatusEffect(REGEN_EFFECT);
        player.addStatusEffect(SPEED_EFFECT);
        player.addStatusEffect(RESISTANCE_EFFECT);
    }

    @Override
    public void unEquip(ItemStack itemStack, PlayerEntity player) {
        player.removeStatusEffect(REGEN_EFFECT.getEffectType());
        player.removeStatusEffect(SPEED_EFFECT.getEffectType());
        player.removeStatusEffect(RESISTANCE_EFFECT.getEffectType());
    }
}
