package com.kqp.awaken.client.entity;

import com.kqp.awaken.client.model.AwakenBipedModel;
import com.kqp.awaken.client.model.VoidGhostEntityModel;
import com.kqp.awaken.entity.mob.VoidGhostEntity;
import com.kqp.awaken.init.Awaken;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class GenericBipedRenderer<T extends MobEntity, M extends AwakenBipedModel<T>> extends BipedEntityRenderer<T, M> {
    public final Identifier texture;

    public GenericBipedRenderer(EntityRenderDispatcher dispatcher, String path) {
        super(dispatcher, (M) new AwakenBipedModel(0F, false), 0.5F);

        this.addFeature(new ArmorFeatureRenderer(
                this,
                new AwakenBipedModel(0.5F, true),
                new AwakenBipedModel(1F, true)
        ));

                this.texture = Awaken.id("textures/entity/" + path + ".png");
    }

    @Override
    public Identifier getTexture(T mobEntity) {
        return texture;
    }
}
