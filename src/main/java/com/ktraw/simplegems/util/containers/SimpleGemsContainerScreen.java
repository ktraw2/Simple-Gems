package com.ktraw.simplegems.util.containers;

import com.ktraw.simplegems.SimpleGems;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public abstract class SimpleGemsContainerScreen<T extends SimpleGemsContainerMenu> extends AbstractContainerScreen<T> {

    protected final T typedContainerMenu;
    protected final ResourceLocation GUI;
    protected final String containerTitle;

    public SimpleGemsContainerScreen(T screenContainerMenu, Inventory inv, Component titleIn, String containerTitle, String textureLocation) {
        super(screenContainerMenu, inv, titleIn);
        this.typedContainerMenu = screenContainerMenu;
        this.containerTitle = containerTitle;
        GUI = new ResourceLocation(SimpleGems.MODID, textureLocation);
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        renderBaseBg(stack, partialTicks, mouseX, mouseY);
    }

    protected Pair<Integer, Integer> renderBaseBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI);
        final int shiftedX = (width - imageWidth) / 2;
        final int shiftedY = (height - imageHeight) / 2;
        blit(stack, shiftedX, shiftedY, 0, 0, imageWidth, imageHeight);

        return new Pair<>(shiftedX, shiftedY);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
        font.draw(stack, containerTitle, 8f, 6f, 4210752);
        drawString(stack, Minecraft.getInstance().font, "Energy: " + menu.getEnergy() + " FE", 8, 70, 0x00FF00);
    }
}
