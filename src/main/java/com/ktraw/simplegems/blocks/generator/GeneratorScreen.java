package com.ktraw.simplegems.blocks.generator;

import com.ktraw.simplegems.SimpleGems;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GeneratorScreen extends AbstractContainerScreen<GeneratorContainer> {

    private static final int FLAME_SIDE = 13;

    private ResourceLocation GUI = new ResourceLocation(SimpleGems.MODID, "textures/gui/generator_gui.png");
    private GeneratorContainer typedContainer;

    public GeneratorScreen(GeneratorContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        typedContainer = screenContainer;
    }

    /**
     * Simply calls drawGuiContainerBackgroundLayer
     * @param stack
     * @param partialTicks
     * @param mouseX
     * @param mouseY
     */
    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI);
        final int shiftedX = (width - imageWidth) / 2;
        final int shiftedY = (height - imageHeight) / 2;
        blit(stack, shiftedX, shiftedY, 0, 0, imageWidth, imageHeight);

        final int timer = typedContainer.getDataAt(GeneratorTile.INT_TIMER);
        if (timer > 0) {
            final int scaledProgress = (timer) / (GeneratorTile.PROCESS_TICKS / FLAME_SIDE);

            // blit(matrixStack, xCoord, yCoord, copyFromX, copyFromY, copyFromWidth, copyFromHeight);
            blit(stack, shiftedX + 80, shiftedY + 50 + scaledProgress, 176, scaledProgress, FLAME_SIDE, FLAME_SIDE);
        }
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
        font.draw(stack, "Generator", 8f, 6f, 4210752);
        drawString(stack, Minecraft.getInstance().font, "Energy: " + menu.getEnergy() + " FE", 8, 70, 0x00FF00);
    }
}
