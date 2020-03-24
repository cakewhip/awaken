package com.kqp.terminus.client.screen;

import com.kqp.terminus.client.container.CelestialAltarContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CelestialAltarScreen extends ContainerScreen<CelestialAltarContainer> {
    private static final Identifier TEXTURE = new Identifier("terminus", "textures/gui/container/celestial_altar.png");

    public CelestialAltarScreen(CelestialAltarContainer container, PlayerInventory playerInventory, Text title) {
        super(container, playerInventory, title);
        this.containerHeight = 114 + 3 * 18;
    }

    @Override
    protected void drawForeground(int mouseX, int mouseY) {
        this.font.draw(this.title.asFormattedString(), 8.0F, 6.0F, 4210752);
        this.font.draw(this.playerInventory.getDisplayName().asFormattedString(), 8.0F, (float)(this.containerHeight - 96 + 2), 4210752);
    }

    @Override
    protected void drawBackground(float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        int i = (this.width - this.containerWidth) / 2;
        int j = (this.height - this.containerHeight) / 2;
        this.blit(i, j, 0, 0, this.containerWidth, 6 * 18 + 17);
        this.blit(i, j + 6 * 18 + 17, 0, 126, this.containerWidth, 96);
    }
}
