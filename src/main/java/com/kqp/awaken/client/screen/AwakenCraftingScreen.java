package com.kqp.awaken.client.screen;

import com.kqp.awaken.client.slot.AwakenCraftingResultSlot;
import com.kqp.awaken.client.slot.AwakenLookUpResultSlot;
import com.kqp.awaken.init.Awaken;
import com.kqp.awaken.init.AwakenNetworking;
import com.kqp.awaken.recipe.AwakenRecipe;
import com.kqp.awaken.recipe.Reagent;
import com.kqp.awaken.recipe.RecipeType;
import com.kqp.awaken.screen.AwakenCraftingScreenHandler;
import com.kqp.awaken.util.MouseUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.StringRenderable;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Screen for Awaken's crafting system.
 */
@Environment(EnvType.CLIENT)
public class AwakenCraftingScreen extends HandledScreen<AwakenCraftingScreenHandler> {
    public static final String TITLE_TRANSLATION_KEY = Util.createTranslationKey("gui", Awaken.id("awaken_crafting"));
    public static final String RECIPE_LOOK_UP_TRANSLATION_KEY = Util.createTranslationKey("gui", Awaken.id("awaken_crafting_recipe_look_up"));
    private static final Identifier TEXTURE = Awaken.id("textures/gui/crafting_2.png");

    /**
     * Position of the crafting outputs scroll bar.
     */
    public float outputScrollPosition = 0.0F;

    /**
     * Position of the recipe look-up scroll bar.
     */
    public float lookUpScrollPosition = 0.0F;

    public AwakenCraftingScreen(AwakenCraftingScreenHandler screenHandler, PlayerInventory playerInventory) {
        super(screenHandler, playerInventory, new TranslatableText(TITLE_TRANSLATION_KEY));
        this.backgroundWidth = 256;
        this.backgroundHeight = 166;
        this.passEvents = false;

        syncCraftingResultScrollbar();
    }

