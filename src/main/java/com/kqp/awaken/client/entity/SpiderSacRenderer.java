package com.kqp.awaken.client.entity;

import com.kqp.awaken.client.model.SpiderSacEntityModel;
import com.kqp.awaken.entity.mob.SpiderSacEntity;
import com.kqp.awaken.init.Awaken;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

/**
 * Renderer for the spider sac.
 */
public class SpiderSacRenderer<T extends SpiderSacEntity> extends MobEntityRenderer<T, SpiderSacEntityModel<T>> {
    public SpiderSacRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager, new SpiderSacEntityModel(), 0.5F);
    }

    @Override
    public Identifier getTexture(T entity) {
        return Awaken.id("textures/entity/spider_sac.png");
    }
}
