package com.kqp.awaken.loot;

import net.minecraft.util.Formatting;

public enum AwakenRarity {
    TERRIBLE(Formatting.GRAY),
    OKAY(Formatting.WHITE),
    UNCOMMON(Formatting.BLUE),
    RARE(Formatting.GOLD),
    EPIC(Formatting.GREEN),
    FABLED(Formatting.AQUA),
    LEGENDARY(Formatting.LIGHT_PURPLE),
    MYTHICAL(Formatting.RED),
    DIVINE(Formatting.YELLOW);

    public final Formatting color;

    AwakenRarity(Formatting color) {
        this.color = color;
    }
}
