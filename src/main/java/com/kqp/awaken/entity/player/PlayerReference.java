package com.kqp.awaken.entity.player;

import net.minecraft.entity.player.PlayerEntity;

public interface PlayerReference {
    PlayerEntity getPlayer();

    void setPlayer(PlayerEntity player);
}
