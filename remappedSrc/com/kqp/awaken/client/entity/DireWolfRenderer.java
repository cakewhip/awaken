package com.kqp.awaken.client.entity;

import com.kqp.awaken.client.model.DireWolfEntityModel;
import com.kqp.awaken.entity.DireWolfEntity;
import com.kqp.awaken.init.Awaken;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

/**
 * Renderer for the dire wolf.
 */
public class DireWolfRenderer<T extends DireWolfEntity> extends MobEntityRenderer<T, DireWolfEntityModel<T>> {
    public DireWolfRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager, new DireWolfEntityModel(), 0.5F);
    }

    @Override
    public Identifier getTexture(T entity) {
        return new Identifier(Awaken.MOD_ID, "textures/entity/dire_wolf.png");
    }

    @Override
    protected void scale(T entity, MatrixStack matrices, float tickDelta) {
        matrices.scale(1.75F, 1.75F, 1.8F);
    }
}
