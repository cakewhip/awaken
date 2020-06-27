package com.kqp.awaken.client.trinket;

import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.List;

/**
 * Model class for the trinket.
 */
public class JetpackRenderer implements TrinketRenderer {
    private final ModelPart mainPiece;

    private final List<ModelPart> partList;

    private float flutterTickTimer;

    public JetpackRenderer() {
        mainPiece = new ModelPart(64, 32, 0, 0);
        this.mainPiece.addCuboid(-4.0F, 0.0F, 0.0F, 8.0F, 10.0F, 3.0F, 0F);

        partList = Arrays.asList(mainPiece);
    }


    @Override
    public void render(String slot, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, PlayerEntityModel<AbstractClientPlayerEntity> playerModel, AbstractClientPlayerEntity player, float headYaw, float headPitch) {
        ItemStack itemStack = TrinketsApi.getTrinketComponent(player).getStack(slot);
        Identifier itemId = Registry.ITEM.getId(itemStack.getItem());
        Identifier texture = new Identifier(itemId.getNamespace(), "textures/entity/trinket/" + itemId.getPath() + ".png");

        matrixStack.push();
        matrixStack.translate(0.0D, 0.0D, 0.125D);

        VertexConsumer vertexConsumer = ItemRenderer.getArmorVertexConsumer(vertexConsumers, RenderLayer.getEntityCutoutNoCull(texture), false, itemStack.hasEnchantments());
        partList.forEach(part -> {
            part.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
        });

        matrixStack.pop();
    }
}
