package com.kqp.terminus.item.sword;

import com.kqp.terminus.item.TerminusToolMaterial;
import com.kqp.terminus.item.tool.TerminusSwordItem;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

import java.util.List;

public class JangKatanaItem extends TerminusSwordItem {
    public JangKatanaItem() {
        super(TerminusToolMaterial.JANG_KATANA);
    }

    @Override
    public boolean hasEnchantmentGlint(ItemStack stack) {
        return true;
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.EPIC;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (selected && entity instanceof LivingEntity) {
            LivingEntity le = (LivingEntity) entity;

            le.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 1, 1));
            le.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 1, 1));
            le.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1, 1));
            le.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 1, 1));
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new LiteralText("When equipped:"));
        tooltip.add(new LiteralText("Absorption I"));
        tooltip.add(new LiteralText("Regeneration I"));
        tooltip.add(new LiteralText("Speed I"));
        tooltip.add(new LiteralText("Resistance I"));
    }
}
