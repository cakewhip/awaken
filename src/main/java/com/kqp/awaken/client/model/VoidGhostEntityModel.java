package com.kqp.awaken.client.model;

import com.kqp.awaken.entity.mob.AbominationEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.BipedEntityModel;

@Environment(EnvType.CLIENT)
public class VoidGhostEntityModel<T extends AbominationEntity> extends BipedEntityModel<T> {
    public VoidGhostEntityModel() {
        super(1F, 0F, 64, 64);
    }
}
