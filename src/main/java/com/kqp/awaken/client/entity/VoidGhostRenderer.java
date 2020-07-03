package com.kqp.awaken.client.entity;

import com.kqp.awaken.client.model.VoidGhostEntityModel;
import com.kqp.awaken.entity.mob.VoidGhostEntity;
import com.kqp.awaken.init.Awaken;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.util.Identifier;

/**
 * TODO: add shaking when near light
 *
 * @param <T>
 * @param <M>
 */
@Environment(EnvType.CLIENT)
public class VoidGhostRenderer<T extends VoidGhostEntity, M extends BipedEntityModel<T>> extends BipedEntityRenderer<T, M> {
    private static final Identifier TEXTURE = Awaken.id("textures/entity/void_ghost.png");

    public VoidGhostRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher, (M) new VoidGhostEntityModel(), 0.5F);
    }

    public Identifier getTexture(VoidGhostEntity VoidGhostEntity) {
        return TEXTURE;
    }
}
