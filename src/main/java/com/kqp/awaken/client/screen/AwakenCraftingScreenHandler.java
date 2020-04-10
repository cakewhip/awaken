package com.kqp.awaken.client.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;

public class AwakenCraftingScreenHandler extends ScreenHandler {
    protected AwakenCraftingScreenHandler(int syncId) {
        super(null, syncId);
        // TODO: figure out screnhandler type
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