    /**
     * Overriden to render the tooltips for the tabs.
     *
     * @param matrices
     * @param mouseX
     * @param mouseY
     * @param delta
     */
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);

        double aX = mouseX - this.x;
        double aY = mouseY - this.y;

        if (aY > -24 && aY < 0) {
            if (aX > 0 && aX < 28) {
                this.renderTooltip(matrices, Arrays.asList(new LiteralText("Player")), mouseX, mouseY);
            } else if (aX > 29 && aX < 57) {
                this.renderTooltip(matrices, Arrays.asList(new LiteralText("Crafting")), mouseX, mouseY);
            }
        }
    }

    /**
     * Overriden to add the reagents needed to craft the hovered slot.
     *
     * @param matrices
     * @param stack
     * @param x
     * @param y
     */
    @Override
    protected void renderTooltip(MatrixStack matrices, ItemStack stack, int x, int y) {
        List<StringRenderable> text = new ArrayList();
        text.addAll(this.getTooltipFromItem(stack));

        if ((this.focusedSlot instanceof AwakenCraftingResultSlot && getScreenHandler().craftingResultRecipes != null)
                || (this.focusedSlot instanceof AwakenLookUpResultSlot && getScreenHandler().lookUpRecipes != null)) {
            int currentIndex;
            List<AwakenRecipe> recipes;

            if (this.focusedSlot instanceof AwakenCraftingResultSlot) {
                currentIndex = ((AwakenCraftingResultSlot) this.focusedSlot).currentIndex;
                recipes = getScreenHandler().craftingResultRecipes;
            } else {
                currentIndex = ((AwakenLookUpResultSlot) this.focusedSlot).currentIndex;
                recipes = getScreenHandler().lookUpRecipes;
            }

            if (currentIndex < recipes.size()) {
                AwakenRecipe recipe = recipes.get(currentIndex);

                text.add(new LiteralText("---"));

                if (!recipe.recipeType.equals(RecipeType.TWO_BY_TWO)) {
                    text.add(new TranslatableText("awaken.recipe_type.requires." + recipe.recipeType));
                }

                text.add(new LiteralText("To Craft: "));
                for (Reagent reagent : recipe.reagents.keySet()) {
                    String reagentLine = recipe.reagents.get(reagent) + " x " + reagent.toString();
                    List<StringRenderable> split = this.textRenderer.wrapLines(new LiteralText(reagentLine), 126);

                    for (StringRenderable splitLine : split) {
                        text.add(splitLine);
                    }
                }
            }
        }

        this.renderTooltip(matrices, text, x, y);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        this.textRenderer.draw(matrices, this.title, 8.0F, 8.0F, 4210752);
        this.textRenderer.draw(matrices, new TranslatableText(RECIPE_LOOK_UP_TRANSLATION_KEY), 186.0F, 8.0F, 4210752);
        this.textRenderer.draw(matrices, this.playerInventory.getDisplayName(), 8.0F, (float) (this.backgroundHeight - 96 + 4), 4210752);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;

        MinecraftClient.getInstance().getTextureManager().bindTexture(TEXTURE);

        this.drawTexture(matrices, i, j - 28, 0, 166, 28, 32);

        this.drawTexture(matrices, i, j, 0, 0, 176, 166);

        this.drawTexture(matrices, i + 176 + 2, j, 178, 0, 78, 166);

        this.drawTexture(matrices, i + 156, j + 18 + (int) ((float) (52 - 15) * this.outputScrollPosition), 56 + (this.hasOutputsScrollbar() ? 0 : 12), 166, 12, 15);

        this.drawTexture(matrices, i + 242, j + 48 + (int) ((float) (106 - 11) * this.lookUpScrollPosition), 80 + (this.hasRecipeLookUpScrollbar() ? 0 : 6), 166, 6, 11);

        this.drawTexture(matrices, i + 29, j - 28, 28, 198, 28, 32);

        this.setZOffset(100);
        this.itemRenderer.zOffset = 100.0F;
        RenderSystem.enableRescaleNormal();
        ItemStack itemStack = new ItemStack(Blocks.CRAFTING_TABLE);
        this.itemRenderer.renderGuiItemIcon(itemStack, this.x + 29 + (28 - 15) / 2, this.y - 28 + 10);
        this.itemRenderer.renderGuiItemOverlay(this.textRenderer, itemStack, this.x + 29 + (28 - 15) / 2, this.y - 28 + 10);
        this.itemRenderer.zOffset = 0.0F;
        this.setZOffset(0);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        double aX = mouseX - this.x;
        double aY = mouseY - this.y;

        if (aX >= 0 && aY >= 0 && aX < 176 && aY < this.backgroundHeight) {
            if (this.hasOutputsScrollbar()) {
                int i = (this.getScreenHandler().craftingResults.size() + 8 - 1) / 8 - 3;
                this.outputScrollPosition = (float) ((double) this.outputScrollPosition - amount / (double) i);
                this.outputScrollPosition = MathHelper.clamp(this.outputScrollPosition, 0.0F, 1.0F);
                syncCraftingResultScrollbar();

                return true;
            }
        } else if (aX >= 176 + 2 && aY >= 0 && aX < 176 + 2 + 78 && aY < this.backgroundHeight) {
            if (this.hasRecipeLookUpScrollbar()) {
                int i = (this.getScreenHandler().lookUpResults.size() + 3 - 1) / 3 - 6;
                this.lookUpScrollPosition = (float) ((double) this.lookUpScrollPosition - amount / (double) i);
                this.lookUpScrollPosition = MathHelper.clamp(this.lookUpScrollPosition, 0.0F, 1.0F);

                this.getScreenHandler().scrollLookUpResults(this.lookUpScrollPosition);
                syncLookUpResultScrollbar();

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            double aX = mouseX - this.x;
            double aY = mouseY - this.y;

            if (aX > 0 && aX < 28) {
                if (aY > -32 && aY < 0) {
                    AwakenNetworking.CLOSE_CRAFTING_C2S.sendToServer((buf) -> {
                        buf.writeDouble(MouseUtil.getMouseX());
                        buf.writeDouble(MouseUtil.getMouseY());
                    });
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean hasOutputsScrollbar() {
        return this.getScreenHandler().shouldShowOutputsScrollbar();
    }

    private boolean hasRecipeLookUpScrollbar() {
        return this.getScreenHandler().shouldShowLookUpResultsScrollbar();
    }

    /**
     * Sends the server the position of the scroll bar.
     */
    public void syncCraftingResultScrollbar() {
        AwakenNetworking.SYNC_CRAFTING_SCROLLBAR_C2S.sendToServer((buf) -> {
            buf.writeFloat(outputScrollPosition);
        });
    }


    /**
     * Sends the server the position of the scroll bar.
     */
    public void syncLookUpResultScrollbar() {
        AwakenNetworking.SYNC_LOOK_UP_SCROLLBAR_C2S.sendToServer((buf) -> {
            buf.writeFloat(lookUpScrollPosition);
        });
    }
}
