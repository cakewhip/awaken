package com.kqp.awaken.util;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.List;

public class TooltipUtil {
    public static void addIterableTooltips(List<Text> tooltip, String baseKey, Formatting color) {
        int i = 0;
        String translationKey = baseKey.concat(".tooltip" + i);

        while (I18n.hasTranslation(translationKey)) {
            tooltip.add(new TranslatableText(translationKey).formatted(color));

            translationKey = baseKey.concat(".tooltip" + ++i);
        }
    }
}
