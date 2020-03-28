package com.kqp.terminus.loot;

import net.minecraft.util.Formatting;

public enum TerminusRarity {
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

    TerminusRarity(Formatting color) {
        this.color = color;
    }
}
