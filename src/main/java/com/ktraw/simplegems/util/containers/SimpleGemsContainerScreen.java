package com.ktraw.simplegems.util.containers;

import com.ktraw.simplegems.SimpleGems;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nonnull;

public abstract class SimpleGemsContainerScreen<T extends SimpleGemsContainerMenu> extends AbstractContainerScreen<T> {

    protected final T typedContainerMenu;
    protected final ResourceLocation gui;
    protected final String containerTitle;

    public SimpleGemsContainerScreen(T screenContainerMenu, Inventory inv, Component titleIn, String containerTitle, String textureLocation) {
        super(screenContainerMenu, inv, titleIn);
        this.typedContainerMenu = screenContainerMenu;
        this.containerTitle = containerTitle;
        this.gui = new ResourceLocation(SimpleGems.MODID, textureLocation);
    }

    @Override
    protected void renderBg(
            @Nonnull final GuiGraphics graphics,
            final float partialTicks,
            final int mouseX,
            final int mouseY
    ) {
        renderBaseBg(graphics, partialTicks, mouseX, mouseY);
    }

    protected Pair<Integer, Integer> renderBaseBg(
            final GuiGraphics graphics,
            final float partialTicks,
            final int mouseX,
            final int mouseY
    ) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, gui);
        final int shiftedX = (width - imageWidth) / 2;
        final int shiftedY = (height - imageHeight) / 2;
        graphics.blit(gui, shiftedX, shiftedY, 0, 0, imageWidth, imageHeight);

        return new Pair<>(shiftedX, shiftedY);
    }

    @Override
    public void render(
            @Nonnull final GuiGraphics graphics,
            final int mouseX,
            final int mouseY,
            final float partialTicks
    ) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(
            @Nonnull final GuiGraphics graphics,
            int mouseX,
            int mouseY
    ) {
        graphics.drawString(font, containerTitle, 8, 6, 4210752, false);
        graphics.drawString(font, "Energy: " + menu.getEnergy() + " FE", 8, 70, 0x00FF00);
    }
}
