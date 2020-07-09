package com.kqp.awaken.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.mob.MobEntity;

@Environment(EnvType.CLIENT)
public class AwakenBipedModel<T extends MobEntity> extends BipedEntityModel<T> {
    public AwakenBipedModel(float scale, boolean armorLayer) {
        super(scale, 0F, 64, armorLayer ? 32 : 64);
    }
}

