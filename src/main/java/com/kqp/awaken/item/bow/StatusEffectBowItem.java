package com.kqp.awaken.item.bow;

import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

/**
 * Class for creating bows that apply a status effect to hit entities.
 */
public class StatusEffectBowItem extends AwakenBowItem {
    public StatusEffect effect;
    public int duration, amplifier;

    public StatusEffectBowItem(double baseDamage, StatusEffect effect, int duration, int amplifier) {
        super(baseDamage);

        this.effect = effect;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    @Override
    public void modifyProjectileEntity(PersistentProjectileEntity projectileEntity) {
        super.modifyProjectileEntity(projectileEntity);

        if (projectileEntity instanceof ArrowEntity) {
            ((ArrowEntity) projectileEntity).addEffect(new StatusEffectInstance(effect, duration, amplifier));
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(new LiteralText(String.format(
                "Applies %s %s for %.2f seconds",
                I18n.translate(effect.getName().asString()),
                I18n.translate("enchantment.level." + (amplifier + 1)),
                (duration / 20F)
        )));
    }
}
