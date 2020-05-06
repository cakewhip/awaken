package com.kqp.awaken.mixin.client.gui.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to cancel health and armor draws and draw new health bar.
 */
@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow
    private int ticks;

    @Shadow
    private int scaledWidth;

    @Shadow
    private int scaledHeight;

    private int lastTicks;
    private float lastHealth;
    private int hurtTick;

    @Redirect(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"))
    public void cancelHeartArmorDraws(InGameHud inGameHud, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        boolean mainHeartRowDrawn = u >= 16 && v == 0;
        boolean hardcoreHeartRowDrawn = u >= 16 && v == 45;
        boolean armorRowDrawn = u >= 16 && v == 9;

        if (!mainHeartRowDrawn && !hardcoreHeartRowDrawn && !armorRowDrawn) {
            inGameHud.drawTexture(matrices, x, y, u, v, width, height);
        }
    }

    @Inject(method = "renderStatusBars", at = @At("HEAD"))
    public void drawHealth(MatrixStack matrixStack, CallbackInfo callbackInfo) {
        InGameHud hud = (InGameHud) (Object) this;
        PlayerEntity playerEntity = MinecraftClient.getInstance().player;

        float health = playerEntity.getHealth() + playerEntity.getAbsorptionAmount();
        float maxHealth = playerEntity.getMaximumHealth() + playerEntity.getAbsorptionAmount();
        float healthPerc = health / maxHealth;

        int pixelPerc = (int) (90 * healthPerc);
        int fullHearts = pixelPerc / 9;
        int remainingHeart = pixelPerc % 9;

        int x = this.scaledWidth / 2 - 91;
        int y = this.scaledHeight - 39;

        if (lastHealth - health > 0) {
            hurtTick = 9;
        } else {
            hurtTick = Math.max(0, hurtTick - (ticks - lastTicks));
        }

        int bgHeartU =  16 + (hurtTick / 3) * 9;

        // Draw blank hearts
        for (int i = 0; i < 10; i++) {
            hud.drawTexture(matrixStack, x + i * 8, y, bgHeartU, 0, 9, 9);
        }

        // Draw full hearts
        for (int i = 0; i < fullHearts; i++) {
            hud.drawTexture(matrixStack, x + i * 8, y, 52, 0, 9, 9);
        }

        // Draw remaining heart
        hud.drawTexture(matrixStack, x + fullHearts * 8, y, 52, 0, remainingHeart, 9);

        Text healthText = new LiteralText(String.format("%.2f/%.2f", health, maxHealth));
        int width = hud.getFontRenderer().getStringWidth(healthText);

        hud.getFontRenderer().drawWithShadow(matrixStack, healthText, x + (41 - width) / 2, y, 0xFFFFFF);

        this.lastHealth = health;
        lastTicks = ticks;
    }
}
