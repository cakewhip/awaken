package com.kqp.awaken.client.entity;

import com.kqp.awaken.client.model.AbominationEntityModel;
import com.kqp.awaken.entity.AbominationEntity;
import com.kqp.awaken.init.Awaken;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AbominationRenderer<T extends AbominationEntity, M extends BipedEntityModel<T>> extends BipedEntityRenderer<T, M> {
    private static final Identifier TEXTURE = new Identifier(Awaken.MOD_ID, "textures/entity/abomination.png");

    public AbominationRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher, (M) new AbominationEntityModel(), 4F);
    }

    public Identifier getTexture(AbominationEntity AbominationEntity) {
        return TEXTURE;
    }

    @Override
    protected void scale(T entity, MatrixStack matrices, float tickDelta) {
        matrices.scale(5F, 5F, 5F);
    }
}
