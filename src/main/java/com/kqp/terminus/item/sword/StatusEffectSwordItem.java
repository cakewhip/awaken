package com.kqp.terminus.item.sword;

import com.kqp.terminus.item.tool.TerminusSwordItem;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

/**
 * Generic sword class for swords that apply a status effect to their target.
 */
public class StatusEffectSwordItem extends TerminusSwordItem {
    public StatusEffect effect;
    public int duration, amplifier;

    public StatusEffectSwordItem(ToolMaterial material, StatusEffect effect, int duration, int amplifier) {
        super(material);

        this.effect = effect;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.addStatusEffect(new StatusEffectInstance(effect, duration, amplifier));

        return true;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new LiteralText(String.format(
                "Applies %s %s for %.2f seconds",
                effect.getName().asFormattedString(),
                I18n.translate("enchantment.level." + (amplifier + 1)),
                (duration / 20F)
        )));
    }
}
